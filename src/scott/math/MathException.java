package scott.math;

/**
 * Represents a fatal error that happened while performing a mathmatical
 * calculation
 *
 * @author scott
 */
public class MathException extends Exception
{
    public MathException( String message )
    {
        super( message );
    }

}
