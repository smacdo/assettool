package scott.math;

public class Quat
{
    /**
     * Quaternion constructor
     * @param qx
     * @param qy
     * @param qz
     * @param qw
     */
    public Quat( float qx, float qy, float qz, float qw )
    {
        m_qx = qx;
        m_qy = qy;
        m_qz = qz;
        m_qw = qw;
    }

    /**
     * Construct a quaternion from the q(x,y,z) portion of a unit quaternion
     * 
     * @param qx
     * @param qy
     * @param qz
     * @return
     */
    public Quat FromUnitQuat( float qx, float qy, float qz )
    {
        float  t = 1.0f - ( qx * qx ) - ( qy * qy ) - ( qz * qz );
        float qw = 0.0f;

        if ( t < 0.0f )
        {
            qw = 0.0f;
        }
        else
        {
            qw = (float) -Math.sqrt( t );
        }

        return new Quat( qx, qy, qz, qw );
    }

    public float qx()
    {
        return m_qx;
    }

    public float qy()
    {
        return m_qy;
    }

    public float qz()
    {
        return m_qz;
    }

    public float qw()
    {
        return m_qw;
    }

    public static Quat mul( Quat lhs, Quat rhs )
    {
        float qx, qy, qz, qw;
        float lx = lhs.m_qx, ly = lhs.m_qy, lz = lhs.m_qz, lw = lhs.m_qw;
        float rx = rhs.m_qx, ry = rhs.m_qy, rz = rhs.m_qz, rw = rhs.m_qw;

        qx = (lx * rw) + (lw * rx) + (ly * rz) - (lz * ry);
        qy = (ly * rw) + (lw * ry) + (lz * rx) - (lx * rz);
        qz = (lz * rw) + (lw * rz) + (lx * ry) - (ly * rx);
        qw = (lw * rw) + (lx * rx) - (ly * ry) - (lz * rz);

        return new Quat( qx, qy, qz, qw );
    }

    public String toString()
    {
        return "<quat; qx: " + m_qx +
                    ", qy: " + m_qy +
                    ", qz: " + m_qz +
               ">";
    }

    private float m_qx;
    private float m_qy;
    private float m_qz;
    private float m_qw;
}
