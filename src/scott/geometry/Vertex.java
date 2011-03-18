package scott.geometry;

import scott.math.*;

class Vertex
{
    Vertex( Vector3  position,
            Vector3  normal,
            TexCoord texcoord,
            Color    color,
            int      weights )
    {
        m_position = position;
        m_normal   = normal;
        m_texcoord = texcoord;
        m_color    = color;

        if ( weights > 0 )
        {
            m_boneWeights = new BoneWeight[ weights ];
        }
    }

    public Vector3 position()
    {
        return m_position;
    }

    public Vector3 normal()
    {
        return m_normal;
    }

    public TexCoord texcoord()
    {
        return m_texcoord;
    }

    public Color color()
    {
        return m_color;
    }

    public int weights()
    {
        return m_boneWeights.length;
    }

    public void setWeight( int offset, BoneWeight w )
    {
        m_boneWeights[offset] = w;
    }

    public BoneWeight getWeight( int offset )
    {
        return m_boneWeights[offset];
    }

    private Vector3    m_position;
    private Vector3    m_normal;
    private TexCoord   m_texcoord;
    private Color      m_color;
    private BoneWeight m_boneWeights[];
}
