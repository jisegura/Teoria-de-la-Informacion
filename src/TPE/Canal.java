package TPE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Canal {
    private double[][] matCanal;
    private double[][] matCanalAnt;
    private double[][] matCondicionalXY;
    private double[][] matConjunta;
    private double[][] matAcumulada;
    private Double[] probMarginalX;
    private Double[] probMarginalY;
    private int[] frecuenciaX;
    private int[] frecuenciaY;
    private int[][] frecuenciaCanal;

    public Canal(BufferedImage imgEntrada, BufferedImage imgSalida) {
        this.probMarginalX = new ProbImagesBMP().getProbabilidadPorTonoDeColor(imgEntrada);
        this.probMarginalY = new ProbImagesBMP().getProbabilidadPorTonoDeColor(imgSalida);
        this.matCanal = getMatrizCanal(imgEntrada, imgSalida);
        this.matAcumulada = getMatrizAcumulada();
        this.matConjunta = getMatrizConjunta(imgEntrada);
        this.matCondicionalXY = getMatrizCanal(imgSalida, imgEntrada);
    }

    public Canal(){
        this.matCanal = new double[16][16];
        this.matCanalAnt = new double[16][16];
        this.matCondicionalXY = new double[16][16];
        this.matConjunta = new double[16][16];
        this.matAcumulada = new double[16][16];
        this.probMarginalX = new Double[256];
        this.probMarginalY = new Double[256];
        this.frecuenciaX = new int[16];
        this.frecuenciaY = new int[16];
        this.frecuenciaCanal = new int[16][16];
        for (int i = 0; i < this.probMarginalY.length; i++) {
            this.probMarginalY[i] = 0.0;
            this.probMarginalX[i] = 0.0;
        }
        for (int i = 0; i < this.matCanalAnt.length; i++) {
            for (int j = 0; j < this.matCanalAnt[i].length; j++) {
                this.matCanalAnt[i][j] = -1d;
            }
        }
    }

    private double[][] getMatrizCanal(BufferedImage imgEntrada, BufferedImage imgSalida) {
        double[][] matCondicional = new double[16][16];

        for (int i = 0; i < imgEntrada.getWidth(); i++) {
            for (int j = 0; j < imgEntrada.getHeight(); j++) {
                int tEntrada = new Color(imgEntrada.getRGB(i, j)).getGreen() >> 4;
                int tSalida = new Color(imgSalida.getRGB(i, j)).getGreen() >> 4;
                matCondicional[tSalida][tEntrada]++;
            }
        }

        int[] frecuencia = new ProbImagesBMP().getFrecuencia(imgEntrada);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                matCondicional[j][i] /= frecuencia[((i << 4) | i)];
            }
        }
        return matCondicional;

    }

    public double[][] getMatrizdeTransicion() {
        double[][] mat = new double[16][16];

        for (int i = 0; i < matCanal.length; i++) {
            for (int j = 0; j < matCanal[i].length; j++) {
                mat[i][j] = matCanal[i][j];
            }
        }

        return mat;
    }

    private double[][] getMatrizConjunta(BufferedImage imgEntrada) {
        double[][] matConjunta = new double[16][16];

        for (int i = 0; i < matConjunta.length; i++) {
            for (int j = 0; j < matConjunta[i].length; j++) {
                matConjunta[j][i] = (this.probMarginalX[(i << 4) | i] * this.matCanal[j][i]);
            }
        }

        return matConjunta;
    }

    private void updateMatCanal(){
        for(int i = 0; i < this.matCanal.length; i++){
            for (int j = 0; j < this.matCanal[i].length; j++) {
                if (this.frecuenciaX[i] != 0)
                    this.matCanal[j][i] = (double) this.frecuenciaCanal[j][i] / this.frecuenciaX[i];
                if (this.frecuenciaY[i] != 0)
                    this.matCondicionalXY[j][i] = (double) this.frecuenciaCanal[i][j]/ this.frecuenciaY[i];
            }
        }
    }

    private void updateProbMarginal(){
        int suma = 0;
        for (int i = 0; i < this.frecuenciaX.length; i++) {
            suma += this.frecuenciaX[i];
        }
        for (int i = 0; i < this.frecuenciaX.length; i++) {
            this.probMarginalX[i << 4 | i] = (double)this.frecuenciaX[i]/ suma;
            this.probMarginalY[i << 4 | i] = (double)this.frecuenciaY[i]/ suma;
        }
    }

    private void updateMatConjunta(){
        for (int i = 0; i < matConjunta.length; i++) {
            for (int j = 0; j < matConjunta[i].length; j++) {
                matConjunta[j][i] = (this.probMarginalX[i << 4 | i] * this.matCanal[j][i]);
            }
        }
    }

    private double[][] getMatrizAcumulada() {
        double[][] matAcu = new double[16][16];
        double acum = 0d;

        for (int i = 0; i < this.matCanal.length; i++) {
            acum = 0d;
            for (int j = 0; j < this.matCanal[i].length; j++) {
                acum += this.matCanal[j][i];
                matAcu[j][i] = acum;
            }
        }

        return matAcu;
    }

    public void addTransmision(int tonoEntrada, int tonoSalida) {
        this.copiarMatrizAct();
        this.frecuenciaX[tonoEntrada]++;
        this.frecuenciaY[tonoSalida]++;
        this.frecuenciaCanal[tonoSalida][tonoEntrada]++;
        this.updateMatCanal();
        this.updateProbMarginal();
        this.updateMatConjunta();
    }

    private void copiarMatrizAct() {
        for (int i = 0; i < this.matCanal.length; i++) {
            for (int j = 0; j < this.matCanal[i].length; j++) {
                this.matCanalAnt[i][j] = this.matCanal[i][j];
            }
        }
    }

    public boolean converge(double epsilon, int min, int contador) {
        if (contador > min) {
            for (int i = 0; i < this.matCanal.length; i++) {
                for (int j = 0; j < this.matCanal[i].length; j++) {
                    if (Math.abs(this.matCanal[i][j] - this.matCanalAnt[i][j]) > epsilon)
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    public int transmitir(int tono) {
        double r = Math.random();
        for (int i = 0; i < this.matAcumulada.length; i++) {
            if (this.matAcumulada[i][tono] > r)
                return i;
        }
        return -1;
    }

    public double getRuido() {
        double ruido = 0d;
        for (int i = 0; i < this.matConjunta.length; i++) {
            for (int j = 0; j < this.matConjunta[i].length; j++) {
                if (this.matCanal[i][j] != 0)
                    ruido += (this.matConjunta[i][j] * (-(Math.log(this.matCanal[i][j]) / (Math.log(2)))));
            }
        }
        return ruido;
    }

    public double getPerdida() {
        double perdida = 0d;
        for (int i = 0; i < this.matConjunta.length; i++) {
            for (int j = 0; j < this.matConjunta[i].length; j++) {
                if (this.matCondicionalXY[j][i] != 0d)
                    perdida += (this.matConjunta[i][j] * (-(Math.log(this.matCondicionalXY[j][i]) / (Math.log(2)))));
            }
        }
        return perdida;
    }

    public double getInformacionMutua() {
        double mutua = 0d;
        for (int i = 0; i < this.matConjunta.length; i++) {
            for (int j = 0; j < this.matConjunta[i].length; j++) {
                if (this.matConjunta[i][j] != 0d)
                    mutua += (this.matConjunta[i][j] * ((Math.log(this.matConjunta[i][j] / (this.probMarginalX[(j << 4) | j] * this.probMarginalY[(i << 4) | i])) / (Math.log(2)))));
            }
        }
        return mutua;
    }

    public double getEntropiaXY() {
        double entropia = 0d;
        for (int i = 0; i < this.matConjunta.length; i++) {
            for (int j = 0; j < this.matConjunta[i].length; j++) {
                if (this.matConjunta[i][j] != 0d)
                    entropia += (this.matConjunta[i][j] * (-(Math.log(this.matConjunta[i][j]) / (Math.log(2)))));
            }
        }
        return entropia;
    }

    public double getEntropiaX() {
        double entropia = 0d;
        for (int i = 0; i < this.probMarginalX.length; i++) {
            if (this.probMarginalX[i] != 0d)
                entropia += (this.probMarginalX[i] * (-(Math.log(this.probMarginalX[i]) / (Math.log(2)))));
        }
        return entropia;
    }

    public double getEntropiaY() {
        double entropia = 0d;
        for (int i = 0; i < this.probMarginalY.length; i++) {
            if (this.probMarginalY[i] != 0d)
                entropia += (this.probMarginalY[i] * (-(Math.log(this.probMarginalY[i]) / (Math.log(2)))));
        }
        return entropia;
    }

    public void imprimir() {
        System.out.println("Ruido: " + getRuido());
        System.out.println("Perdida: " + getPerdida());
        System.out.println("Inf Mutua: " + getInformacionMutua());
        System.out.println("EntropiaXY: " + getEntropiaXY());
        System.out.println("EntropiaX: " + getEntropiaX());
        System.out.println("EntropiaY: " + getEntropiaY());
        double cuentaInfM = getEntropiaX() + getEntropiaY() - getInformacionMutua();
        double cuentaRuido = getEntropiaX() + getRuido();
        double cuentaPerdida = getEntropiaY() + getPerdida();
        System.out.println("Cuenta Inf Mutua: " + cuentaInfM);
        System.out.println("Cuenta Ruido: " + cuentaRuido);
        System.out.println("Cuenta Perdida: " + cuentaPerdida);
    }
}