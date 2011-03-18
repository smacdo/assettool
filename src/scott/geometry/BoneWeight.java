package scott.geometry;

public class BoneWeight
{
    public BoneWeight( Bone bone, float weight )
    {
        m_bone   = bone;
        m_weight = weight;
    }

    public Bone bone()
    {
        return m_bone;
    }

    public int boneid()
    {
        return m_bone.id();
    }

    public String name()
    {
        return m_bone.name();
    }

    public float weight()
    {
        return m_weight;
    }

    public String toString()
    {
        return "{bweight; i: " + m_bone.id() + ", w: " + m_weight +
               "}";
    }

    private Bone  m_bone;
    private float m_weight;
}
