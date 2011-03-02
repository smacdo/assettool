package scott.assettool;

public class Quat
{
    public Quat( float qx, float qy, float qz, float qw )
    {
        m_qx = qx;
        m_qy = qy;
        m_qz = qz;
        m_qw = qw;
    }

    public Quad FromUnitQuat( float qx, float qy, float qz )
    {
        float  t = 1.0f - ( qx * qx ) - ( qy * qy ) - ( qz * qz );
        float qw = 0.0f;

        if ( t < 0.0f )
        {
            qw = 0.0f;
        }
        else
        {
            qw = -Math.squareRoot( t );
        }

        return Quat( qx, qy, qz, qw );
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
        float rx, ry, rz, rw;

        rx = (lhs.m_x * rhs.m_w) + (lhs.m_w * rhs.m_x) +
             (lhs.m_y * rhs.m_z) - (lhs.m_z * rhs.m_y);
        ry = (lhs.m_y * rhs.m_w) + (lhs.m_w * rhs.m_y) +
             (lhs.m_z * rhs.m_x) - (lhs.m_x * rhs.m_z);
        rz = (lhs.m_z * rhs.m_w) + (lhs.m_w * rhs.m_z) +
             (lhs.m_x * rhs.m_y) - (lhs.m_y * rhs.m_x);
        rw = (lhs.m_w * rhs.m_w) + (lhs.m_x * rhs.m_x) -
             (lhs.m_y * rhs.m_y) - (lhs.m_z * rhs.m_z);

        return new Quat( rx, ry, rz, rw );
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
