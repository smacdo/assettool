package scott.assettool;

import java.io.*;
import java.util.Scanner;

public class ObjReader
{
    public void import( FileReader input ) throws IOException
    {
        Scanner scanner = new Scanner( input );

        while ( scanner.hasNextLine() )
        {
            processLine( scanner );
        }
    }

    private boolean processLine( Scanner scanner )
    {
        String cmd = scanner.next();

        switch ( cmd )
        {
            case "v":  processVertexCmd( scanner ); break;
            case "vt": processVTexCmd( scanner );   break;
            case "vn": processVNormCmd( scanner );  break;
            case "f":  processFaceCmd( scanner );   break;
            case "g":  processGroupCmd( scanner );  break;
            case "mtllib": processMlibCmd( scanner);break;
            case "usemtl": processMatCmd( scanner); break;
        }
    }

    private void processVertexCmd( Scanner scanner )
    {
        float x = scanner.nextFloat();
        float y = scanner.nextFloat();
        float z = scanner.nextFloat();

    }

    private void processVTexCmd( Scanner scanner )
    {
        float u = scanner.nextFloat();
        float v = scanner.nextFloat();

    }

    private void processVNormCmd( Scanner scanner )
    {
        float x = scanner.nextFloat();
        float y = scanner.nextFloat();
        float z = scanner.nextFloat();
    }

    private void processFaceCmd( Scanner scanner )
    {
        scanner.findInLine(
            "(\\d+)/(\\d+)?/(\\d+)? (\\d+)/(\\d+)?/(\\d+)? (\\d+)/(\\d+)?/(\\d+)?"
        );
    }

    private void processGroupCmd( Scanner scanner )
    {
        String groupName = scanner.next();
    }

    private void processMLibCmd( Scanner scanner )
    {
        String filename = scanner.next();
    }

    private void processMatCmd( Scanner scanner )
    {
        String materialName = scanner.next();
    }
}
