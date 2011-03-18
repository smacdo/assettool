package scott.assettool;

public class Vector2
{
    public Vector2( float x_, float y_ )
    {
        m_x = x_;
        m_y = y_;
    }

    public Vector2( Vector2 v )
    {
        m_x = v.m_x;
        m_y = v.m_y;
    }

    public float x()
    {
        return m_x;
    }

    public float y()
    {
        return m_y;
    }

    public void set( float x_, float y_ )
    {
        m_x = x_;
        m_y = y_;
    }

    public void setX( float x_ )
    {
        m_x = x_;
    }

    public void setY( float y_ )
    {
        m_y = y_;
    }

    public String toString()
    {
        return "<vec2; " + m_x + ", " + m_y + ">";
    }

    private float m_x;
    private float m_y;
}
