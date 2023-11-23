import java.util.Random;

class Perceptron {
    private double[] weights;

    public Perceptron() {
        this.weights = new double[Constants.CHROMOSOME_SIZE];
        initializeWeights();
    }

    public void initializeWeights() {
        Random random = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextDouble() * 2 - 1;
        }
    }

    public void setPesos(double[] pesos) {
        this.weights = pesos;
    }

    //Com base nos pesos do perceptron o mesmo tenta prever o atributo classe do objeto
    public int predict(double[] inputs) {
        double sum = 0;

        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }

        return (sum > 0) ? 1 : 0;
    }
}