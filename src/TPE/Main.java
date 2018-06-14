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
        Transmision transmisionCanal2 = new Transmision(canal2);
        c25 = transmisionCanal2.transmitir(prob, 25);
        c75 = transmisionCanal2.transmitir(prob, 75);
        c150 = transmisionCanal2.transmitir(prob, 150);
        cEpsilon = transmisionCanal2.transmitir(prob);

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

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////PARTE FINAL/////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////EJERCICIO 1/////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////


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

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////EJERCICIO 2/////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////


        int[] frecuenciaCanal = pimg.getFrecuencia(biCanal2);
        int pixelesCanal = biCanal2.getWidth() * biCanal2.getHeight();
        StringBuffer sBufferCanal2 = new StringBuffer();
        hm = new Huffman(frecuenciaCanal, pixelesCanal);
        for (int i = 0; i < frecuenciaCanal.length; i++) {
            if (frecuenciaCanal[i] != 0) {
                sBufferCanal2.append(Arrays.toString(hm.getCodigo(String.valueOf(i)).toCharArray()) + ": " + String.valueOf(i) + "\n");
            }
        }
        sBufferCanal2.append("\n<L>: "+ hm.getLongMedia());

        frecuenciaCanal = pimg.getFrecuencia(biCanal8);
        StringBuffer sBufferCanal8 = new StringBuffer();
        hm = new Huffman(frecuenciaCanal, pixelesCanal);
        for (int i = 0; i < frecuenciaCanal.length; i++) {
            if (frecuenciaCanal[i] != 0) {
                sBufferCanal8.append(Arrays.toString(hm.getCodigo(String.valueOf(i)).toCharArray()) + ": " + String.valueOf(i) + "\n");
            }
        }
        sBufferCanal8.append("\n<L>: "+ hm.getLongMedia());

        frecuenciaCanal = pimg.getFrecuencia(biCanal10);
        StringBuffer sBufferCanal10 = new StringBuffer();
        hm = new Huffman(frecuenciaCanal, pixelesCanal);
        for (int i = 0; i < frecuenciaCanal.length; i++) {
            if (frecuenciaCanal[i] != 0) {
                sBufferCanal10.append(Arrays.toString(hm.getCodigo(String.valueOf(i)).toCharArray()) + ": " + String.valueOf(i) + "\n");
            }
        }
        sBufferCanal10.append("\n<L>: "+ hm.getLongMedia());

        (new Compresor()).semiEstatico(biCanal2, DIRFINAL+"/salida_ej02_Canal2(comprimido)");
        (new Compresor()).semiEstatico(biCanal8, DIRFINAL+"/salida_ej02_Canal8(comprimido)");
        (new Compresor()).semiEstatico(biCanal10, DIRFINAL+"/salida_ej02_Canal10(comprimido)");
        long pesoComprimidoCanal2 = new File(DIRFINAL+"/salida_ej02_Canal2(comprimido).bin").length();
        long pesoOriginalCanal2 = iw.getSizeImage(ImagesWill.WILLCANAL2);
        long pesoComprimidoCanal8 = new File(DIRFINAL+"/salida_ej02_Canal8(comprimido).bin").length();
        long pesoOriginalCanal8 = iw.getSizeImage(ImagesWill.WILLCANAL8);
        long pesoComprimidoCanal10 = new File(DIRFINAL+"/salida_ej02_Canal10(comprimido).bin").length();
        long pesoOriginalCanal10 = iw.getSizeImage(ImagesWill.WILLCANAL10);
        formatter = new DecimalFormat("#0.00");
        try {
            PrintWriter write = new PrintWriter(DIRFINAL+"/salida_ej02.txt", "UTF-8");
            write.println("Salida del ejercio 2\n");
            write.println("//////////////Canal2//////////////");
            write.println(sBufferCanal2.toString());
            write.println("Tasa de compresión: " + formatter.format((double) pesoOriginalCanal2 / pesoComprimidoCanal2));
            write.println("\n//////////////Canal8//////////////");
            write.println(sBufferCanal8.toString());
            write.println("Tasa de compresión: " + formatter.format((double) pesoOriginalCanal8 / pesoComprimidoCanal8));
            write.println("\n//////////////Canal10//////////////");
            write.println(sBufferCanal10.toString());
            write.println("Tasa de compresión: " + formatter.format((double) pesoOriginalCanal10 / pesoComprimidoCanal10));
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////EJERCICIO 3/////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        Transmision transmisionCanal8 = new Transmision(canal8);
        Transmision transmisionCanal10 = new Transmision(canal10);

        Canal canal2Will6 = transmisionCanal2.transmitir(iw.getBufferedImage(ImagesWill.WILL6));
        Canal canal8Will6 = transmisionCanal8.transmitir(iw.getBufferedImage(ImagesWill.WILL6));
        Canal canal10Will6 = transmisionCanal10.transmitir(iw.getBufferedImage(ImagesWill.WILL6));

        try {
            PrintWriter write = new PrintWriter(DIRFINAL+"/salida_ej03.txt", "UTF-8");
            write.println("Salida del ejercio 3\n");
            write.println("Transmision Canal2 (Will6):");
            write.println("Ruido: "+ canal2Will6.getRuido());
            write.println("Perdida: "+ canal2Will6.getPerdida());
            write.println("Informacion Mutua: "+ canal2Will6.getInformacionMutua());
            write.println("\nTransmision Canal8 (Will6):");
            write.println("Ruido: "+ canal8Will6.getRuido());
            write.println("Perdida: "+ canal8Will6.getPerdida());
            write.println("Informacion Mutua: "+ canal8Will6.getInformacionMutua());
            write.println("\nTransmision Canal10 (Will6):");
            write.println("Ruido: "+ canal10Will6.getRuido());
            write.println("Perdida: "+ canal10Will6.getPerdida());
            write.println("Informacion Mutua: "+ canal10Will6.getInformacionMutua());
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////EJERCICIO 4/////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        Canal canal2_10, canal2_100, canal2_1000, canal2_10000, canal2_100000, canal2Epsilon, canal8_10, canal8_100,
                canal8_1000, canal8_10000, canal8_100000, canal8Epsilon, canal10_10, canal10_100, canal10_1000, canal10_10000, canal10_100000, canal10Epsilon;
        double[] probTonos = pimg.getProbabilidadAcumulada(iw.getBufferedImage(ImagesWill.WILLORIGINAL));

        ////////////CANAL 2///////////////////////////////////////
        canal2_10 = transmisionCanal2.transmitir(probTonos, 10);
        canal2_100 = transmisionCanal2.transmitir(probTonos, 100);
        canal2_1000 = transmisionCanal2.transmitir(probTonos, 1000);
        canal2_10000 = transmisionCanal2.transmitir(probTonos, 10000);
        canal2_100000 = transmisionCanal2.transmitir(probTonos, 100000);
        canal2Epsilon = transmisionCanal2.transmitir(probTonos);

        ////////////CANAL 8///////////////////////////////////////
        canal8_10= transmisionCanal8.transmitir(probTonos, 10);
        canal8_100 = transmisionCanal8.transmitir(probTonos, 100);
        canal8_1000 = transmisionCanal8.transmitir(probTonos, 1000);
        canal8_10000 = transmisionCanal8.transmitir(probTonos, 10000);
        canal8_100000 = transmisionCanal8.transmitir(probTonos, 100000);
        canal8Epsilon = transmisionCanal8.transmitir(probTonos);

        ////////////CANAL 10///////////////////////////////////////
        canal10_10 = transmisionCanal10.transmitir(probTonos, 10);
        canal10_100 = transmisionCanal10.transmitir(probTonos, 100);
        canal10_1000 = transmisionCanal10.transmitir(probTonos, 1000);
        canal10_10000 = transmisionCanal10.transmitir(probTonos, 10000);
        canal10_100000 = transmisionCanal10.transmitir(probTonos, 100000);
        canal10Epsilon = transmisionCanal10.transmitir(probTonos);

        try {
            PrintWriter write = new PrintWriter(DIRFINAL+"/salida_ej04.txt", "UTF-8");
            write.println("Salida del ejercio 4\n");
            write.println("////////////////Canal 2////////////////");
            write.println("10 datos:");
            write.println("Ruido: "+ canal2_10.getRuido());
            write.println("Perdida: "+ canal2_10.getPerdida());
            write.println("Informacion Mutua: "+ canal2_10.getInformacionMutua());
            write.println("\n100 datos:");
            write.println("Ruido: "+ canal2_100.getRuido());
            write.println("Perdida: "+ canal2_100.getPerdida());
            write.println("Informacion Mutua: "+ canal2_100.getInformacionMutua());
            write.println("\n1000 datos:");
            write.println("Ruido: "+ canal2_1000.getRuido());
            write.println("Perdida: "+ canal2_1000.getPerdida());
            write.println("Informacion Mutua: "+ canal2_1000.getInformacionMutua());
            write.println("\n10000 datos:");
            write.println("Ruido: "+ canal2_10000.getRuido());
            write.println("Perdida: "+ canal2_10000.getPerdida());
            write.println("Informacion Mutua: "+ canal2_10000.getInformacionMutua());
            write.println("\n100000 datos:");
            write.println("Ruido: "+ canal2_100000.getRuido());
            write.println("Perdida: "+ canal2_100000.getPerdida());
            write.println("Informacion Mutua: "+ canal2_100000.getInformacionMutua());
            write.println("\nConverge:");
            write.println("Ruido: "+ canal2Epsilon.getRuido());
            write.println("Perdida: "+ canal2Epsilon.getPerdida());
            write.println("Informacion Mutua: "+ canal2Epsilon.getInformacionMutua());

            write.println("\n////////////////Canal 8////////////////");
            write.println("10 datos:");
            write.println("Ruido: "+ canal8_10.getRuido());
            write.println("Perdida: "+ canal8_10.getPerdida());
            write.println("Informacion Mutua: "+ canal8_10.getInformacionMutua());
            write.println("\n100 datos:");
            write.println("Ruido: "+ canal8_100.getRuido());
            write.println("Perdida: "+ canal8_100.getPerdida());
            write.println("Informacion Mutua: "+ canal8_100.getInformacionMutua());
            write.println("\n1000 datos:");
            write.println("Ruido: "+ canal8_1000.getRuido());
            write.println("Perdida: "+ canal8_1000.getPerdida());
            write.println("Informacion Mutua: "+ canal8_1000.getInformacionMutua());
            write.println("\n10000 datos:");
            write.println("Ruido: "+ canal8_10000.getRuido());
            write.println("Perdida: "+ canal8_10000.getPerdida());
            write.println("Informacion Mutua: "+ canal8_10000.getInformacionMutua());
            write.println("\n100000 datos:");
            write.println("Ruido: "+ canal8_100000.getRuido());
            write.println("Perdida: "+ canal8_100000.getPerdida());
            write.println("Informacion Mutua: "+ canal8_100000.getInformacionMutua());
            write.println("\nConverge:");
            write.println("Ruido: "+ canal8Epsilon.getRuido());
            write.println("Perdida: "+ canal8Epsilon.getPerdida());
            write.println("Informacion Mutua: "+ canal8Epsilon.getInformacionMutua());

            write.println("\n////////////////Canal 10////////////////");
            write.println("10 datos:");
            write.println("Ruido: "+ canal10_10.getRuido());
            write.println("Perdida: "+ canal10_10.getPerdida());
            write.println("Informacion Mutua: "+ canal10_10.getInformacionMutua());
            write.println("\n100 datos:");
            write.println("Ruido: "+ canal10_100.getRuido());
            write.println("Perdida: "+ canal10_100.getPerdida());
            write.println("Informacion Mutua: "+ canal10_100.getInformacionMutua());
            write.println("\n1000 datos:");
            write.println("Ruido: "+ canal10_1000.getRuido());
            write.println("Perdida: "+ canal10_1000.getPerdida());
            write.println("Informacion Mutua: "+ canal10_1000.getInformacionMutua());
            write.println("\n10000 datos:");
            write.println("Ruido: "+ canal10_10000.getRuido());
            write.println("Perdida: "+ canal10_10000.getPerdida());
            write.println("Informacion Mutua: "+ canal10_10000.getInformacionMutua());
            write.println("\n100000 datos:");
            write.println("Ruido: "+ canal10_100000.getRuido());
            write.println("Perdida: "+ canal10_100000.getPerdida());
            write.println("Informacion Mutua: "+ canal10_100000.getInformacionMutua());
            write.println("\nConverge:");
            write.println("Ruido: "+ canal10Epsilon.getRuido());
            write.println("Perdida: "+ canal10Epsilon.getPerdida());
            write.println("Informacion Mutua: "+ canal10Epsilon.getInformacionMutua());
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


}
