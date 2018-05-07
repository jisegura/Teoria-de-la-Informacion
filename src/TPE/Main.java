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
        HashMap<Double, String> solucion = new HashMap();
        Double r;
        CompararImagesBMP cimg = new CompararImagesBMP();

        try {
            BufferedImage imgOriginal = ImageIO.read(new File(DIR_RES+"/img/Will/"+WILLORIGINAL+".bmp"));
            r = cimg.getCoeficienteCorrelacion(imgOriginal, imgOriginal);
            System.out.println("Will_Original: " + r);
            for (String imgName : willArr) {
                BufferedImage imgComparar = ImageIO.read(new File(DIR_RES+"/img/Will/"+imgName+".bmp"));
                r = cimg.getCoeficienteCorrelacion(imgOriginal, imgComparar);
                solucion.put(r, imgName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Double> listaCorrelacion = new ArrayList(solucion.keySet());
        Collections.sort(listaCorrelacion, Collections.reverseOrder());
        String salidaEj01 = new String();
        for (Double key:listaCorrelacion) {
            salidaEj01 += solucion.get(key) + ": " + key + "\n";
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

        try {
            BufferedImage imgOriginal = ImageIO.read(new File(DIR_RES+"/img/Will/"+WILLORIGINAL+".bmp"));
            Double[] probOriginal = cimg.getProbabilidad(imgOriginal);
            Histograma hisOriginal = new Histograma(probOriginal, WILLORIGINAL);
            hisOriginal.saveAsPNG();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
