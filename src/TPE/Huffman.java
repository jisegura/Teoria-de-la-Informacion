package TPE;

import javafx.util.Pair;

import java.util.*;

public class Huffman {

    private class Nodo {
        private Integer tono;
        private Nodo ramaSuperior;
        private Nodo ramaInferior;

        private boolean esHoja() {
            return  tono != null;
        }

        private Integer getTono() {
            return tono;
        }

        private void setTono(Integer tono) {
            this.tono = tono;
        }

        private Nodo getRamaSuperior() {
            return ramaSuperior;
        }

        private void setRamaSuperior(Nodo ramaSuperior) {
            this.ramaSuperior = ramaSuperior;
        }

        private Nodo getRamaInferior() {
            return ramaInferior;
        }

        private void setRamaInferior(Nodo ramaInferior) {
            this.ramaInferior = ramaInferior;
        }
    }

    private Nodo arbolHuffman;
    private Nodo posArbol;
    private HashMap<String, String> hashHuffman;
    private ArrayList<Simbolo> simbolos;

    public Huffman(int[] frecuencias, int cantPixeles) {
        ArrayList<Simbolo> listaSimbolos = this.cargarListaSimbolos(frecuencias, cantPixeles);
        this.simbolos = this.cargarListaSimbolos(frecuencias, cantPixeles);

        this.hashHuffman = this.crearHashHuffman(listaSimbolos);

        this.arbolHuffman = this.crearArbolHuffman(listaSimbolos);
        this.posArbol = this.arbolHuffman;
    }

    public double getLongMedia() {
        double longMedia = 0d;

        for (Simbolo s : this.simbolos) {
            longMedia += this.hashHuffman.get(s.getSimbolo()).toCharArray().length * s.getProbabilidad();
        }

        return longMedia;
    }

    public String getCodigo(String tono) {
        return this.hashHuffman.get(tono);
    }

    public int nodoGetTono() {
        int tono = this.posArbol.getTono();
        this.posArbol = this.arbolHuffman;
        return tono;
    }

    public void nodoSuperior() {
        this.posArbol = this.posArbol.getRamaSuperior();
    }

    public void nodoInferior() {
        this.posArbol = this.posArbol.getRamaInferior();
    }

    public boolean nodoEsHoja() {
        return this.posArbol.esHoja();
    }

    private Nodo crearArbolHuffman(ArrayList<Simbolo> listaSimbolos) {
        Nodo raiz = new Nodo();

        for (Simbolo s : listaSimbolos) {
            this.agregarHoja(raiz, s);
        }

        return raiz;
    }

    private void agregarHoja(Nodo arbol, Simbolo simbolo) {
        Nodo posNodo = arbol;
        char[] codigo = this.hashHuffman.get(simbolo.getSimbolo()).toCharArray();
        for (int i = 1; i <= codigo.length; i++) {
            if (codigo[i - 1] == '1') {
                if (posNodo.getRamaSuperior() == null) {
                    posNodo.setRamaSuperior(new Nodo());
                }
                posNodo = posNodo.getRamaSuperior();
                if (i == codigo.length) {
                    posNodo.setTono(Integer.valueOf(simbolo.getSimbolo()));
                }
            } else {
                if (posNodo.getRamaInferior() == null) {
                    posNodo.setRamaInferior(new Nodo());
                }
                posNodo = posNodo.getRamaInferior();
                if (i == codigo.length) {
                    posNodo.setTono(Integer.valueOf(simbolo.getSimbolo()));
                }
            }
        }
    }

    private HashMap<String, String> crearHashHuffman(ArrayList<Simbolo> listaSimbolos) {
        HashMap<String, String> codificacion = this.inicHashCodificacion(listaSimbolos);
        ArrayList<Pair<Double, ArrayList<String>>> listProbConSimb = this.cargarListaProbConSimb(listaSimbolos);
        ArrayList<Double> listProb = this.cargarListaProb(listaSimbolos);

        this.crearHuffman(codificacion, listProbConSimb, listProb);

        return codificacion;
    }

    private void crearHuffman(HashMap<String, String> codificacion, ArrayList<Pair<Double, ArrayList<String>>> listProbConSimb, ArrayList<Double> listProb) {
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
            this.crearHuffman(codificacion, listProbConSimb, listProb);
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
