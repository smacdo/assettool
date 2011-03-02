package scott.assettool;

public class TexCoord
{
    public TexCoord( float u, float v )
    {
        m_u = u;
        m_v = v;
    }

    public float u()
    {
        return m_u;
    }

    public float v()
    {
        return m_v;
    }

    public String toString()
    {
        return "<texcoord: " + m_u + ", " + m_v + ">";
    }

    private float m_u;
    private float m_v;
}
