package scott.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class Mesh
{
    public Mesh( String name )
    {
        m_name         = name;
        m_vertices     = new ArrayList<Vertex>( DefaultVertexCount );
        m_faceGroups   = new ArrayList<FaceGroup>();
        m_groupNameMap = new Hashtable<String,FaceGroup>();
    }

    public Mesh( String               name,
                 ArrayList<Vertex>    vertices,
                 ArrayList<FaceGroup> facegroups )
    {
        m_name         = name;
        m_vertices     = new ArrayList<Vertex>( vertices );
        m_faceGroups   = new ArrayList<FaceGroup>( facegroups );
        m_groupNameMap = new Hashtable<String,FaceGroup>();        
    }
    
    /**
     * Return the name of the mesh
     * @return Name of the mesh
     */
    public String name()
    {
        return m_name;
    }
    
    /**
     * Returns a list of vertices for this mesh
     * @return
     */
    public List<Vertex> vertices()
    {
        return Collections.unmodifiableList( m_vertices );
    }
    
    public List<FaceGroup> facegroups()
    {
        return Collections.unmodifiableList( m_faceGroups );
    }

    /**
     * Name of the mesh
     */
    private String m_name;

    /**
     * List of vertices in the mesh
     */
    private ArrayList<Vertex> m_vertices;

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
}
