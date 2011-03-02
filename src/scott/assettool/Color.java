package scott.assettool;

public class Color
{
    public Color( float r, float g, float b, float a )
    {
        m_r = r;
        m_g = g;
        m_b = b;
        m_a = a;
    }

    public Color( float r, float g, float b )
    {
        m_r = r;
        m_g = g;
        m_b = b;
        m_a = 1.0f;
    }

    public float r()
    {
        return m_r;
    }

    public float g()
    {
        return m_g;
    }

    public float b()
    {
        return m_b;
    }

    public float a()
    {
        return m_a;
    }

    private float m_r;
    private float m_g;
    private float m_b;
    private float m_a;
}
