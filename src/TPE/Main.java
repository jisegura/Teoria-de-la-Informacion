package TPE;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Main {

    static String WILLORIGINAL = "Will(Original)";
    static String WILL1 = "Will_1";
    static String WILL2 = "Will_2";
    static String WILL3 = "Will_3";
    static String WILL4 = "Will_4";
    static String WILL5 = "Will_5";
    static String WILL6 = "Will_6";
    static String WILL7 = "Will_7";
    static String DIR_RES = "/home/jis/Project/Teoria-de-la-Informacion/src/TPE/res";

    public static void main(String[] args) {
        String[] willArr = new String[]{WILL1, WILL2, WILL3, WILL4, WILL5, WILL6, WILL7};
        HashMap<Double, String> mapCorrelacion = new HashMap();
        Double factorCorrelacion;
        ProbImagesBMP pimg = new ProbImagesBMP();

        try {
            BufferedImage imgOriginal = ImageIO.read(new File(DIR_RES+"/img/Will/"+WILLORIGINAL+".bmp"));

            for (String imgName : willArr) {
                BufferedImage imgComparar = ImageIO.read(new File(DIR_RES+"/img/Will/"+imgName+".bmp"));
                factorCorrelacion = pimg.getCoeficienteCorrelacion(imgOriginal, imgComparar);
                mapCorrelacion.put(factorCorrelacion, imgName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Double> listaCorrelacion = new ArrayList(mapCorrelacion.keySet());
        Collections.sort(listaCorrelacion, Collections.reverseOrder());
        String salidaEj01 = new String();
        for (Double key:listaCorrelacion) {
            salidaEj01 += mapCorrelacion.get(key) + ": " + key + "\n";
        }

        try {
            PrintWriter write = new PrintWriter("salida_ej01.txt", "UTF-8");
            write.println("Salida del ejercio 1");
            write.println("El factor de correlación ordenado de mayor a menor\n");
            write.println(salidaEj01);
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Double first = listaCorrelacion.get(0);
        Double last = listaCorrelacion.get(listaCorrelacion.size()-1);

        try {
            BufferedImage imgOriginal = ImageIO.read(new File(DIR_RES+"/img/Will/"+WILLORIGINAL+".bmp"));
            BufferedImage imgFirst = ImageIO.read(new File(DIR_RES+"/img/Will/"+mapCorrelacion.get(first)+".bmp"));
            BufferedImage imgLast = ImageIO.read(new File(DIR_RES+"/img/Will/"+mapCorrelacion.get(last)+".bmp"));
            Double[] probOriginal = pimg.getProbabilidadPorTonoDeColor(imgOriginal);
            Double[] probFirst = pimg.getProbabilidadPorTonoDeColor(imgFirst);
            Double[] probLast = pimg.getProbabilidadPorTonoDeColor(imgLast);
            Double mediaOriginal = pimg.getMedia(probOriginal);
            Double mediaFirst = pimg.getMedia(probFirst);
            Double mediaLast = pimg.getMedia(probLast);
            Double desvioOriginal = pimg.getDesvioEstandar(imgOriginal);
            Double desvioFirst = pimg.getDesvioEstandar(imgFirst);
            Double desvioLast = pimg.getDesvioEstandar(imgLast);
            Histograma histogram = new Histograma();
            histogram.addHistograma(probOriginal, WILLORIGINAL, mediaOriginal,desvioOriginal);
            histogram.addHistograma(probFirst, mapCorrelacion.get(first), mediaFirst, desvioFirst);
            histogram.addHistograma(probLast, mapCorrelacion.get(last),mediaLast,desvioLast);
            histogram.saveAsBMP();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
