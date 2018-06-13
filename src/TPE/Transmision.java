package TPE;

public class Transmision {
    private Canal canal;
    private static final int MAX_TRANMISIONES = 100;
    private double[][] matCanal;

    public Transmision(Canal canal){
        this.canal = canal;
    }

    public Canal transmitir(double[] frecuenciaAcumulada, int n) {
        Canal canalNuevo = new Canal();

        for (int i = 0; i < n; i++) {
            int genTono = this.generarEntrada(frecuenciaAcumulada);
            canalNuevo.addTransmision(genTono, canal.transmitir(genTono));
        }

        return canalNuevo;
    }

   /* public Canal transmitir(double[] frecuenciaAcumulada){
        for (int i = 0; i < MAX_TRANMISIONES; i++){
             this.canal.transmitir(this.generarEntrada(frecuenciaAcumulada));

        }

    }
*/
    private int generarEntrada(double[] frecuenciaAcumulada){
        double r = Math.random();
        for (int i = 0; i < frecuenciaAcumulada.length; i++) {
            if(frecuenciaAcumulada[i] > r)
                return i;
        }
        return -1;
    }

}
