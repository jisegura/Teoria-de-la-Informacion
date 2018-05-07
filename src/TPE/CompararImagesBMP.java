package TPE;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CompararImagesBMP {

    public Double[] getProbabilidad(BufferedImage imagen) {
        Double[] prob = new Double[256];
        int pixeles = imagen.getHeight() * imagen.getWidth();

        for (int i = 0; i < prob.length; i++) {
            prob[i] = 0d;
        }

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                int rgb = imagen.getRGB(i, j);
                int color = (new Color(rgb, true)).getGreen();
                prob[color]++;
            }
        }

        for (int i = 0; i < prob.length; i++) {
            prob[i] /= pixeles;
        }

        return prob;
    }

    public Double getMedia(Double[] prob) {
        Double media = 0d;
        for (int i = 0; i < prob.length; i++) {
            media += i*prob[i];
        }
        return media;
    }

    private Double getCovarianza(BufferedImage imagenA, BufferedImage imagenB) {
        Double[] probA = this.getProbabilidad(imagenA);
        Double[] probB = this.getProbabilidad(imagenB);
        Double mediaA = this.getMedia(probA);
        Double mediaB = this.getMedia(probB);
        Double covarianza = 0d;
        int pixeles = imagenA.getWidth() * imagenA.getHeight();

        for (int i = 0; i < imagenA.getWidth(); i++) {
            for (int j = 0; j < imagenA.getHeight(); j++) {
                int rgbA = imagenA.getRGB(i, j);
                int rgbB = imagenB.getRGB(i, j);
                int colorA = (new Color(rgbA, true)).getGreen();
                int colorB = (new Color(rgbB, true)).getGreen();
                covarianza += (colorA - mediaA) * (colorB - mediaB);
            }
        }

        return covarianza / pixeles;
    }

    public Double getDesvioEstandar(BufferedImage imagen) {
        Double[] prob = this.getProbabilidad(imagen);
        Double media = this.getMedia(prob);
        Double desvio = 0d;
        int pixeles = imagen.getWidth() * imagen.getHeight();

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                int rgb = imagen.getRGB(i, j);
                int color = (new Color(rgb, true)).getGreen();
                desvio += Math.pow(color - media, 2);
            }
        }

        return Math.sqrt(desvio / pixeles);
    }

    public Double getCoeficienteCorrelacion(BufferedImage imagenA, BufferedImage imagenB) {

        double divisor = this.getDesvioEstandar(imagenA) * this.getDesvioEstandar(imagenB);

        if (imagenA.equals(imagenB)) {
            System.out.println(this.getCovarianza(imagenA, imagenB));
        }

        return this.getCovarianza(imagenA, imagenB) / divisor;
    }

}
