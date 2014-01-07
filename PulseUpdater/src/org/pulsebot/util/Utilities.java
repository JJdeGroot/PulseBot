package org.pulsebot.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Utilities {

    private static final Random random = new Random();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy-hh-mm-ss");
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
            System.out.println("Error while trying to sleep: " + ex.getMessage());
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
     * Returns the current date
     */
    private static Date getDate() {
        return Calendar.getInstance().getTime();
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

    private static URLConnection createConnection(URL url) throws IOException {
        URLConnection con = url.openConnection();
        con.addRequestProperty("Protocol", "HTTP/1.1");
        con.addRequestProperty("Connection", "keep-alive");
        con.addRequestProperty("Keep-Alive", "200");
        con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
        return con;
    }

    /**
     * Loads a page from the given link
     *
     * @param link link to the page
     * @return page of the link
     */
    public static String getPage(String link) {
        try {
            URLConnection con = createConnection(new URL(link));
            byte[] buffer = new byte[con.getContentLength()];
            try (DataInputStream stream = new DataInputStream(con.getInputStream())) {
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
     * @param outputFile path to save
     */
    public static void downloadFile(String link, String outputFile) {
        downloadFile(link, new File(outputFile));
    }

    public static void downloadFile(String url, File output) {
        try {
            URLConnection con = createConnection(new URL(url));
            try (FileOutputStream fos = new FileOutputStream(output); InputStream in = con.getInputStream()) {
                byte[] buffer = new byte[1024];
                for (int i; (i = in.read(buffer)) != -1; ) {
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
    public static int getMultiplier(ClassNode cn, String owner, String fieldName) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        ListIterator<MethodNode> mnit = cn.methods.listIterator();
        while (mnit.hasNext()) {
            InsnList in = mnit.next().instructions;
            Iterator<AbstractInsnNode> anin = in.iterator();
            while (anin.hasNext()) {
                AbstractInsnNode an = anin.next();
                if (an instanceof FieldInsnNode) {
                    if (((FieldInsnNode) an).owner.equals(owner) && ((FieldInsnNode) an).name.equals(fieldName)) {
                        if (an.getNext().getOpcode() == Opcodes.LDC)
                            a.add((Integer) ((LdcInsnNode) an.getNext()).cst);
                        else if (an.getPrevious().getOpcode() == Opcodes.LDC)
                            a.add((Integer) ((LdcInsnNode) an.getPrevious()).cst);
                    }

                }
            }
        }
        if (a.size() > 0)
            return (getMostFrequentNLogN(a).getKey());
        return -1;
    }

    /*
    @Author Some chap on Stackoverflow
     */
    private static AbstractMap.SimpleEntry<Integer, Integer> getMostFrequentNLogN(ArrayList<Integer> values) {
        ArrayList<Integer> tmp = new ArrayList(values);

        Collections.sort(tmp);

        AbstractMap.SimpleEntry<Integer, Integer> max = new AbstractMap.SimpleEntry<>(0, 0);

        int current = tmp.get(0);
        int count = 0;
        for (int i = 0; i < tmp.size(); ++i) {
            if (tmp.get(i) == current) {
                count++;
            } else {
                if (count > max.getValue()) {
                    max = new AbstractMap.SimpleEntry<Integer, Integer>(current, count);
                }

                current = tmp.get(i);

                count = 1;
            }
        }

        if (count > max.getValue()) {
            max = new AbstractMap.SimpleEntry<Integer, Integer>(current, count);
        }

        return max;
    }

}
