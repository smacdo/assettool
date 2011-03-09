package scott.assettool;

/**
 * Useful methods
 */
public class GeomUtils
{
    public static Winding calculateWinding( Vertex a, Vertex b, Vertex c )
    {
        // Calculate the area of the triangle
        float area = 0.5f * Vector3.Cross( Vector3.Sub( b, a ),
                                           Vector3.Sub( c, a ) );

        if ( area > 0.0f )
        {
            return Winding.CounterClockWise;
        }
        else
        {
            return Winding.Clockwise;
        }
    }
}
