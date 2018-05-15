package TPE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Compresor {

    private FileOutputStream fos;
    private ObjectOutputStream oos;

    public void semiEstatico(BufferedImage imagen, String nombreDestino) {

        this.crearArchivo(nombreDestino);

        int[] frecuencias = (new ProbImagesBMP()).getFrecuencia(imagen);
        int width = imagen.getWidth();
        int height = imagen.getHeight();

        this.crearEncabezado(width, height, frecuencias);

        Huffman hm = new Huffman(frecuencias, width * height);

        this.crearCompresion(imagen, hm);

        this.cerrarArchivo();
    }

    private void crearArchivo(String nombreDestino) {

        try {
            this.fos = new FileOutputStream(nombreDestino + ".bin");
            this.oos = new ObjectOutputStream(this.fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void crearEncabezado(int width, int height, int[] frecuencias) {

        try {
            this.oos.writeInt(width);
            this.oos.writeInt(height);

            int cantSimbolos = -1;
            for (int i = 0; i < frecuencias.length; i++) {
                if (frecuencias[i] != 0) {
                    cantSimbolos++;
                }
            }
            this.oos.writeByte(cantSimbolos);

            for (int i = 0; i < frecuencias.length; i++) {
                if (frecuencias[i] != 0) {
                    this.oos.writeByte(i);
                    this.oos.writeInt(frecuencias[i]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void crearCompresion(BufferedImage imagen, Huffman hm) {

        try {
            char buffer = 0;
            int bitsCounter = 0;

            for (int i = 0; i < imagen.getWidth(); i++) {
                for (int j = 0; j < imagen.getHeight(); j++) {
                    int rgb = imagen.getRGB(i, j);
                    int simbolo = (new Color(rgb, true)).getGreen();
                    char[] codigo = hm.getCodigo(String.valueOf(simbolo)).toCharArray();
                    for (char bit : codigo) {
                        buffer = (char) (buffer << 1);
                        if (bit == '1') {
                            buffer = (char) (buffer | 1);
                        }
                        bitsCounter++;
                        if (bitsCounter == 16) {
                            this.oos.writeChar(buffer);
                            buffer = 0;
                            bitsCounter = 0;
                        }
                    }
                }
            }

            if ((bitsCounter < 16) && (bitsCounter != 0)) {
                buffer = (char) (buffer << (16-bitsCounter));
                this.oos.writeChar(buffer);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cerrarArchivo() {
        try {
            this.oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
