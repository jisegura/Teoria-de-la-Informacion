package TPE;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class Descompresor {

    private FileInputStream fis;
    private ObjectInputStream ois;

    public void semiEstatico(String nombreOrigen, String nombreDestino) {

        this.leerArchivo(nombreOrigen);

        int width = this.leerWidth();
        int height = this.leerHeight();
        int[] frecuencias = this.leerFrecuencias();

        this.crearDescompresion(width, height, frecuencias, nombreDestino);

        this.cerrarArchivo();
    }

    private void leerArchivo(String nombreOrigen) {

        try {
            this.fis = new FileInputStream(nombreOrigen);
            this.ois = new ObjectInputStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int leerWidth() {
        int w = 0;
        try {
            w = this.ois.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return w;
    }

    private int leerHeight() {
        int h = 0;
        try {
            h = this.ois.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return h;
    }

    private int[] leerFrecuencias() {
        int[] f = new int[256];
        for (int i = 0; i < f.length; i++) {
            f[i] = 0;
        }
        try {
            int cantSimbolos = (this.ois.readByte() & 0xFF) + 1;

            for (int i = 0; i < cantSimbolos; i++) {
                int pos = (this.ois.readByte() & 0xFF);
                f[pos] = this.ois.readInt();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;

    }


    private void crearDescompresion(int width, int height, int[] frecuencias, String nombreDestino) {

        byte[] r = {0,17,34,51,68,85,102,119,(byte) 136,(byte) 153,(byte) 170,(byte) 187,(byte) 204,(byte) 221,(byte) 238,(byte) 255};
        byte[] g = {0,17,34,51,68,85,102,119,(byte) 136,(byte) 153,(byte) 170,(byte) 187,(byte) 204,(byte) 221,(byte) 238,(byte) 255};
        byte[] b = {0,17,34,51,68,85,102,119,(byte) 136,(byte) 153,(byte) 170,(byte) 187,(byte) 204,(byte) 221,(byte) 238,(byte) 255};
        IndexColorModel icm = new IndexColorModel(4, 16, r, g, b);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY, icm);
        //BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Huffman hm = new Huffman(frecuencias, width * height);

        int cantidadBits = 0;
        char buffer = 0;
        int mask = 32768;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boolean coloreado = false;

                while (!coloreado) {
                    if (hm.nodoEsHoja()) {
                        int tono = hm.nodoGetTono();
                        Color color = new Color(tono, tono, tono);
                        bi.setRGB(i, j, color.getRGB());
                        coloreado = true;
                    } else {
                        if (cantidadBits == 0) {
                            mask = 32768;
                            cantidadBits = 16;
                            try {
                                buffer = this.ois.readChar();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if ((buffer & mask) == mask) {
                            hm.nodoSuperior();
                        } else {
                            hm.nodoInferior();
                        }
                        cantidadBits--;
                        mask = mask >> 1;
                    }
                }
            }
        }

        try {
            ImageIO.write(bi, "bmp", new File(nombreDestino + ".bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cerrarArchivo() {
        try {
            this.ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
