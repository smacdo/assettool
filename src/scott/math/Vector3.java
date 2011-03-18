package scott.assettool;

import scott.math.MathException;

public final class Vector3 extends Vector
{
    public Vector3( float x, float y, float z )
    {
        m_x = x;
        m_y = y;
        m_z = z;
    }

    public Vector3( double x, double y, double z )
    {
        m_x = (float) x;
        m_y = (float) y;
        m_z = (float) z;
    }

    public Vector3( Vector3 v )
    {
        m_x = v.m_x;
        m_y = v.m_y;
        m_z = v.m_z;
    }

    public Vector3 normalized()
        throws MathException
    {
        float abv = (float) Math.sqrt( m_x * m_x + m_y * m_y + m_z * m_z );

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

    public float length()
    {
        return (float) Math.sqrt( m_x * m_x + m_y * m_y + m_z * m_z );
    }

    public float fastLength()
    {
        return m_x * m_x + m_y * m_y + m_z * m_z;
    }

    public static Vector3 add( Vector3 lhs, Vector3 rhs )
    {
        return new Vector3( lhs.m_x + rhs.m_x,
                            lhs.m_y + rhs.m_y,
                            lhs.m_z + rhs.m_z );
    }

    public static Vector3 sub( Vector3 lhs, Vector3 rhs )
    {
        return new Vector3( lhs.m_x - rhs.m_x,
                            lhs.m_y - rhs.m_y,
                            lhs.m_z - rhs.m_z );
    }

    public static Vector3 scale( Vector3 v, float s )
    {
        return new Vector3( v.m_x * s, v.m_y * s, v.m_z * s );
    }

    public static float dot( Vector3 lhs, Vector3 rhs )
    {
        return lhs.m_x * rhs.m_x + lhs.m_y * rhs.m_y +
               lhs.m_z * rhs.m_z;
    }

    public static Vector3 cross( Vector3 lhs, Vector3 rhs )
    {
        float lx = lhs.m_x, ly = lhs.m_y, lz = lhs.m_z;
        float rx = rhs.m_x, ry = rhs.m_y, rz = rhs.m_z;

        return new Vector3( ly * rz - lz * ry,
                            lz * rx - lx * rz,
                            lx * ry - ly * rx );
    }

    public static float distance( Vector3 lhs, Vector3 rhs )
    {
        float x = lhs.m_x - rhs.m_x;
        float y = lhs.m_y - rhs.m_y;
        float z = lhs.m_z - rhs.m_z;

        return (float) Math.sqrt( x * x + y * y + z * z );
    }

    private float m_x;
    private float m_y;
    private float m_z;
}
