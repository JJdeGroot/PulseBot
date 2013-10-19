package org.pulsebot.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public final class Utilities {

    private static final Random random = new Random();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy-hh-mm-ss");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static Utilities instance = new Utilities();

    /**
     * Private constructor so the user can't create instances
     */
    private Utilities() {
    }

    /**
     * Returns the one and only instance of this class
     *
     * @return Utilities instance
     */
    public static Utilities getInstance() {
        return instance;
    }

    /**
     * Generates a random number between min and max
     *
     * @param min minimum number
     * @param max maximum number
     * @return random number between min and max
     */
    public static int random(int min, int max) {
        return random.nextInt(Math.abs(max - min)) + min;
    }

    /**
     * Sleeps for a certain amount of milliseconds
     *
     * @param ms amount of milliseconds to sleep
     */
    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            System.out.println("Error while trying to sleep!!");
            ex.printStackTrace();
        }
    }

    /**
     * Sleeps for a random amount of time between min and max
     *
     * @param min minimum number
     * @param max maximum number
     */
    public static void sleep(int min, int max) {
        sleep(random(min, max));
    }


    /**
     * Returns the current date (month-day-year-hour-minute-seconds)
     */
    public static Date getDate() {
        return Calendar.getInstance().getTime();
    }
    
    /**
     * Returns the current date (month-day-year-hour-minute-seconds)
     */
    public static String getFormattedDate() {
    	return dateFormat.format(getDate());
    }
    
    /**
     * Returns the current time (hour-minute-seconds)
     */
    public static String getFormattedTime() {
    	return timeFormat.format(getDate());
    }

    /**
     * Saves an image in the screenshots folder
     *
     * @param img the image to save
     */
    public static void saveImage(BufferedImage img) {
        try {
            File directory = new File("Screenshots/");
            if (!directory.exists()) {
                directory.mkdir();
            }

            if (directory.exists()) {
                File path = new File("Screenshots/" + dateFormat.format(getDate()) + ".png");
                ImageIO.write(img, "png", path);
            }
        } catch (Exception ex) {
            System.out.println("Error while trying to save an image: " + ex.getMessage());
        }
    }

    /**
     * Loads a page from the given link
     *
     * @param link link to the page
     * @return page of the link
     */
    public static String getPage(String link) {
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("Protocol", "HTTP/1.1");
            conn.addRequestProperty("Connection", "keep-alive");
            conn.addRequestProperty("Keep-Alive", "200");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
            byte[] buffer = new byte[conn.getContentLength()];
            try (DataInputStream stream = new DataInputStream(conn.getInputStream())) {
                stream.readFully(buffer);
            }
            return new String(buffer);
        } catch (Exception ex) {
            System.out.println("Error while trying to get a page: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Attempts to download a file from a certain link
     *
     * @param link link to the file
     * @param path path to save
     */
    public static void downloadFile(String link, String path) {
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            try (FileOutputStream fos = new FileOutputStream(path); InputStream in = conn.getInputStream()) {
                byte[] buffer = new byte[1024];
                for (int i = 0; (i = in.read(buffer)) != -1; ) {
                    fos.write(buffer, 0, i);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error while downloading file: " + ex.getMessage());
        }
    }

    /**
     * Scales the given image to the input width/height
     *
     * @param width  new width
     * @param height new height
     * @param img    img to resize
     * @return scaled image
     */
    public static Image scaleImage(int width, int height, Image img) {
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * Scales the given image icon to the input width/height
     *
     * @param width  new width
     * @param height new height
     * @param img    image icon to resize
     * @return scaled image icon
     */
    public static ImageIcon scaleIcon(int width, int height, ImageIcon img) {
        return new ImageIcon(scaleImage(width, height, img.getImage()));
    }

    /**
     * Loads an image from our resources
     *
     * @param resourcePath path to the image
     * @return image from the resource path
     */
    public Image loadResourceImage(String resourcePath) {
        try {
            return ImageIO.read(getClass().getResource(resourcePath));
        } catch (Exception ex) {
            System.out.println("Error while loading image from resources: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Creates a gray scaled variant of the given image
     *
     * @param img     image to grayscale
     * @param percent grayscale percentage
     * @return grayscaled image
     */
    public static Image grayScaleImage(Image img, int percent) {
        ImageProducer producer = new FilteredImageSource(img.getSource(), new GrayFilter(true, percent));
        return Toolkit.getDefaultToolkit().createImage(producer);
    }


}
