import java.util.Random;

class Perceptron {
    private double[] pesos;

    public Perceptron() {
        this.pesos = new double[3];
        initializeWeights();
    }

    public void initializeWeights() {
        Random random = new Random();
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = random.nextDouble() * 2 - 1;
        }
    }

    public void setPesos(double[] pesos) {
        this.pesos = pesos;
    }

    //Com base nos pesos do perceptron o mesmo tenta prever o atributo classe do objeto
    public int predict(double[] inputs) {
        double sum = 0;

        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * pesos[i];
        }

        return (sum > 0) ? 1 : 0;
    }
}