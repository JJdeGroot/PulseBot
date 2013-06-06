package java.awt;

import org.pulsebot.loader.Client;
import org.pulsebot.loader.utils.ClientPool;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import java.awt.image.BufferStrategy;
import java.awt.peer.CanvasPeer;

/**
 * @author SunJava
 * @author Modified by: JJ
 */
public class Canvas1 extends Canvas {

    private static final String base = "canvas";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -2284879212465893870L;

    private Client client = null;

    public Canvas1() {
    	super();
    	System.out.println("[Canvas] constructor called!");
    }

    public Canvas1(GraphicsConfiguration config) {
        this();
        setGraphicsConfiguration(config);
    }

    @Override
    void setGraphicsConfiguration(GraphicsConfiguration gc) {
        synchronized(getTreeLock()) {
            CanvasPeer peer = (CanvasPeer) getPeer();
            if (peer != null) {
                gc = peer.getAppropriateGraphicsConfiguration(gc);
            }
            super.setGraphicsConfiguration(gc);
        }
    }
    
    @Override
    public Graphics getGraphics() {
    	if(client == null){
    		client = ClientPool.getClient(this);
    	}
    	
    	if(client != null){
    		return client.drawGraphics((Graphics2D) super.getGraphics());
    	}
    	
    	return super.getGraphics();
    }

    @Override
    public void setVisible(boolean b) {
    	// TODO: Modify
    	super.setVisible(b);
    }
    
    String constructComponentName() {
        synchronized (Canvas.class) {
            return base + nameCounter++;
        }
    }

    public void addNotify() {
        synchronized (getTreeLock()) {
            if (peer == null)
                peer = getToolkit().createCanvas(this);
            super.addNotify();
        }
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, width, height);
    }

    public void update(Graphics g) {
        g.clearRect(0, 0, width, height);
        paint(g);
    }

    boolean postsOldMouseEvents() {
        return true;
    }

    public void createBufferStrategy(int numBuffers) {
        super.createBufferStrategy(numBuffers);
    }

    public void createBufferStrategy(int numBuffers,
        BufferCapabilities caps) throws AWTException {
        super.createBufferStrategy(numBuffers, caps);
    }

    public BufferStrategy getBufferStrategy() {
        return super.getBufferStrategy();
    }

    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleAWTCanvas();
        }
        return accessibleContext;
    }

    protected class AccessibleAWTCanvas extends AccessibleAWTComponent
    {
        private static final long serialVersionUID = -6325592262103146699L;

        /**
         * Get the role of this object.
         *
         * @return an instance of AccessibleRole describing the role of the
         * object
         * @see AccessibleRole
         */
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CANVAS;
        }

    } 
}
