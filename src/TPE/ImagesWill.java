package TPE;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ImagesWill {

    public static final int WILLORIGINAL = 0;
    public static final int WILL1 = 1;
    public static final int WILL2 = 2;
    public static final int WILL3 = 3;
    public static final int WILL4 = 4;
    public static final int WILL5 = 5;
    public static final int WILL6 = 6;
    public static final int WILL7 = 7;
    public static final int WILLCANAL2 = 8;
    public static final int WILLCANAL8 = 9;
    public static final int WILLCANAL10 = 10;

    private HashMap<Integer, URL> hashWill;

    public ImagesWill() {
        this.hashWill = new HashMap<>();
        this.hashWill.put(WILLORIGINAL, this.getClass().getResource("/images/Will/Will(Original).bmp"));
        this.hashWill.put(WILL1, this.getClass().getResource("/images/Will/Will_1.bmp"));
        this.hashWill.put(WILL2, this.getClass().getResource("/images/Will/Will_2.bmp"));
        this.hashWill.put(WILL3, this.getClass().getResource("/images/Will/Will_3.bmp"));
        this.hashWill.put(WILL4, this.getClass().getResource("/images/Will/Will_4.bmp"));
        this.hashWill.put(WILL5, this.getClass().getResource("/images/Will/Will_5.bmp"));
        this.hashWill.put(WILL6, this.getClass().getResource("/images/Will/Will_6.bmp"));
        this.hashWill.put(WILL7, this.getClass().getResource("/images/Will/Will_7.bmp"));
        this.hashWill.put(WILLCANAL2, this.getClass().getResource("/images/Will/Will_Canal2.bmp"));
        this.hashWill.put(WILLCANAL8, this.getClass().getResource("/images/Will/Will_Canal8.bmp"));
        this.hashWill.put(WILLCANAL10, this.getClass().getResource("/images/Will/Will_Canal10.bmp"));
    }

    public BufferedImage getBufferedImage(int nombreImagen) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(this.hashWill.get(nombreImagen));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }

    public long getSizeImage(int nombreImagen) {
        long size = 0;
        try {
            size = this.hashWill.get(nombreImagen).openStream().available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

}
