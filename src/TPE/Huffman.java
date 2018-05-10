package TPE;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Huffman {

    private ArrayList<Simbolo> listaSimbolo;

    public Huffman() {
        this.listaSimbolo = new ArrayList<Simbolo>();
    }

    public void addSimbolo(Simbolo simbolo) {
        this.listaSimbolo.add(simbolo);
    }

    public void addSimbolo(String simbolo, Double probabilidad) {
        Simbolo s = new Simbolo(simbolo, probabilidad);
        this.listaSimbolo.add(s);
    }

    public void codificar() {
        HashMap<String, String> codificacion = this.inicHashCodificacion(this.listaSimbolo);
        ArrayList<Pair<Double, ArrayList<String>>> listProbConSimb = this.cargarListaProbConSimb(this.listaSimbolo);
        ArrayList<Double> listProb = this.cargarListaProb(this.listaSimbolo);

        this.crearArbolHuffman(codificacion, listProbConSimb, listProb);

        ArrayList<String> test = new ArrayList<>(codificacion.keySet());

        for (String key : test) {
            System.out.println(key +" "+ codificacion.get(key));
        }

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

}
