package org.pulsebot.injection.input;

import org.pulsebot.injection.generic.RSClient;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 6/18/13
 * Time: 9:28 PM
 */
public class PulseKeyListeners implements KeyListener {
    private ArrayList<KeyListener> keyListeners = null;
    private Canvas canvas = null;
    private RSClient rsClient = null;
    private Boolean InputEnabled = true;

    public PulseKeyListeners(Canvas canvas, RSClient rsClient) {
        this.construct(canvas, rsClient);
    }

    public void construct(Canvas canvas, RSClient client) {
        this.canvas = canvas;
        this.rsClient = rsClient;
        this.keyListeners = new ArrayList<>();
        this.keyListeners.addAll(Arrays.asList(canvas.getKeyListeners()));
        removeAllListeners();
    }

    public void updateRSClientCanvas(Canvas canvas, RSClient RSClient) {
        this.restoreAllListeners();
        this.construct(canvas, RSClient);
    }

    private void removeAllListeners() {
        for (KeyListener Listener : canvas.getKeyListeners()) {
            canvas.removeKeyListener(Listener);
        }
        canvas.addKeyListener(this);
    }

    private void restoreAllListeners() {
        canvas.removeKeyListener(this);

        for (KeyListener Listener : keyListeners) {
            canvas.addKeyListener(Listener);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == rsClient.getApplet()) {
            e.setSource(canvas);
            for (KeyListener Listener : keyListeners) {
                Listener.keyTyped(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : keyListeners) {
                Listener.keyTyped(e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == rsClient.getApplet()) {
            e.setSource(canvas);

            for (KeyListener Listener : keyListeners) {
                Listener.keyPressed(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : keyListeners) {
                Listener.keyPressed(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == rsClient.getApplet()) {
            e.setSource(canvas);

            for (KeyListener Listener : keyListeners) {
                Listener.keyReleased(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : keyListeners) {
                Listener.keyReleased(e);
            }
        }
    }
}
