package scott.assettool;

import java.io.FileReader;
import java.io.IOException;

public interface IFileParser
{
    public void read( FileReader input ) throws IOException, ImportException;
}
