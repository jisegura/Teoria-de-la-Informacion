package TPE;

import javafx.util.Pair;

public class Simbolo {

    private Pair<String, Double> simbolo;

    public Simbolo(String simbolo, Double probabilidad) {
        this.simbolo = new Pair<>(simbolo, probabilidad);
    }

    public String getSimbolo() {
        return this.simbolo.getKey();
    }

    public Double getProbabilidad() {
        return this.simbolo.getValue();
    }
}
