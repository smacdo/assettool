package scott.assettool;

public final class Vector3 extends Vector
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

    public Vector3 normalized()
    {
        float abv = Math.sqrt( m_x * m_x + m_y * m_y + m_z * m_z );

        if ( abv == 0.0f )  // equals close?
        {
            throw new MathException("Cannot normalize this vector");
        }

        double k = 1.0 / abv;

        return new Vector3( m_x * k, m_y * k, m_z * k );
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

    private float m_x;
    private float m_y;
    private float m_z;
}
