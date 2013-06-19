package org.pulsebot.injection.input;

import org.pulsebot.loader.Client;

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
    private ArrayList<KeyListener> KeyListeners = null;
    private Canvas canvas = null;
    private Client client = null;
    private Boolean InputEnabled = true;

    public PulseKeyListeners(Canvas canvas, Client client) {
        this.construct(canvas, client);
    }

    public void construct(Canvas canvas, Client client) {
        this.canvas = canvas;
        this.client = client;
        this.KeyListeners = new ArrayList<>();
        this.KeyListeners.addAll(Arrays.asList(canvas.getKeyListeners()));
        removeAllListeners();
    }

    public void updateclientCanvas(Canvas canvas, Client client) {
        this.restoreAllListeners();
        this.construct(canvas, client);
    }

    private void removeAllListeners() {
        for (KeyListener Listener : canvas.getKeyListeners()) {
            canvas.removeKeyListener(Listener);
        }
        canvas.addKeyListener(this);
    }

    private void restoreAllListeners() {
        canvas.removeKeyListener(this);

        for (KeyListener Listener : KeyListeners) {
            canvas.addKeyListener(Listener);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);
            for (KeyListener Listener : KeyListeners) {
                Listener.keyTyped(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : KeyListeners) {
                Listener.keyTyped(e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (KeyListener Listener : KeyListeners) {
                Listener.keyPressed(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : KeyListeners) {
                Listener.keyPressed(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == client.getApplet()) {
            e.setSource(canvas);

            for (KeyListener Listener : KeyListeners) {
                Listener.keyReleased(e);
            }
        } else if (InputEnabled) {
            for (KeyListener Listener : KeyListeners) {
                Listener.keyReleased(e);
            }
        }
    }
}
