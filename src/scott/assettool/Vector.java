package scott.assettool;

/**
 * Base vector class. Contains utility methods for dealing with vectors
 */
public class Vector
{
    public static Vector3 add( Vector3 lhs, Vector3 rhs )
    {
        return new Vector3( lhs.x + rhs.x,
                            lhs.y + rhs.y,
                            lhs.z + rhs.z );
    }

    public static Vector3 sub( Vector3 lhs, Vector3 rhs )
    {
        return new Vector3( lhs.x - rhs.x,
                            lhs.y - rhs.y,
                            lhs.z - rhs.z );
    }

    public static Vector3 scale( Vector3 v, float s )
    {
        return new Vector3( v.x * s, v.y * s, v.z * s );
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

        return new Vector( ly * rz - lz * ry,
                           lz * rx - lx * rz,
                           lx * ry - ky * rx );
    }

    public static float distance( Vector3 lhs, Vector3 rhs )
    {
        float x = lhs.m_x - rhs.m_x;
        float y = lhs.m_y - rhs.m_y;
        float z = lhs.m_z - rhs.m_z;

        return Math.sqrt( x * x + y * y + z * z );
    }
}
