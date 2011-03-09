package scott.assettool;

import java.io.*;

public class AssetToolCommandline
{
    public static void main( String[] args )
    {
        System.out.println( "Hello world" );

        String filename = "assets/obj/teapot.obj";

        try
        {
            File inputFile    = new File( filename );
            FileReader reader = new FileReader( inputFile );

            ObjReader objreader = new ObjReader();
            objreader.read( reader );
        }
        catch ( FileNotFoundException e )
        {
            System.err.println( "Failed to open: " + filename );
        }
        catch ( IOException e )
        {
            System.err.println( "Failed while reading: " + filename );
            System.err.println( e.getMessage() );
            e.printStackTrace();
        }
        catch ( ImportException e )
        {
            System.err.println( "Failed to load: " + filename );
            System.err.println( e.getMessage() );
            e.printStackTrace();
        }
        
    }
}
