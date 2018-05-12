package TPE;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ProbImagesBMP {

    private int getColor(BufferedImage imagen, int pixelX, int pixelY) {
        int rgb = imagen.getRGB(pixelX, pixelY);
        return (new Color(rgb, true)).getGreen();
    }

    public Double[] getProbabilidadPorTonoDeColor(BufferedImage imagen) {
        Double[] prob = new Double[256];
        int pixeles = imagen.getHeight() * imagen.getWidth();

        for (int i = 0; i < prob.length; i++) {
            prob[i] = 0d;
        }

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                int color = this.getColor(imagen, i, j);
                prob[color]++;
            }
        }

        for (int i = 0; i < prob.length; i++) {
            prob[i] /= pixeles;
        }

        return prob;
    }

    public int[] getFrecuencia(BufferedImage imagen) {
        int[] frecArr = new int[256];

        for (int i = 0; i < frecArr.length; i++) {
            frecArr[i] = 0;
        }

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                int color = this.getColor(imagen, i, j);
                frecArr[color]++;
            }
        }

        return frecArr;
    }

    public Double getMedia(BufferedImage imagen) {
        Double media = 0d;
        int pixeles = imagen.getHeight() * imagen.getWidth();

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                int color = this.getColor(imagen, i, j);
                media += color;
            }
        }

        return media / pixeles;
    }

    public Double getMedia(Double[] prob) {
        Double media = 0d;
        for (int i = 0; i < prob.length; i++) {
            media += i*prob[i];
        }
        return media;
    }

    public Double getCovarianza(BufferedImage imagenA, BufferedImage imagenB) {
        Double mediaA = this.getMedia(imagenA);
        Double mediaB = this.getMedia(imagenB);
        Double covarianza = 0d;
        int pixeles = imagenA.getWidth() * imagenA.getHeight();

        for (int i = 0; i < imagenA.getWidth(); i++) {
            for (int j = 0; j < imagenA.getHeight(); j++) {
                int colorA = getColor(imagenA, i, j);
                int colorB = getColor(imagenB, i, j);
                covarianza += (colorA - mediaA) * (colorB - mediaB);
            }
        }

        return covarianza / pixeles;
    }

    public Double getDesvioEstandar(BufferedImage imagen) {
        Double media = this.getMedia(imagen);
        Double desvio = 0d;
        int pixeles = imagen.getWidth() * imagen.getHeight();

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                int color = getColor(imagen, i, j);
                desvio += Math.pow(color - media, 2);
            }
        }

        return Math.sqrt(desvio / pixeles);
    }

    public Double getCoeficienteCorrelacion(BufferedImage imagenA, BufferedImage imagenB) {

        double divisor = this.getDesvioEstandar(imagenA) * this.getDesvioEstandar(imagenB);

        return this.getCovarianza(imagenA, imagenB) / divisor;
    }

}
