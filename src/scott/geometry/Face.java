package scott.assettool;

public class Face
{
    Face( int a, int b, int c )
    {
        assert a >= 0;
        assert b >= 0;
        assert c >= 0;

        m_a = a;
        m_b = b;
        m_c = c;
    }

    int a()
    {
        return m_a;
    }

    int b()
    {
        return m_b;
    }

    int c()
    {
        return m_c;
    }

    int at( int index )
    {
        assert index >= 0 && index < 3;
        
        switch ( index )
        {
            case 1: return m_a;
            case 2: return m_b;
            case 3: return m_c;
        }

        return 0;
    }

    private int m_a;
    private int m_b;
    private int m_c;
}
