package scott.modelviewer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;

public class ModelViewerApplication
{
    public void start()
    {
        //
        // Create a render window
        //
        try
        {
            Display.setDisplayMode( new DisplayMode( 800, 600 ) );
            Display.create();
            
            Display.setTitle( "Scott's Model Viewer" );
        }
        catch ( LWJGLException e )
        {
            System.err.println( "Failed to start: " + e.getMessage() );
            e.printStackTrace();
            
            return;
        }
        
        //
        // Main game loop
        //
        rendererInit();
        
        while (! Display.isCloseRequested() )
        {
            long time = getTime();
            
            processInput();
            renderFrame();
            
            Display.update();
            Display.sync( 60 );
        }
        
        Display.destroy();
    }
    
    /**
     * Processes keyboard and mouse input
     */
    private void processInput()
    {
        int x = Mouse.getX();
        int y = Mouse.getY();
        
        // Go through all buffered keyboard events
        while ( Keyboard.next() )
        {
            // nothing right now
        }
    }
    
    private void rendererInit()
    {
        // Set OpenGL view to projection
        GL11.glMatrixMode(  GL11.GL_PROJECTION );
        GL11.glLoadIdentity();
        
        // Setup GL render frame
        GL11.glOrtho(  0, 800, 600, 0, 1, -1 );
        
        // Tell OpenGL to switch matrix model to modelview
        GL11.glMatrixMode(  GL11.GL_MODELVIEW );
    }
    
    /**
     * Get time in milliseconds
     * @return
     */
    private long getTime()
    {
        return System.nanoTime() / 1000000;
    }
    
    /***
     * Renderer update
     */
    private void renderFrame()
    {
        // Clear front buffer
        GL11.glClear(  GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
        
        // draw a quad
        GL11.glColor3f(  0.5f, 0.5f, 1.0f );
        
        GL11.glBegin(  GL11.GL_QUADS );
            GL11.glVertex2f( 100, 100 );
            GL11.glVertex2f( 300, 100 );
            GL11.glVertex2f( 300, 300 );
            GL11.glVertex2f( 100, 300 );
        GL11.glEnd();
    }
    
    public static void main( String[] args )
    {
        ModelViewerApplication app = new ModelViewerApplication();
        app.start();
    }
}
