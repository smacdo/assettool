package scott.assettool;

public class Vector3
{
    public Vector3( float x, float y, float z )
    {
        m_x = x;
        m_y = y;
        m_z = z;
    }

    public Vector3( Vector3 v )
    {
        m_x = v.m_x;
        m_y = v.m_y;
        m_z = v.m_z;
    }

    public float x()
    {
        return m_x;
    }

    public float y()
    {
        return m_y;
    }

    public float z()
    {
        return m_z;
    }

    public String toString()
    {
        return "<vec3; " + m_x + ", " + m_y + ", " + m_z + ">";
    }

    public static Vector3 add( Vector3 lhs, Vector3 rhs )
    {
        return new Vector3(
            lhs.m_x + rhs.m_x,
            lhs.m_y + rhs.m_y,
            lhs.m_z + rhs.m_z
        );
    }

    private float m_x;
    private float m_y;
    private float m_z;
}
