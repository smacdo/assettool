package scott.assettool;

import java.util.ArrayList;
import java.util.Hashtable;

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

    /**
     * Used to keep track of faces in the mesh
     */
    private class FaceGroup
    {
        public FaceGroup( String name, String material )
        {
            m_name     = name;
            m_material = material;
            m_faces    = new ArrayList<Face>( DefaultFaceCount );
        }

        /**
         * Name of the face group
         */
        public String m_name;

        /**
         * Associated material name for the face group
         */
        public String m_material;

        /**
         * List of faces in the group
         */
        public ArrayList<Face> m_faces;

        /**
         * Default number of faces predicted to be in the group
         * (Used to pre-size face group arrayh)
         */
        public static final int DefaultFaceCount = 512;
    }
}
