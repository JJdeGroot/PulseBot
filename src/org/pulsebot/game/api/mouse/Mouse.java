package org.pulsebot.game.api.mouse;

import org.pulsebot.injection.generic.RSClient;
import org.pulsebot.injection.input.PulseMouseListeners;


import java.awt.*;

import static org.pulsebot.utils.Utilities.random;
import static org.pulsebot.utils.Utilities.sleep;


/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 6/19/13
 * Time: 8:57 AM
 */
public class Mouse {
    private static RSClient client = null;

    public static void moveMouse(int x, int y){
        client = RSClient.getClient();
        System.out.println(MouseInfo.getPointerInfo().getLocation());
        if(client != null){
            PulseMouseListeners mouseListeners = new PulseMouseListeners(client.getCanvas(),client);
            mouseListeners.moveMouse(x,y);
        }
    }
    public static void clickMouse(int x, int y, boolean right){
            moveMouse(x,y);
        if(client != null){
            PulseMouseListeners mouseListeners = new PulseMouseListeners(client.getCanvas(),client);
            mouseListeners.pressMouse(right);
            sleep(random(10,30));
        }
    }
    public static void clickMouse(boolean right){
        client = RSClient.getClient();
        PulseMouseListeners mouseListeners = new PulseMouseListeners(client.getCanvas(),client);
        mouseListeners.pressMouse(right);
        sleep(random(10,30));
    }
    public static void clickMouse(){
        clickMouse(false);
    }
}
