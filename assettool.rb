#!/usr/bin/ruby
#######################################################################
# Simple .obj + .mtl reader
#
$materials = {}
$VERBOSE   = true
$DEBUG     = false
$output    = :lua
$CONSTANTS = 
{
    :ASSET_MODEL     => 10,
    :ASSET_MATERIAL  => 20,
    :MODEL_VTX_ANIM  => 1,
    :MODEL_BONE_ANIM => 2
}

class Mesh
    attr_reader :index_buffers, :vertex_buffer, :groups

    def initialize( name, ib, vb, groups, info )
        @name          = name
        @index_buffers = ib
        @vertex_buffer = vb
        @groups        = groups
        @info          = info
    end

    def normals?
        return @info[:normals] == true
    end

    def texture_coords?
        return @info[:texture] == true
    end
    
    def materials?
        return @info[:materials] == true
    end

    def frame_count
        return 1
    end
    
    def anim_group_count
        return 1
    end

    def face_count
        count = 0

        #
        # Determine face count
        #
        @groups.each_with_index do |group,index|
            count += @index_buffers[ group[:name] ].size
        end

        return count / 3
    end

    def to_lua( s )
        #
        # Asset header
        #
        s << "Asset(\"mesh\", \"va\", \"#{@name}\", {\n"

        s << "\tmeshinfo = {\n"
        s << "\t\tvformat = 'simple'\n"
        s << "\t\tmeshgroups = #{@groups.size},\n"
        s << "\t\tanimgroups = 1,\n"
        s << "\t\tframecount = #{@vertex_buffer.size},\n"
        s << "\t\tfacecount  = #{self.face_count}\n"
        s << "\t},\n"

        #
        # Write mesh group information out
        #  { [group_name, mat_name, num_faces, ofs_faces }
        #
        offset = 0
        s << "\tmeshgroups = {\n"

        @groups.each_with_index do |group,index|
            indexcount  = @index_buffers[group[:name]].size
            facecount   = indexcount / 3
            matname     = prettyStr( group[:material] )

            s << "\t\t{#{group[:name]}, #{matname}, "
            s << "#{facecount}, #{offset}},\n"

            offset    += indexcount
        end

        s << "\t},\n"

        #
        # Write animation groups out
        #  { [name, num_frames, ofs_frames, frameTickTime] }
        #
        s << "\tanimgroups = {\n"
        s << "\t\t{'__default', 1, 0, 0}\n"
        s << "\t},\n"

        
        #
        # Write index buffers out
        #
        s << "\tindexbuffer = {\n"

        @groups.each_with_index do |group,index|
            s << "\t\t"
            s << print_tuple_lua( @index_buffers[ group[:name] ] )
            s << "\n"
        end

        s << "\t},\n"

        #
        # Write vertex buffer out
        #
        s << "\tvertex_buffer = {\n"

        @vertex_buffer.each do |v|
            s << "\t\t{ "
            s << print_tuple_lua( v )
            s << " },\n"
        end

        s << "\t}\n"

        # Close it out
        s << "})\n"
    end

    def to_binary( s )
        asset_flags = 0
        model_flags = 0
        
        model_flags += 1                  # Always has groups
        model_flags += 2 if normals?
        model_flags += 4 if texture_coords?
        model_flags += 8 if materials?

        #
        # Asset header
        #
        s << [ 89,
               65, 83, 83, 69, 84,
               77, 69, 83, 72,
               13, 10, 26, 10,
               1,
               asset_flags,
               @name
        ].pack("C16a32")

        #
        # Model header
        #
        s << [ $CONSTANTS[:MODEL_VTX_ANIM],
               model_flags, 
               @groups.size,
               self.anim_group_count,
               self.frame_count,
               self.face_count,
               @vertex_buffer.size ].pack("CCCCiii")

        #
        # Write group information out
        #
        offset = 0
        @groups.each_with_index do |group,index|
            indexcount  = @index_buffers[group[:name]].size
            facecount   = indexcount / 3

            s << [ prettyStr( group[:name]     ),
                   prettyStr( group[:material] ),
                   facecount,
                   offset
                 ].pack("a32a32ii")

            offset    += indexcount
        end

        #
        # Write animation data out
        #  [ anim_name, frames, offset, time, reserved ]
        #
        s << [ "__default",
               1,
               0,
               0.0,
               0 ].pack("a32iifi")

        #
        # Write index buffers out
        #
        @groups.each_with_index do |group,index|
            s << @index_buffers[ group[:name] ].pack("I*")
        end

        #
        # Write vertex buffer out
        #
        @vertex_buffer.each do |v|
            s << v.pack("ffffffff")
        end
    end    
end

class Material
    attr_accessor :name, :ambient, :diffuse, :specular, :alpha
    attr_accessor :shininess, :shading_model, :texture_map

    def initialize( name )
        @name = name
        @ambient       = nil
        @diffuse       = nil
        @specular      = nil
        @emissive      = [ 0.0, 0.0, 0.0 ]
        @alpha         = nil
        @shininess     = nil
        @shading_model = nil
        @texture_map   = nil
    end

    def to_lua( buffer )
        #
        # Material flags
        #
        buffer << "Asset(\"material\", \"simple\", \"#{name}\", {\n"

        buffer << "\tflags = {\n"
        buffer << "\t\trecv_shadow = true,\n"
        buffer << "\t\tdepth_check = true,\n"
        buffer << "\t\tdepth_write = true,\n"
        buffer << "\t\tlighting    = true,\n"
        buffer << "\t\twrite_color = true\n"
        buffer << "\t},\n"

        #
        # Passes
        #
        buffer << "\tpasses = {\n"

        #
        # Pass 1
        #
        buffer << "\t\t{\n"

        #####################################################################
        # Texture Pass Settings (64 bytes)
        #   F3 ambient                  12 / 12
        #   F3 diffuse                  12 / 24
        #   F3 specular                 12 / 36
        #   F3 emissive                 12 / 48
        #   F  alpha                     4 / 54
        #   F  shininess                 4 / 58
        #   B  shading_model   (0=flat, 1=gouraud, 2=phong)    1 / 59
        buffer << "\t\t\tambient  = #{print_tuple_lua(@ambient)},\n"
        buffer << "\t\t\tdiffuse  = #{print_tuple_lua(@diffuse)},\n"
        buffer << "\t\t\tspecular = #{print_tuple_lua(@specular)},\n"
        buffer << "\t\t\temissive = 0.0,\n"
        buffer << "\t\t\tshiny    = 0.0,\n"
        buffer << "\t\t\tshading  = 'normal',\n"
    
        #   B depth_func (0=fail,1=pass,2=lt,3=le,4=eq,5=ne,6=gt,7=ge)  1 / 60
        #   B cull func (0=none,1=cw,2=ccw)                             1 / 61
        #   B polygon mode (0=solid, 1=wireframe, 2=points)             1 / 62
        #   B flags                                                     1 / 63
        #   B number of textures                                        1 / 64
        buffer << "\t\t\tdepth    = 'lt',\n"
        buffer << "\t\t\tcull     = 'none',\n"
        buffer << "\t\t\tpolymode = 'solid',\n"
        buffer << "\t\t\tflags    = {},\n"
        buffer << "\t\t\ttexcount = 1,\n"

        #####################################################################
        # Texture (64 bytes)
        # 
        #    address_*: 0 = wrap, 1 = clamp, 2 = mirror, 3 = border
        #    (see colour_op_ex)
        #
        #  [  F2 scale,                 8 /  8
        #     F2 rotate,                8 / 16
        #     F2 scroll,                8 / 24
        #     B scale_type_x,           1 / 25  (1=replace,2=add,3=modulate,
        #     B scale_type_y,           1 / 26   4=alphablend)
        #     B color_op                1 / 27
        #     B alpha_color_op          1 / 28
        #     B4 reserved               4 / 32
        #     S texture_path[32]       32 / 64
        #
        buffer << "\t\t\ttextures = {\n"
        
        buffer << "\t\t\t\tscale    = { 1.0, 1.0 },\n"
        buffer << "\t\t\t\trotate   = { 0.0, 0.0 },\n"
        buffer << "\t\t\t\tscale    = { 1.0, 1.0 },\n"
        buffer << "\t\t\t\tst_x     = 'wrap',\n"
        buffer << "\t\t\t\tst_y     = 'wrap',\n"
        buffer << "\t\t\t\tcolorop  = 'replace',\n"
        buffer << "\t\t\t\tacolorop = 'replace',\n"
        buffer << "\t\t\t\tfile     = '#{prettyStr(@texture_map)}',\n"

        buffer << "\t\t\t}\n"

        # End pass
        buffer << "\t\t},\n"

        # End passes
        buffer << "\t}\n"

        # End texture asset
        buffer << "})\n"
    end

    def to_binary( buffer )
        #
        # Material flags
        #
        mat_flags = 0

        mat_flags += 0              # 1, unused
        mat_flags += 0              # 2, uses shader (otherwise mat def)
        mat_flags += 4              # 4, receives shadows
        mat_flags += 8              # 8, depth check
        mat_flags += 16             # 16, depth write
        mat_flags += 32             # 32, lighting
        mat_flags += 64             # 64, write color
        mat_flags += 0              # 128, unused

        #####################################################################
        # Material asset header (48 bytes)
        #
        buffer << [ 89,
               65, 83, 83, 69, 84,
               77, 84, 82, 76,
               13, 10, 26, 10,
               1,
               0,
               @name
        ].pack("C16a32")

        #####################################################################
        # Material header (4 bytes)
        #   B material flags             1 / 1
        #   B texture passes             1 / 2
        #   B unused                     1 / 3
        #   B unused                     1 / 4
        buffer << [ mat_flags, 1, 0, 0 ].pack("CCCC")

        #####################################################################
        # Texture Pass Settings (64 bytes)
        #   F3 ambient                  12 / 12
        #   F3 diffuse                  12 / 24
        #   F3 specular                 12 / 36
        #   F3 emissive                 12 / 48
        #   F  alpha                     4 / 52
        #   F  shininess                 4 / 56
        #   B  shading_model   (1=flat, 2=gouraud, 3=phong)    1 / 57
        shading = ( @shading_model == :flat ? 1 : 2 )

        buffer << @ambient.pack("f3")
        buffer << @diffuse.pack("f3")
        buffer << @specular.pack("f3")
        buffer << @emissive.pack("f3")
        buffer << [ @alpha, @shininess, shading ].pack("ffC")
    
        #   B depth_func (0=fail,1=pass,2=lt,3=le,4=eq,5=ne,6=gt,7=ge)  1 / 58
        #   B cull func (0=none,1=cw,2=ccw)                    1 / 59
        #   B polygon mode (0=solid, 1=wireframe, 2=points)    1 / 60
        #   B texture count                                    1 / 61
        #   B2 scene_blend_op                                  2 / 63
        #   B flags                                            1 / 64
        buffer << [ 2, 0, 0, 1, 0, 0, mat_flags ].pack("C7")

        #####################################################################
        # Texture (64 bytes)
        # 
        #    address_*: 0 = wrap, 1 = clamp, 2 = mirror, 3 = border
        #    (see colour_op_ex)
        #
        #  [  B  type                   1 /  1 (1d,2d,3d,cubic)
        #     B  uv_set                 1 /  2 (default 1)
        #     F2 scale,                 8 / 10
        #     F2 rotate,                8 / 18
        #     F2 scroll,                8 / 26
        #     B scale_type_x,           1 / 27 (wrap,clamp,mirror,border)
        #     B scale_type_y,           1 / 28   
        #     B color_op                1 / 29 (1=replace, 2=add,3=modulate,
        #     B alpha_color_op          1 / 30  4=alphablend,5=min,6=max)
        #     B2 reserved               4 / 32
        #     S texture_path[32]       32 / 64
        #     
        buffer << [ 2,
               1,
               1.0, 1.0,
               0.0, 0.0,
               0.0, 0.0,
               1,
               1,
               2,
               2,
               0,
               0,
               @texture_map ].pack("CCf6C4C2a32")
    end
end

class MeshConstructionBuffer
    def initialize
        # Mesh groups (by group name)
        @groups     = []

        # Construction arrays (vp = vertex postion, vn = normal, vt = texture)
        @vp         = []
        @vn         = []
        @vt         = []

        @facegroups = {}

        # Mesh material list
        @materials = {}

        # Number of faces
        @fcount = 0
    end

    def add_pos( x, y, z )
        @vp << [ x, y, z ]
    end

    def add_normal( x, y, z )
        @vn << [ x, y, z ]
    end

    def add_tex( u, v )
        @vt << [ u, v ]
    end

    def add_group( group )
        @groups << group
        @facegroups[group] = []
    end

    def add_material( group, material )
        @materials[group] = material
    end

    def add_face( group, f0, f1, f2 )
        if @facegroups[group] == nil
            puts "WARNING: Group '#{group}' was not defined, creating...\n"
            add_group( group )
        end

        # Add face specifier to the group
        #  [ ... [[a,b,c], [b,c,d], [x,y,z]] ]
        @facegroups[group] << [ f0, f1, f2 ] 

        # Update counter
        @fcount += 1
    end

    ###
    ### Create a compiled mesh from this obj
    ###
    def compile( filename )
        index_buffers  = {}
        vertex_buffer  = []
        vertex_key_map = {}
        info           = { :texture => false, :normals => false }

        #
        # There is a facegroup for every named group in the mesh. Iterate
        # through each one of these groups, and generate index buffers for
        # them.
        #
        @facegroups.each do |name,group|
            index_buffer = []

            group.each do |face|
                # Go through each element of the face (3)
                face.each do |f|
                    key   = "#{f[0]}:#{f[1]}:#{f[2]}"
                    index = vertex_key_map[key]

                    # Check if this index is in the cache
                    if index == nil
                        # Add a new vertex
                        p = @vp[f[0] - 1]
                        t = (f[1] == nil ? [0.0,0.0]     : @vt[f[1]-1])
                        n = (f[2] == nil ? [0.0,0.0,0.0] : @vn[f[2]-1])
                        v = [ p[0], p[1], p[2], t[0], t[1], n[0], n[1], n[2] ]

                        index                 = vertex_buffer.length
                        vertex_key_map[key]   = index
                        vertex_buffer[index] = v

                        # Mark any missing fields (for exporting later)
                        info[:texture] = true if f[1] != nil
                        info[:normals] = true if f[2] != nil

                    else
                        puts "cache hit on '#{key.inspect}'" if $DEBUG
                    end

                    # Add it to the current index buffer
                    index_buffer << index
                end
            end

            # Add it to list of index buffers
            index_buffers[name] = index_buffer
        end

        #
        # Construct an array of groups describing the properties of each
        # group in the mesh
        #
        groups = []
        hasmat = false

        @groups.each do |group|
            groups << { :name     => group, 
                        :material => @materials[group] }

            if ( @materials[group] != nil )
                hasmat = true
            end
        end

        # does the mesh have ANY materials?
        info[:materials] = hasmat

        #
        # Now construct a mesh and return it
        #
        Mesh.new( filename, index_buffers, vertex_buffer, groups, info )
    end

    def print
        puts "Model has #{@vp.size} positions, #{@vn.size} normals, #{@vt.size} texcoords"
        puts "texcoords, #{@facegroups.size} groups and #{@fcount} faces."

        if @index_buffer != nil
            puts "\n"
            puts "Index Buffers: #{@index_buffer.inspect} "

            puts "Vertex Buffer: #{@vertex_buffer.inspect} "
        end
    end
end

class Model

end

def prettyStr( str )
    if ( str == nil )
        return "nil"
    else
        return str
    end
end

def error( msg,line )
    puts("ERROR: #{msg}")
    puts("LINE: #{line}")
    exit
end

def print_tuple_lua( *chunks )
    "{#{chunks.join(", ")}}"
end

def check_floats( *chunks )
    chunks.each do |chunk|
        return false unless /^\-?(\d+(\.[\d+])?)|(\.\d+)$/.match(chunk)
    end

    return true
end

def check_ints( *chunks )
    chunks.each do |chunk|
        return false unless /^-?\d+/.match(chunk)
    end

    return true
end

def check_notnull( *chunks )
    chunks.each do |chunk|
        return false if chunk == nil
    end

    return true
end

def parse_tuple( tuplestr )
    # Convert the tuple into an array of floats
    a = tuplestr.split( /\// ).map { |t| /^\d+$/.match(t) ? t.to_i : nil }
    i = 3 - a.size      # Number of elements missing from a correct 3-tuple

    if i > 3
        # Can't have face tuple with more than three components
        return nil
    elsif i > 0
        # Tuple neglected to specify last 1 or 2 tuple elements. Add them
        i.times { |c| a << nil }
    end

    if a[0] == nil
        # We can't have a face without a position.
        return nil
    else
        # Looks good
        return a
    end
end

#############################################################################
# Imports an obj model file
#############################################################################
def importObjFile( path, filename )
    lines = readFileAsArray( path )
    mesh  = MeshConstructionBuffer.new
    group = "__root"

    # Parse each line as a command
    lines.each do |line|
        chunks = parseObjLine( line )

        # Skip blank lines
        next if chunks.size == 0

        case chunks[0]
            when "g"
                # Make sure we have a name
                error("Group does not have name",line) if chunks.size < 2
                temp = chunks[1, chunks.length-1].join("_")

                # Check that the name is valid
                error("Group name '#{temp}' invalid",line) unless 
                                            /[A-Za-z0-9_-]/.match(temp)

                # Add the group to the model, and set it to be active group
                group = temp
                mesh.add_group( group )

            when "v"
                # Make sure we have enough elements
                error("Not enough chunks for v",line) if chunks.size != 4
                
                # Get the coordinates
                c = chunks[1,3]

                # Check that they're all numerics
                error("Invalid position",line) unless check_floats(c[0],c[1],c[2])

                # Add it to the buffer
                mesh.add_pos( c[0].to_f, c[1].to_f, c[2].to_f )

            when "vn"
                # Make sure we have enough elements
                error("Not enough chunks for vn",line) if chunks.size != 4
                
                # Get the coordinates
                c = chunks[1,3]

                # Check that they're all numerics
                error("Invalid normal",line) unless check_floats(c[0],c[1],c[2])

                # Add it to the buffer
                mesh.add_normal( c[0].to_f, c[1].to_f, c[2].to_f )

            when "vt"
                # Make sure we have enough elements
                error("Not enough chunks for vt",line) if chunks.size != 3
                
                # Get the coordinates
                c = chunks[1,2]

                error("3d tex not supported",line) unless c[2] == nil

                # Check that they're all numerics
                error("Invalid texcoord",line) unless check_floats(c[0],c[1])

                # Add it to the buffer
                mesh.add_tex( c[0].to_f, c[1].to_f )

            when "f"
                # Make sure we have enough elements
                error("Invalid face count",line) if chunks.size != 4

                # Get the tuples
                f0 = parse_tuple(chunks[1])
                f1 = parse_tuple(chunks[2])
                f2 = parse_tuple(chunks[3])

                # Make sure they're all valid
                error("Invalid face",line) unless check_notnull(f0,f1,f2)

                # Add the face
                mesh.add_face( group, f0, f1, f2 )

           when "usemtl"
                # Make sure there is a material name
                error("Invalid material name",line) if chunks.size != 2

                # Assign the material
                mesh.add_material( group, chunks[1] )
        end
    end

    mesh.compile( filename )
end

#############################################################################
# Imports a mtl texture file
#############################################################################
def importMtlFile( path )
    lines            = readFileAsArray( path )
    current_material = nil
    materials        = []

    # Parse each line as a command
    lines.each do |line|
        chunks = parseObjLine( line )

        # Skip blank lines
        next if chunks.size == 0

        # Determine what to do
        case chunks[0].downcase
            when "newmtl"
                # Make sure we have a name
                error("Material does not have name",line) if chunks.size != 2
                name = chunks[1]

                # Check that the name is valid
                error("Material name '#{name}' invalid",line) unless 
                                            /[A-Za-z0-9_-]/.match(name)

                # Before we create a new material, were we parsing an older
                # material that needs to be saved?
                if ( current_material != nil )
                    materials << current_material
                end

                # Create a new material to represent the incoming mateiral def
                current_material = Material.new( name )

            when "ka"
                # Ambient term (r g b)
                error("Ambient term needs r g b",line) if chunks.size != 4

                # Get the tuples
                r = chunks[1]
                g = chunks[2]
                b = chunks[3]

                # Make sure they're all valid
                error("Ambient [r,g,b] invalid",line) unless check_floats(r,g,b)

                # Set material
                current_material.ambient = [r.to_f, g.to_f, b.to_f]

            when "kd"
                # Diffuse term (r g b)
                error("Diffuse term needs r g b",line) if chunks.size != 4

                # Get the tuples
                r = chunks[1]
                g = chunks[2]
                b = chunks[3]

                # Make sure they're all valid
                error("Diffuse [r,g,b] invalid",line) unless check_floats(r,g,b)

                # Set material
                current_material.diffuse = [r.to_f, g.to_f, b.to_f]

            when "ks"
                # Specular term (r g b)
                error("Specular term needs r g b",line) if chunks.size != 4

                # Get the tuples
                r = chunks[1]
                g = chunks[2]
                b = chunks[3]

                # Make sure they're all valid
                error("Specular [r,g,b] invalid",line) unless check_floats(r,g,b)

                # Set material
                current_material.specular = [r.to_f, g.to_f, b.to_f]

            when "d"
                # Alpha value
                error("Alpha term needs value",line) if chunks.size != 2

                # Get the tuples
                a = chunks[1]

                # Make sure they're all valid
                error("Alpha term invalid",line) unless check_floats(a)

                # Set material
                current_material.alpha = a.to_f

            when "tr"
                # Alpha term
                error("Alpha term needs value",line) if chunks.size != 2

                # Get the tuples
                a = chunks[1]

                # Make sure they're all valid
                error("Alpha term invalid",line) unless check_floats(a)

                # Set material
                current_material.alpha = a.to_f

            when "ns"
                # Shininess
                error("Shininess needs value",line) if chunks.size != 2

                # Get the tuples
                s = chunks[1]

                # Make sure they're all valid
                error("Shininess term invalid",line) unless check_floats(s)

                # Set material
                current_material.shininess = s.to_f

            when "illum"
                # Illumination model (1 = flat, no spec. 2 = with specs)
                error("Illumination model needs value",line) if chunks.size != 2

                # Get the tuples
                m = chunks[1]

                # Make sure they're all valid
                if ( m != "1" and m != "2" )
                    error("Illumination model needs to be 1 or 2",line)
                end

                # Set material
                if ( m == "1" )
                    current_material.shading_model = :flat
                else
                    current_material.shading_model = :default
                end
            
            when "map_ka"
                # Illumination model (1 = flat, no spec. 2 = with specs)
                error("Texture map needs value",line) if chunks.size != 2

                # Get the tuples
                t = chunks[1]

                # Make sure texture exists
                error("Texture path invalid",line) unless File.exists?(t)

                # Set material
                current_material.texture_map = t

           when "map_kd"
                 # Illumination model (1 = flat, no spec. 2 = with specs)
                error("Texture map needs value",line) if chunks.size != 2

                # Get the tuples
                t = chunks[1]

                # Make sure texture exists
                error("Texture path invalid",line) unless File.exists?(t)

                # Set material
                current_material.texture_map = t
        end
    end

    #
    # Make sure to commit the last current_material into the group
    #
    if ( current_material != nil )
        materials << current_material
    end

    #
    # Now copy each material into the global material cache
    #
    materials.each do |material|
        $materials[ material.name ] = material
    end

    materials
end

#############################################################################
# Parse an obj "command"
#############################################################################
def parseObjLine( line )
    if line == nil
        return []
    end

    # Remove any comments
    line = line[ 0, line.index("#") ] if line.index("#") != nil 

    # Split the command line, return it
    return line.split( /\s+/ ).select { |i| i != "" }
end

#############################################################################
# Reads a text file in as an array of lines
#############################################################################
def readFileAsArray( path )
    lines = []

    File.open( path, "r" ) do |f|
        while ( line = f.gets )
            lines << line.chomp
        end
    end

    lines
end

#############################################################################
### Run app                                                               ###
###  TODO: make app run like                                              ###
###         ./app <output_dir> <file_1> <file_2> ...                      ###
#############################################################################
exit("Not enough arguments") if ARGV.size == 0

console_out = false
#output_dir  = "output"
output_dir  = "/home/scott/sandbox/opengl/content"
output_fmt  = :binary

#
# Import each asset
#
ARGV.each do |filename|
    #
    # Check the file type, and perform the correct import type on it the
    # file.
    #
    str  = String.new
    name = nil
    ext  = nil

    #
    # Load the asset in, and then convert it to the desired output
    # format
    #
    if    /([A-Za-z0-9_ ]\/)*([A-Za-z0-9_ ]+)\.obj$/.match( filename )
        name = $2
        mesh = importObjFile( filename, name )

        if output_fmt == :lua
            mesh.to_lua( str )
            ext = "amesh"
        elsif output_fmt == :binary
            mesh.to_binary( str )
            ext = "mesh"
        else
            puts "Unexpected output format :("
            exit
        end

    elsif /([A-Za-z0-9_ ]\/)*([A-Za-z0-9_ ]+)\.mtl$/.match( filename )
        name = $2
        mats = importMtlFile( filename )

        # Now export the material
        mats.each do |material|
            if output_fmt == :lua
                material.to_lua( str )
                ext = "amaterial"
            elsif output_fmt == :binary
                material.to_binary( str )
                ext = "material"
            else
                puts "Unexpected output format :("
                exit
            end
        end

    else
        puts "Failed to import file #{filename}: Filetype not recogonized"
    end

    #
    # Write the asset out (either to disk or standard out)
    #
    filepath = "#{output_dir}/#{name}.#{ext}"

    if ( console_out )
        puts str
    else
        File.open( filepath, "w" ) do |file|
            file << str
        end

        puts "WROTE: #{filepath}"
    end
end

#
# Now that we've imported each asset and generated all needed data to support
# these assets, write them out in the desired output format
#   (eg, lua data format or binary format)
#

puts " "
puts "All done"
