package scott.assettool;

/**
 *
 * @author scott
 */
public class ImportException extends Exception
{
    public ImportException( String message )
    {
        super( message );
    }

    private String message;
}
