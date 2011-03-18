package scott.geometry;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Constructs meshes on the fly
 */
public class MeshBuilder
{
    public MeshBuilder( String name )
    {
        m_name         = name;
        m_bones        = new ArrayList<Bone>( DefaultBoneCount );
        m_vertices     = new ArrayList<Vertex>( DefaultVertexCount );
        m_faceGroups   = new ArrayList<FaceGroup>();
        m_groupNameMap = new Hashtable<String,FaceGroup>();
    }

    /**
     * Adds a named face group to the mesh
     */
    void addGroup( String group, String material )
    {
        // Make sure the group does not exist
        if ( m_groupNameMap.containsKey( group ) )
        {
    //        throw new MeshBuilderException(
    //          "Mesh already contains group '" + group + "'" );
        }

        FaceGroup fg = new FaceGroup( group, material );

        m_groupNameMap.put( group, fg );
        m_faceGroups.add( fg );
    }

    /**
     * Sets a face group's material name
     */
    void setGroupMaterial( String group, String material )
    {
        // Make sure group exists
        if (! m_groupNameMap.containsKey( group ) )
        {
    //        throw new MeshBuilderException(
    //                "Mesh does not define group '" + group + "'" );
        }

        FaceGroup fg = new FaceGroup( group, material );

        m_groupNameMap.put( group, fg );
        m_faceGroups.add( fg );
    }

    void addBone( Bone b )
    {
        m_bones.add( b );
    }

    /**
     * Adds a face composed of three vertices to the mesh. Verts must
     * be specified in counter clockwise order
     */
    void addFace( String group, Vertex va, Vertex vb, Vertex vc )
    {
        // Make sure the vertices are correctly specified
   //     if ( calculateWinding( va, vb, vc ) != Winding.CounterClockwise )
   //     {
   //         throw new MeshBuilderException(
   //                 "Vertex winding is not counter clockwise"
   //         );
   //     }

        // Find a stored vertex offset for each given vertex. Either
        // they already exist in the vertex buffer, or we need to create
        // it.
   //     int a = findVertexOffset( va );
   //     int b = findVertexOffset( vb );
   //     int c = findVertexOffset( vc );

   //     m_faceGroups.get( group ).addFace( a, b, c );
    }

    /**
     * Adds a face composed of four vertices to the mesh. Verts must
     * be specified in counter clockwise order. This will split the
     * quad into two separate triangles
     */
    void addFace( String group,
                  Vertex a,
                  Vertex b,
                  Vertex c,
                  Vertex d )
    {
  //      throw new MeshBuilderException( "Not supported yet" );
    }

    /**
     * Recaculates normals. This calculate normals per face, and then
     * average the values across shared vertices.
     */
    void recaculateNormals()
    {

    }

    private int getVertexOffset( Vertex v )
    {
        return 0;
    }

    /**
     * Name of the mesh
     */
    private String m_name;

    /**
     * Flag specifying if we should merge shared vertices
     */
    private boolean m_mergeSharedVerts;

    /**
     * List of vertices in the mesh
     */
    private ArrayList<Vertex> m_vertices;

    /**
     * Cache of vertices in the mesh, and their vertex buffer position
     */
//    private Hashtable<Vertex, int> m_vertexCache;

    /**
     * List of bones in the mesh
     */
    private ArrayList<Bone> m_bones;

    /**
     * List of face groups in the mesh
     */
    private ArrayList<FaceGroup> m_faceGroups;

    /**
     * List of the names of face groups in the mesh, mapped to their
     * corresponding face group
     */
    private Hashtable<String,FaceGroup>  m_groupNameMap;

    /**
     * Default number of vertices in a mesh
     */
    private static final int DefaultVertexCount = 4096;

    private static final int DefaultBoneCount = 64;
}
