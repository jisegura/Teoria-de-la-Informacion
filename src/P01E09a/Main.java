package P01E09a;

public class Main {

    public static void main(String[] args) {
        float prob2 = (new Main()).CalcularProbabilidadAlmenosUnoEsFacil();
        float prob = (new Main()).AlmenosUnoEsFacil();
        System.out.println("La probabilidad calculada es " + prob);
        System.out.println("La probabilidad 2 calculada es " + prob2);
    }

    private int AgarrarSobre() {
        float[] probAcumulada = new float[] {3f/5, 1f};
        float prob = (float) Math.random();
        for (int i = 0; i < probAcumulada.length; i++) {
            if (prob < probAcumulada[i]) {
                return i+1; // 1=facil 2=dificil
            }
        }

        return -1;
    }

    private boolean Converge(float probActual, float probAnterior, int tiradas) {
        if (tiradas > 10000) {
            return (Math.abs(probActual - probAnterior) < 0.001f);
        }
        return false;
    }

    private float CalcularProbabilidadSobre(int dificultad) {
        int exitos = 0;
        int tiradas = 0;
        float prob = 0;
        float probAnterior = -1;

        while (!this.Converge(prob, probAnterior, tiradas)) {
            int sobre = this.AgarrarSobre();

            if (sobre == dificultad) {
                exitos++;
            }
            tiradas++;

            probAnterior = prob;
            prob = (float) (exitos) / tiradas;
        }

        return prob;
    }

    private float CalcularProbabilidadAlmenosUnoEsFacil() {
        int exitos = 0;
        int tiradas = 0;
        float prob = 0;
        float probAnterior = -1;

        while (!this.Converge(prob, probAnterior, tiradas)) {
            int sobre1 = this.AgarrarSobre();
            int sobre2 = this.AgarrarSobre();

            if ((sobre1 == 1 && sobre2 == 2) || // (sobre1 == 1 || sobre2 == 1)
                (sobre1 == 1 && sobre2 == 1) ||
                (sobre1 == 2 && sobre2 == 1)) {
                exitos++;
            }
            tiradas++;

            probAnterior = prob;
            prob = (float) (exitos) / tiradas;
        }

        return prob;
    }

    private float AlmenosUnoEsFacil() {
        float prob1 = this.CalcularProbabilidadSobre(1); // 1=facil
        float prob2 = this.CalcularProbabilidadSobre(2); // 2=dificil
        return prob1 * prob2 + prob1 * prob1 + prob2 * prob1;
    }

}
