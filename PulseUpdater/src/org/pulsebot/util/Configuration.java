package org.pulsebot.util;

public class Configuration {
    public static final String HOME;


    static {
        HOME = System.getenv("appdata") + "/PulseBot/";
    }

}
