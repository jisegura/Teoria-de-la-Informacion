package TPE;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    static final String DIRI = "Ejercicios Parte 1";
    static final String DIRII = "Ejercicios Parte 2";
    static final String DIRFINAL = "Ejercicios Parte Final";

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

        new File(DIRI).mkdirs();

        try {
            PrintWriter write = new PrintWriter(DIRI+"/salida_ej01.txt", "UTF-8");
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
        histogram.saveAsBMP(DIRI+"/salida_ej02(histograma)");

        long startTiempoCompresion = System.nanoTime();
        (new Compresor()).semiEstatico(iw.getBufferedImage(masParecido), DIRI+"/salida_ej03_Will"+masParecido+"(comprimido)");
        long endTiempoCompresion = System.nanoTime();

        long startTiempoDescompresion = System.nanoTime();
        (new Descompresor()).semiEstatico(DIRI+"/salida_ej03_Will"+masParecido+"(comprimido).bin", DIRI+"/salida_ej03_Will"+masParecido+"(descomprimido)");
        long endTiempoDescompresion = System.nanoTime();


        int[] frecuencias = pimg.getFrecuencia(biMasParecido);
        int cantPixeles = biMasParecido.getWidth() * biMasParecido.getHeight();
        StringBuffer sBuffer = new StringBuffer();

        Huffman hm = new Huffman(frecuencias, cantPixeles);

        for (int i = 0; i < frecuencias.length; i++) {
            if (frecuencias[i] != 0) {
                sBuffer.append(Arrays.toString(hm.getCodigo(String.valueOf(i)).toCharArray()) + ": " + String.valueOf(i) + "\n");
            }
        }

        NumberFormat formatter = new DecimalFormat("#0.00000");
        String tiempoProcesamientoCompresion = formatter.format((endTiempoCompresion - startTiempoCompresion) / 1000000d) + " milisegundos";
        String tiempoProcesamientoDescompresion = formatter.format((endTiempoDescompresion - startTiempoDescompresion) / 1000000d) + " milisegundos\n";

        try {
            long pesoComprimido = new File(DIRI+"/salida_ej03_Will"+masParecido+"(comprimido).bin").length();
            long pesoOriginal = iw.getSizeImage(masParecido);
            formatter = new DecimalFormat("#0.00");
            String tasa = formatter.format((double) pesoOriginal / pesoComprimido);
            PrintWriter write = new PrintWriter(DIRI+"/salida_ej04.txt", "UTF-8");
            write.println("Salida del ejercio 4\n");
            write.println("Codificación Huffman para el niño mas parecido (Will"+masParecido+"):");
            write.println(sBuffer.toString());
            write.println("El tiempo de procesamiento de la compresion es de " + tiempoProcesamientoCompresion);
            write.println("El tiempo de procesamiento de la descompresion es de " + tiempoProcesamientoDescompresion);
            write.println("Tasa de compresión es de " + tasa + ":1");
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        new File(DIRII).mkdirs();

        Canal canal2 = new Canal(iw.getBufferedImage(ImagesWill.WILLORIGINAL), iw.getBufferedImage(ImagesWill.WILLCANAL2));
        Canal canal8 = new Canal(iw.getBufferedImage(ImagesWill.WILLORIGINAL), iw.getBufferedImage(ImagesWill.WILLCANAL8));
        Canal canal10 = new Canal(iw.getBufferedImage(ImagesWill.WILLORIGINAL), iw.getBufferedImage(ImagesWill.WILLCANAL10));
        double[][] matrizTransicionCanal2 = canal2.getMatrizdeTransicion();
        double[][] matrizTransicionCanal8 = canal8.getMatrizdeTransicion();
        double[][] matrizTransicionCanal10 = canal10.getMatrizdeTransicion();

        try {
            PrintWriter write = new PrintWriter(DIRII+"/salida_ej01.txt", "UTF-8");
            write.println("Salida del ejercio 1\n");
            write.println("Matriz de transicion del Canal2:");
            write.println("    [0, 17, 34, 51, 68, 85, 102, 119, 136, 153, 170, 187, 204, 221, 238, 255]");
            for (int i = 0; i < matrizTransicionCanal2.length; i++) {
                write.println("["+((i << 4) | i)+"] "+Arrays.toString(matrizTransicionCanal2[i]));
            }
            write.println("\nMatriz de transicion del Canal8:");
            write.println("    [0, 17, 34, 51, 68, 85, 102, 119, 136, 153, 170, 187, 204, 221, 238, 255]");
            for (int i = 0; i < matrizTransicionCanal8.length; i++) {
                write.println("["+((i << 4) | i)+"] "+Arrays.toString(matrizTransicionCanal8[i]));
            }
            write.println("\nMatriz de transicion del Canal10:");
            write.println("    [0, 17, 34, 51, 68, 85, 102, 119, 136, 153, 170, 187, 204, 221, 238, 255]");
            for (int i = 0; i < matrizTransicionCanal8.length; i++) {
                write.println("["+((i << 4) | i)+"] "+Arrays.toString(matrizTransicionCanal10[i]));
            }
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter write = new PrintWriter(DIRII+"/salida_ej02.txt", "UTF-8");
            write.println("Salida del ejercio 2\n");
            write.println("Canal2:");
            write.println("Ruido: "+ canal2.getRuido());
            write.println("Perdida: "+ canal2.getPerdida());
            write.println("Informacion Mutua: "+ canal2.getInformacionMutua());
            write.println("\nCanal8:");
            write.println("Ruido: "+ canal8.getRuido());
            write.println("Perdida: "+ canal8.getPerdida());
            write.println("Informacion Mutua: "+ canal8.getInformacionMutua());
            write.println("\nCanal10:");
            write.println("Ruido: "+ canal10.getRuido());
            write.println("Perdida: "+ canal10.getPerdida());
            write.println("Informacion Mutua: "+ canal10.getInformacionMutua());
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Canal c25, c75, c150, cEpsilon;
        double[] prob = pimg.getProbabilidadAcumulada(iw.getBufferedImage(ImagesWill.WILLORIGINAL));
        Transmision t = new Transmision(canal2);
        c25 = t.transmitir(prob, 25);
        c75 = t.transmitir(prob, 75);
        c150 = t.transmitir(prob, 150);
        cEpsilon = t.transmitir(prob);

        try {
            PrintWriter write = new PrintWriter(DIRII+"/salida_ej03.txt", "UTF-8");
            write.println("Salida del ejercio 3\n");
            write.println("Canal2 (25 datos):");
            write.println("Ruido: "+ c25.getRuido());
            write.println("Perdida: "+ c25.getPerdida());
            write.println("Informacion Mutua: "+ c25.getInformacionMutua());
            write.println("\nCanal2 (75 datos):");
            write.println("Ruido: "+ c75.getRuido());
            write.println("Perdida: "+ c75.getPerdida());
            write.println("Informacion Mutua: "+ c75.getInformacionMutua());
            write.println("\nCanal2 (150 datos):");
            write.println("Ruido: "+ c150.getRuido());
            write.println("Perdida: "+ c150.getPerdida());
            write.println("Informacion Mutua: "+ c150.getInformacionMutua());
            write.println("\nCanal2 (converge):");
            write.println("Ruido: "+ cEpsilon.getRuido());
            write.println("Perdida: "+ cEpsilon.getPerdida());
            write.println("Informacion Mutua: "+ cEpsilon.getInformacionMutua());
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        new File(DIRFINAL).mkdirs();

        BufferedImage biCanal2 = iw.getBufferedImage(ImagesWill.WILLCANAL2);
        BufferedImage biCanal8 = iw.getBufferedImage(ImagesWill.WILLCANAL8);
        BufferedImage biCanal10 = iw.getBufferedImage(ImagesWill.WILLCANAL10);
        Double[] probCanal2 = pimg.getProbabilidadPorTonoDeColor(biCanal2);
        Double[] probCanal8 = pimg.getProbabilidadPorTonoDeColor(biCanal8);
        Double[] probCanal10 = pimg.getProbabilidadPorTonoDeColor(biCanal10);
        Double mediaCanal2 = pimg.getMedia(probCanal2);
        Double mediaCanal8 = pimg.getMedia(probCanal8);
        Double mediaCanal10 = pimg.getMedia(probCanal10);
        Double desvioCanal2 = pimg.getDesvioEstandar(biCanal2);
        Double desvioCanal8 = pimg.getDesvioEstandar(biCanal8);
        Double desvioCanal10 = pimg.getDesvioEstandar(biCanal10);
        histogram = new Histograma();
        histogram.addHistograma(probCanal2, "Canal2", mediaCanal2, desvioCanal2);
        histogram.addHistograma(probCanal8, "Canal8", mediaCanal8, desvioCanal8);
        histogram.addHistograma(probCanal10, "Canal10", mediaCanal10, desvioCanal10);
        histogram.saveAsBMP(DIRFINAL+"/salida_ej01(histograma)");



        int[] frecuenciaCanal = pimg.getFrecuencia(biCanal2);
        int pixelesCanal = biCanal2.getWidth() * biCanal2.getHeight();
        StringBuffer sBufferCanal2 = new StringBuffer();
        hm = new Huffman(frecuenciaCanal, pixelesCanal);
        for (int i = 0; i < frecuenciaCanal.length; i++) {
            if (frecuenciaCanal[i] != 0) {
                sBufferCanal2.append(Arrays.toString(hm.getCodigo(String.valueOf(i)).toCharArray()) + ": " + String.valueOf(i) + "\n");
            }
        }
        sBufferCanal2.append("<L>: \n");

        frecuenciaCanal = pimg.getFrecuencia(biCanal8);
        StringBuffer sBufferCanal8 = new StringBuffer();
        hm = new Huffman(frecuenciaCanal, pixelesCanal);
        for (int i = 0; i < frecuenciaCanal.length; i++) {
            if (frecuenciaCanal[i] != 0) {
                sBufferCanal8.append(Arrays.toString(hm.getCodigo(String.valueOf(i)).toCharArray()) + ": " + String.valueOf(i) + "\n");
            }
        }
        sBufferCanal8.append("<L>: \n");

        frecuenciaCanal = pimg.getFrecuencia(biCanal10);
        StringBuffer sBufferCanal10 = new StringBuffer();
        hm = new Huffman(frecuenciaCanal, pixelesCanal);
        for (int i = 0; i < frecuenciaCanal.length; i++) {
            if (frecuenciaCanal[i] != 0) {
                sBufferCanal10.append(Arrays.toString(hm.getCodigo(String.valueOf(i)).toCharArray()) + ": " + String.valueOf(i) + "\n");
            }
        }
        sBufferCanal10.append("<L>: \n");


        System.out.println(sBufferCanal2.toString());
        System.out.println(sBufferCanal8.toString());
        System.out.println(sBufferCanal10.toString());
    }

}
