package TPE;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Huffman {

    public void comprimirSemiEstatico(BufferedImage imagen, String dirDestino) {
        int[] frecuencias = (new ProbImagesBMP()).getFrecuencia(imagen);
        int width = imagen.getWidth();
        int height = imagen.getHeight();

        ArrayList<Simbolo> listaSimbolos = this.cargarListaSimbolos(frecuencias, width * height);

        HashMap<String, String> codigoHuffman = this.codificar(listaSimbolos);

        this.crearComprimido(imagen, dirDestino, codigoHuffman);

    }

    public void descomprimirSemiEstatico(String dirOrigen, String dirDestino, BufferedImage imagen) {
        int[] frecuencias = (new ProbImagesBMP()).getFrecuencia(imagen);
        int width = imagen.getWidth();
        int height = imagen.getHeight();

        ArrayList<Simbolo> listaSimbolos = this.cargarListaSimbolos(frecuencias, width * height);

        HashMap<String, String> codigoHuffman = this.codificar(listaSimbolos);
        HashMap<String, String> codigoHuffmanInvertido = new HashMap<>();

        ArrayList<String> keys = new ArrayList<>(codigoHuffman.keySet());

        for (String key : keys) {
            codigoHuffmanInvertido.put(codigoHuffman.get(key), key);
        }


        try {
            FileInputStream fis = new FileInputStream(dirOrigen);
            ObjectInputStream ois = new ObjectInputStream(fis);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            String sTono;
            StringBuffer buffer = new StringBuffer();
            int cantidad = 0;
            int tonos = 0;
            int cantidadBits = 0;
            char c = 0;
            int mask = 32768;

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cantidad++;
                    boolean coloreado = false;

                    while (!coloreado) {
                        if ((sTono = codigoHuffmanInvertido.get(buffer.toString())) != null) {
                            tonos++;
                            int iTono = Integer.parseInt(sTono);
                            Color color = new Color(iTono, iTono, iTono);
                            bi.setRGB(i, j, color.getRGB());
                            buffer = new StringBuffer();
                            coloreado = true;
                        } else {
                            if (cantidadBits == 0) {
                                mask = 32768;
                                cantidadBits = 16;
                                c = ois.readChar();
                            }
                            if ((c & mask) == mask) {
                                buffer.append("1");
                            } else {
                                buffer.append("0");
                            }
                            cantidadBits--;
                            mask = mask >> 1;
                        }
                    }
                }
            }

            System.out.println("pixeles coloreados: "+ cantidad);
            System.out.println("tonos encontrados: "+ tonos);

            ImageIO.write(bi, "bmp", new File(dirDestino));



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void crearComprimido(BufferedImage imagen, String dirDestino, HashMap<String, String> codigoHuffman) {


        ArrayList<String> p = new ArrayList<>(codigoHuffman.keySet());
        for (String s :
                p) {
            System.out.println(Arrays.toString(codigoHuffman.get(s).toCharArray()) + " " + s);
        }


        try {
            FileOutputStream fos = new FileOutputStream(dirDestino);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            int buffer = 0;
            int bitsCounter = 0;
            int cantidaddeIntguardados = 0;
            for (int i = 0; i < imagen.getWidth(); i++) {
                for (int j = 0; j < imagen.getHeight(); j++) {
                    int rgb = imagen.getRGB(i, j);
                    int simbolo = (new Color(rgb, true)).getGreen();
                    char[] codigo = codigoHuffman.get(String.valueOf(simbolo)).toCharArray();
                    for (char bit : codigo) {
                        buffer = (buffer << 1);
                        if (bit == '1') {
                            buffer = (char) (buffer | 1);
                        }
                        bitsCounter++;
                        if (bitsCounter == 16) {
                            oos.writeChar(buffer);
                            cantidaddeIntguardados++;
                            buffer = 0;
                            bitsCounter = 0;
                        }
                    }
                }
            }

            if ((bitsCounter < 16) && (bitsCounter != 0)) {
                buffer = (buffer << (16-bitsCounter));
                oos.writeChar(buffer);
                cantidaddeIntguardados++;
            }

            System.out.println("cantidad de integers guardados: "+cantidaddeIntguardados);

            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private HashMap<String, String> codificar(ArrayList<Simbolo> listaSimbolos) {
        HashMap<String, String> codificacion = this.inicHashCodificacion(listaSimbolos);
        ArrayList<Pair<Double, ArrayList<String>>> listProbConSimb = this.cargarListaProbConSimb(listaSimbolos);
        ArrayList<Double> listProb = this.cargarListaProb(listaSimbolos);

        this.crearArbolHuffman(codificacion, listProbConSimb, listProb);

        return codificacion;
    }

    private void crearArbolHuffman(HashMap<String, String> codificacion, ArrayList<Pair<Double, ArrayList<String>>> listProbConSimb, ArrayList<Double> listProb) {
        if (listProb.size() != 1) {
            Collections.sort(listProb);
            Pair<Double, Double> parMasChico = new Pair<>(listProb.get(0), listProb.get(1));
            ArrayList<String> branchInf = this.identificarRama(parMasChico.getKey(), listProbConSimb, listProb);
            ArrayList<String> branchSup = this.identificarRama(parMasChico.getValue(), listProbConSimb, listProb);
            Double newProb = parMasChico.getKey() + parMasChico.getValue();
            ArrayList<String> newBranch = new ArrayList<>();
            newBranch.addAll(branchInf);
            newBranch.addAll(branchSup);
            Pair<Double, ArrayList<String>> newPar = new Pair<>(newProb, newBranch);
            listProb.add(newProb);
            listProbConSimb.add(newPar);
            this.crearArbolHuffman(codificacion, listProbConSimb, listProb);
            for (String simbolo : branchInf) {
                codificacion.put(simbolo, codificacion.get(simbolo) + "0");
            }
            for (String simbolo : branchSup) {
                codificacion.put(simbolo, codificacion.get(simbolo) + "1");
            }
        }
    }

    private ArrayList<String> identificarRama(Double prob, ArrayList<Pair<Double, ArrayList<String>>> listProbConSimb, ArrayList<Double> listProb) {
        ArrayList<String> branch = new ArrayList<>();

        Iterator<Double> it_listProb = listProb.iterator();
        boolean done = false;

        while (it_listProb.hasNext() && !done) {
            Double probabilidad = it_listProb.next();
            if (prob.equals(probabilidad)) {
                it_listProb.remove();
                done = true;
            }
        }

        Iterator<Pair<Double, ArrayList<String>>> it_listProbConSimb = listProbConSimb.iterator();
        done = false;

        while (it_listProbConSimb.hasNext() && !done) {
            Pair<Double, ArrayList<String>> par = it_listProbConSimb.next();
            if (prob.equals(par.getKey())) {
                branch.addAll(par.getValue());
                it_listProbConSimb.remove();
                done = true;
            }
        }


        return branch;
    }

    private HashMap<String, String> inicHashCodificacion(ArrayList<Simbolo> simbolos) {
        HashMap<String, String> hashCod = new HashMap<>();
        for (Simbolo simbolo : simbolos) {
            hashCod.put(simbolo.getSimbolo(), "");
        }
        return hashCod;
    }

    private ArrayList<Double> cargarListaProb(ArrayList<Simbolo> simbolos) {
        ArrayList<Double> listProb = new ArrayList<>();
        for (Simbolo simbolo : simbolos) {
            listProb.add(simbolo.getProbabilidad());
        }
        return listProb;
    }

    private ArrayList<Pair<Double, ArrayList<String>>> cargarListaProbConSimb(ArrayList<Simbolo> simbolos) {
        ArrayList<Pair<Double, ArrayList<String>>> listProb = new ArrayList<>();
        for (Simbolo simbolo : simbolos) {
            ArrayList<String> listaSimbolo = new ArrayList<>();
            listaSimbolo.add(simbolo.getSimbolo());
            Pair<Double, ArrayList<String>> par = new Pair<>(simbolo.getProbabilidad(), listaSimbolo);
            listProb.add(par);
        }
        return listProb;
    }

    private ArrayList<Simbolo> cargarListaSimbolos(int[] frecuencias, int pixeles) {
        ArrayList<Simbolo> listaSimbolos = new ArrayList<>();
        for (int i = 0; i < frecuencias.length; i++) {
            if (frecuencias[i] != 0) {
                Simbolo simbolo = new Simbolo(String.valueOf(i), (double) frecuencias[i] / pixeles);
                listaSimbolos.add(simbolo);
            }
        }
        return listaSimbolos;
    }

}
