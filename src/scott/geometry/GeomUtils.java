package scott.assettool;

/**
 * Useful methods
 */
public class GeomUtils
{
    public static Winding calculateWinding( Vertex vA, Vertex vB, Vertex vC )
    {
        // Calculate the area of the triangle
        Vector3   a = vA.position();
        Vector3   b = vB.position();
        Vector3   c = vC.position();

        Vector3 tmp = Vector3.cross( Vector3.sub( b, a ), Vector3.sub( c, a ) );
        float area  = tmp.length() * 0.5f;

        if ( area > 0.0f )
        {
            return Winding.CounterClockwise;
        }
        else
        {
            return Winding.Clockwise;
        }
    }
}
