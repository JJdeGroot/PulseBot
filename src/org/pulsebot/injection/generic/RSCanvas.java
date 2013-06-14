package org.pulsebot.injection.generic;

import org.pulsebot.loader.Client;
import org.pulsebot.loader.utils.ClientPool;

import javax.accessibility.AccessibleRole;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.peer.CanvasPeer;

/**
 * @author SunJava
 * @author Modified by: JJ
 */
public class RSCanvas extends Canvas {

    private static final String base = "canvas";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -2284879212465893870L;

    private Client client = null;

    public RSCanvas() {
        super();
        System.out.println("[Canvas] constructor called!");
    }

    public RSCanvas(GraphicsConfiguration config) {
        this();
        setGraphicsConfiguration(config);
    }

    void setGraphicsConfiguration(GraphicsConfiguration gc) {
        synchronized(getTreeLock()) {
            CanvasPeer peer = (CanvasPeer) getPeer();
            if (peer != null) {
                gc = peer.getAppropriateGraphicsConfiguration(gc);
            }
           setGraphicsConfiguration(gc);
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

    boolean postsOldMouseEvents() {
        return true;
    }

    public void createBufferStrategy(int numBuffers) {
        super.createBufferStrategy(numBuffers);
    }

    public void createBufferStrategy(int numBuffers, BufferCapabilities caps) throws AWTException {
        super.createBufferStrategy(numBuffers, caps);
    }

    public BufferStrategy getBufferStrategy() {
        return super.getBufferStrategy();
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
