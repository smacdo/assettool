package scott.assettool;

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

    public String name()
    {
        return m_name;
    }

    public String material()
    {
        return m_material;
    }

    public void addFace( Face f )
    {
        m_faces.add( f );
    }

    public void addFace( int a, int b, int c )
    {
        assert a >= 0;

        m_faces.add( new Face( a, b, c ) );
    }

    /**
     * Name of the face group
     */
    private String m_name;

    /**
     * Associated material name for the face group
     */
    private String m_material;

    /**
     * List of faces in the group
     */
    private ArrayList<Face> m_faces;

    /**
     * Default number of faces predicted to be in the group
     * (Used to pre-size face group arrayh)
     */
    private static final int DefaultFaceCount = 512;
}
