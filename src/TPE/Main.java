package TPE;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Main {

    static final String WILLORIGINAL = "Will(Original)";
    static final String WILL1 = "Will_1";
    static final String WILL2 = "Will_2";
    static final String WILL3 = "Will_3";
    static final String WILL4 = "Will_4";
    static final String WILL5 = "Will_5";
    static final String WILL6 = "Will_6";
    static final String WILL7 = "Will_7";

    public static void main(String[] args) {

        ImagesWill iw = new ImagesWill();
        ProbImagesBMP pimg = new ProbImagesBMP();

        String[] swArr = new String[]{WILL1, WILL2, WILL3, WILL4, WILL5, WILL6, WILL7};
        HashMap<Double, String> hashCorrelacion = new HashMap();
        Double factorCorrelacion;

        for (int i = 0; i < swArr.length; i++) {
            factorCorrelacion = pimg.getCoeficienteCorrelacion(iw.getBufferedImage(ImagesWill.WILLORIGINAL), iw.getBufferedImage(i+1));
            hashCorrelacion.put(factorCorrelacion, swArr[i]);
        }


        ArrayList<Double> listaCorrelacion = new ArrayList(hashCorrelacion.keySet());
        listaCorrelacion.sort(Collections.reverseOrder());
        StringBuffer salidaEj01 = new StringBuffer();
        for (Double key:listaCorrelacion) {
            salidaEj01.append(hashCorrelacion.get(key) + ": " + key + "\n");
        }

        try {
            PrintWriter write = new PrintWriter("salida_ej01.txt", "UTF-8");
            write.println("Salida del ejercio 1");
            write.println("El factor de correlación ordenado de mayor a menor\n");
            write.println(salidaEj01.toString());
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int masParecido = 0;

        switch (hashCorrelacion.get(listaCorrelacion.get(0))) {
            case WILL1: masParecido = 1;
                        break;
            case WILL2: masParecido = 2;
                        break;
            case WILL3: masParecido = 3;
                        break;
            case WILL4: masParecido = 4;
                        break;
            case WILL5: masParecido = 5;
                        break;
            case WILL6: masParecido = 6;
                        break;
            case WILL7: masParecido = 7;
                        break;
        }

        int menosParecido = 0;

        switch (hashCorrelacion.get(listaCorrelacion.get(listaCorrelacion.size()-1))) {
            case WILL1: menosParecido = 1;
                break;
            case WILL2: menosParecido = 2;
                break;
            case WILL3: menosParecido = 3;
                break;
            case WILL4: menosParecido = 4;
                break;
            case WILL5: menosParecido = 5;
                break;
            case WILL6: menosParecido = 6;
                break;
            case WILL7: menosParecido = 7;
                break;
        }

        BufferedImage biOriginal = iw.getBufferedImage(ImagesWill.WILLORIGINAL);
        BufferedImage biMasParecido = iw.getBufferedImage(masParecido);
        BufferedImage biMenosParecido = iw.getBufferedImage(menosParecido);
        Double[] probOriginal = pimg.getProbabilidadPorTonoDeColor(biOriginal);
        Double[] probMasParecido = pimg.getProbabilidadPorTonoDeColor(biMasParecido);
        Double[] probMenosParecido = pimg.getProbabilidadPorTonoDeColor(biMenosParecido);
        Double mediaOriginal = pimg.getMedia(probOriginal);
        Double mediaMasParecido = pimg.getMedia(probMasParecido);
        Double mediaMenosParecido = pimg.getMedia(probMenosParecido);
        Double desvioOriginal = pimg.getDesvioEstandar(biOriginal);
        Double desvioMasParecido = pimg.getDesvioEstandar(biMasParecido);
        Double desvioMenosParecido = pimg.getDesvioEstandar(biMenosParecido);

        Histograma histogram = new Histograma();
        histogram.addHistograma(probOriginal, WILLORIGINAL, mediaOriginal,desvioOriginal);
        histogram.addHistograma(probMasParecido, hashCorrelacion.get(listaCorrelacion.get(0)), mediaMasParecido, desvioMasParecido);
        histogram.addHistograma(probMenosParecido, hashCorrelacion.get(listaCorrelacion.get(listaCorrelacion.size()-1)),mediaMenosParecido,desvioMenosParecido);
        histogram.saveAsBMP("salida_ej02(histograma)");


        (new Compresor()).semiEstatico(iw.getBufferedImage(ImagesWill.WILL6), "salida_ej03(comprimido)");

        (new Descompresor()).semiEstatico("salida_ej03(comprimido).bin", "salida_ej03(descomprimido)");


    }

}
