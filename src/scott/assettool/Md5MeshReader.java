package scott.assettool;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Md5MeshReader implements IFileParser
{
    private enum ReaderState
    {
        Base,
        Joints,
        Mesh
    }
    
    /**
     * Reads and parses an MD5mesh file
     * 
     * @param input The MD5mesh file
     * @throws IOException     Exception when failing to read file
     * @throws ImportException Exception when failing to parse file
     */
    public void read( FileReader input ) throws IOException, ImportException
	{
		Scanner scanner   = new Scanner( input );
		ReaderState state = ReaderState.Base;
		
		//
		// Read each line of the md5 mesh
		//
		while ( scanner.hasNextLine() )
		{
		    switch ( state )
		    {
		        case Base:
		            state = processBaseLine( scanner );
		            break;
		            
		        case Joints:
		            state = processJointLine( scanner );
		            break;
		            
		        case Mesh:
		            state = processMeshLine( scanner );
		            break;
		    }
		}
	}
    
    /**
     * Processes a single line of input from an MD5mesh file
     * @param scanner The scanner reading the file
     */
    public ReaderState processBaseLine( Scanner scanner )
        throws IOException, ImportException
    {
        String command = scanner.next();
        
        //
        // MD5Version
        //
        if ( command.equals( "MD5Version" ) )
        {
            int version = scanner.nextInt();
          
            // Only supports version 10
            if ( version != 10 )
            {
                throw new ImportException("MD5 version not supported: " + version);
            }
        }
        else
        {
            throw new ImportException("Unsupported " + command );
        }
        
        return ReaderState.Base;
    }
    
    public ReaderState processJointLine( Scanner scanner )
    {
        return ReaderState.Joints;
    }
    
    public ReaderState processMeshLine( Scanner scanner )
    {
        return ReaderState.Mesh;
    }
}
