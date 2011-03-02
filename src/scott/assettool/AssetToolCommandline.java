package scott.assettool;

public class AssetToolCommandline
{
    public static void main( String[] args )
    {
        System.out.println( "Hello world" );

        String filename = "assets/sample.obj";

        File inputFile    = new File( filename );
        FileReader reader = new FileReader( inputFile );
    }
}
