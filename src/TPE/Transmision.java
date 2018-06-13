package TPE;

public class Transmision {
    private Canal canal;
    private static final double EPSILON = 0.0001d;
    private static final int MIN_TRANSMISIONES = 200;
    private double[][] matCanal;

    public Transmision(Canal canal){
        this.canal = canal;
    }

    public Canal transmitir(double[] frecuenciaAcumulada, int n) {
        Canal canalNuevo = new Canal();

        for (int i = 0; i < n; i++) {
            int genTono = this.generarEntrada(frecuenciaAcumulada);
            canalNuevo.addTransmision(genTono, this.canal.transmitir(genTono));
        }
        return canalNuevo;
    }

   public Canal transmitir(double[] frecuenciaAcumulada){
        Canal canalNuevo = new Canal();
        int transmisiones = 0;

        while (!canalNuevo.converge(EPSILON, MIN_TRANSMISIONES, transmisiones)) {
            int genTono = this.generarEntrada(frecuenciaAcumulada);
            canalNuevo.addTransmision(genTono, this.canal.transmitir(genTono));
            transmisiones++;
        }

        return canalNuevo;
    }

    private int generarEntrada(double[] frecuenciaAcumulada){
        double r = Math.random();
        for (int i = 0; i < frecuenciaAcumulada.length; i++) {
            if(frecuenciaAcumulada[i] > r)
                return i;
        }
        return -1;
    }

}
