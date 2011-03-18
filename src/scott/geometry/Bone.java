package scott.assettool;

public class Bone
{
    public Bone( String name, int id, Vector3 pos, Quat rot )
    {
        m_name     = name;
        m_id       = id;
        m_position = pos;
        m_rotation = rot;
    }

    public String name()
    {
        return m_name;
    }

    public int id()
    {
        return m_id;
    }

    public Vector3 position()
    {
        return m_position;
    }

    public Quat rotation()
    {
        return m_rotation;
    }

    public String toString()
    {
        return "{bone; n: '"   + m_name     + "', id: " +
               m_id + ", p: "  + m_position + ", r: "  + m_rotation +
               "}";
    }

    private String  m_name;
    private int     m_id;
    private Vector3 m_position; 
    private Quat    m_rotation;
}
