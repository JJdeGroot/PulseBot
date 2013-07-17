package org.pulsebot.injection.input;



import org.pulsebot.injection.generic.RSClient;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 6/12/13
 * Time: 1:58 PM
 */
public class PulseMouseListeners implements MouseListener, MouseWheelListener, MouseMotionListener {
    private ArrayList<MouseListener> MouseListeners = null;
    private ArrayList<MouseMotionListener> MouseMotionListeners = null;
    private ArrayList<MouseWheelListener> MouseWheelListeners = null;
    private Canvas canvas = null;
    private RSClient client = null;
    private Point Position = new Point(-1, -1);
    private Boolean InputEnabled = true;
    private static Point sPosition;

    public PulseMouseListeners(Canvas canvas, RSClient client) {
        this.construct(canvas, client);
    }

    public void construct(Canvas canvas, RSClient client) {
        this.canvas = canvas;
        this.client = client;
        this.MouseListeners = new ArrayList<>();
        this.MouseMotionListeners = new ArrayList<>();
        this.MouseWheelListeners = new ArrayList<>();
        this.MouseListeners.addAll(Arrays.asList(canvas.getMouseListeners()));
        this.MouseMotionListeners.addAll(Arrays.asList(canvas.getMouseMotionListeners()));
        this.MouseWheelListeners.addAll(Arrays.asList(canvas.getMouseWheelListeners()));
        removeAllListeners();
    }

    public void updateclientCanvas(Canvas canvas, RSClient client) {
        this.restoreAllListeners();
        this.construct(canvas, client);
    }

    private void removeAllListeners() {
        for (MouseListener Listener : canvas.getMouseListeners()) {
            canvas.removeMouseListener(Listener);
        }

        for (MouseMotionListener Listener : canvas.getMouseMotionListeners()) {
            canvas.removeMouseMotionListener(Listener);
        }

        for (MouseWheelListener Listener : canvas.getMouseWheelListeners()) {
            canvas.removeMouseWheelListener(Listener);
        }

        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);
    }

    private void restoreAllListeners() {
        canvas.removeMouseListener(this);
        canvas.removeMouseMotionListener(this);
        canvas.removeMouseWheelListener(this);

        for (MouseListener Listener : MouseListeners) {
            canvas.addMouseListener(Listener);
        }

        for (MouseMotionListener Listener : MouseMotionListeners) {
            canvas.addMouseMotionListener(Listener);
        }

        for (MouseWheelListener Listener : MouseWheelListeners) {
            canvas.addMouseWheelListener(Listener);
        }
    }
    public static Point getPosition(){

        return sPosition;
    }
    public void moveMouse(final int x, final int y) {
        final MouseEvent event = new MouseEvent(client.getApplet(), MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0, x, y, 0, false);
        this.mouseMoved(event);
        Position.setLocation(x, y);
        sPosition = Position;
    }
    public void pressMouse(boolean right) {
        MouseEvent pressed = new MouseEvent(client.getApplet(), MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), right ? InputEvent.BUTTON3_MASK : 0,
                (int) Position.getX(), (int) Position.getY(), 1, false);

        this.mousePressed(pressed);
    }



    public void releaseMouse() {
        MouseEvent released = new MouseEvent(client.getApplet(), MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, (int) Position.getX(), (int) Position.getY(), 1,
                false);
        this.mouseReleased(released);
    }


    public synchronized Point windMouse(int x, int y, final double speedFactor) {
        Random rand = new Random();
        double speed = (rand.nextDouble() * 15D + 15D) / 10D;
        speed *= speedFactor;
        return windMouseImpl(Position.x, Position.y, x, y, 9D, 3D, 5D / speed, 10D / speed,
                10D * speed, 8D * speed);
    }

    /**
     * Internal mouse movement algorithm. Do not use this without credit to either Benjamin J. Land
     * or BenLand100. This is synchronized to prevent multiple motions and bannage.
     *
     * @param xs         The x start
     * @param ys         The y start
     * @param xe         The x destination
     * @param ye         The y destination
     * @param gravity    Strength pulling the position towards the destination
     * @param wind       Strength pulling the position in random directions
     * @param minWait    Minimum relative time per step
     * @param maxWait    Maximum relative time per step
     * @param maxStep    Maximum size of a step, prevents out of control motion
     * @param targetArea Radius of area around the destination that should trigger slowing, prevents
     *                   spiraling
     * @result The actual end point
     */


    private synchronized Point windMouseImpl(double xs, double ys, double xe, double ye,
                                             double gravity, double wind, double minWait, double maxWait, double maxStep,
                                             double targetArea) {

        final double sqrta = Math.sqrt(5);
        final double sqrtb = Math.sqrt(7);
        double dist, veloX = 0, veloY = 0, windX = 0, windY = 0;
        while ((dist = Math.hypot(xe - xs, ys - ye)) >= 1) {
            wind = Math.min(wind, dist);
            if (dist >= targetArea) {
                windX = windX / sqrta + (Math.random() * (wind * 2D + 1D) - wind) / sqrta;
                windY = windY / sqrtb + (Math.random() * (wind * 2D + 1D) - wind) / sqrtb;
            } else {
                windX /= sqrtb;
                windY /= sqrta;
                if (maxStep < 4) {
                    maxStep = Math.random() * 3 + 3D;
                } else {
                    maxStep /= sqrta;
                }

            }
            veloX += windX + gravity * (xe - xs) / dist;
            veloY += windY + gravity * (ye - ys) / dist;
            double veloMag = Math.hypot(veloX, veloY);
            if (veloMag > maxStep) {
                double randomDist = maxStep / 2D + Math.random() * maxStep / 2D;
                veloX = (veloX / veloMag) * randomDist;
                veloY = (veloY / veloMag) * randomDist;
            }
            xs += veloX;
            ys += veloY;
            int mx = (int) Math.round(xs);
            int my = (int) Math.round(ys);
            if (Position.x != mx || Position.y != my) {

                moveMouse(mx, my);
            }
            double step = Math.hypot(xs - Position.x, ys - Position.y);
            try {
                Thread.sleep(Math.round((maxWait - minWait) * (step / maxStep) + minWait));
            } catch (InterruptedException ex) {
            }
        }
        return new Point(Position.x, Position.y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseClicked(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseClicked(e);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mousePressed(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseReleased(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseEntered(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseEntered(e);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (MouseListener Listener : MouseListeners) {
                Listener.mouseExited(e);
            }
        } else if (InputEnabled) {
            for (MouseListener Listener : MouseListeners) {
                Listener.mouseExited(e);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseDragged(e);
            }
        } else if (InputEnabled) {
            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseDragged(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getSource() == client.getApplet() || InputEnabled) {
            e.setSource(canvas);

            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseMoved(e);
            }
            Position = new Point(e.getX(), e.getY());
            sPosition = Position;
        } else if (InputEnabled) {
            for (MouseMotionListener Listener : MouseMotionListeners) {
                Listener.mouseMoved(e);
            }
            Position = new Point(e.getX(), e.getY());
            sPosition = Position;
        }

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (MouseWheelListener Listener : MouseWheelListeners) {
                Listener.mouseWheelMoved(e);
            }
        } else if (InputEnabled) {
            for (MouseWheelListener Listener : MouseWheelListeners) {
                Listener.mouseWheelMoved(e);
            }
        }
    }
}
