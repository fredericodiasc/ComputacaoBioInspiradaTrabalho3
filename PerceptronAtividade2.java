import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PerceptronAtividade2 {
    private double[] weights;
    private double learningRate;
    private static final int COLUMN_CONTROL = 1;

    private static final int[] TRAINING_GROUP_AMOUNT = {
            10, 10, 10, 10, 10};

    private static final double[] LEARNING_RATE = {
            0.1, 0.1, 0.1, 0.1, 0.1};

    private static final int[] EPOCHS = {
            10000, 10000, 10000, 10000, 10000};

    public PerceptronAtividade2(int numInputs, double learningRate) {
        weights = new double[numInputs];
        this.learningRate = learningRate;
        initializeWeights();
    }

    private void initializeWeights() {
        Random random = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextDouble();
        }
    }

    public double dotProduct(double[] inputs) {
        double result = 0;
        for (int i = 0; i < weights.length; i++) {
            result += inputs[i] * weights[i];
        }
        return result;
    }

    public int activate(double weightedSum) {
        return (weightedSum > 0) ? 1 : 0;
    }

    public void train(double[] inputs, int target) {
        double weightedSum = dotProduct(inputs);
        int prediction = activate(weightedSum);
        int error = target - prediction;

        for (int i = 0; i < weights.length; i++) {
            weights[i] += error * inputs[i] * learningRate;
        }
    }

    public static void main(String[] args) throws IOException {
        long tempoInicial = System.currentTimeMillis();
        String csvFile = "C:\\Users\\frede\\OneDrive\\Área de Trabalho\\trabalho3\\iris.data"; // Path Duas classes: data\\iris_p.data - Path três classes data\iris.data
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        for(int i=0 ; i < TRAINING_GROUP_AMOUNT.length; i++) {
            int k = 0;
            int l = 0;
            int trainingInstances = TRAINING_GROUP_AMOUNT[i];
            int testingInstances = lines.size() - TRAINING_GROUP_AMOUNT[i];

            int[] trainingTargets = new int[trainingInstances];
            double[][] trainingGroup = new double[trainingInstances][];
            int[] testingTargets = new int[testingInstances];
            double[][] testingGroup = new double[testingInstances][];
            double[][] resulting = new double[testingInstances][];

            // Processar os dados embaralhados
            for (int p = 0; p < lines.size(); p++) {
                String[] values = lines.get(p).split(",");
                double[] dataRow = new double[values.length - COLUMN_CONTROL];
                for (int j = 0; j < values.length - COLUMN_CONTROL; j++) {
                    dataRow[j] = Double.parseDouble(values[j]);
                }
                if (p < TRAINING_GROUP_AMOUNT[i]) {
                    trainingTargets[k] = values[values.length - 1].equals("Iris-setosa") ? 1 : 0;
                    trainingGroup[k] = dataRow;
                    k++;
                } else {
                    testingTargets[l] = values[values.length - 1].equals("Iris-setosa") ? 1 : 0;
                    testingGroup[l] = dataRow;
                    l++;
                }
            }
            int numInputs = trainingGroup[0].length;
            PerceptronAtividade2 perceptron = new PerceptronAtividade2(numInputs, LEARNING_RATE[i]);

            // Treinando o perceptron
            for (int epoch = 0; epoch < EPOCHS[i]; epoch++) {
                for (int q = 0; q < trainingGroup.length; q++) {
                    perceptron.train(trainingGroup[q], trainingTargets[q]);
                }
            }

            // Executando o aprendizado nos demais registros da tabela
            double amountError = 0;
            for (int r = 0; r < testingGroup.length; r++) {
                double result = perceptron.activate(perceptron.dotProduct(testingGroup[r]));
                double expected = testingTargets[r];
                double isRight = result == expected ? 0L : 1L;
                amountError = result == expected ? amountError : amountError + 1;
                resulting[r] = new double[]{result, expected, isRight};
            }

            amountError = amountError * 100 / testingGroup.length;
            String amountErrorNormalized = String.format("%.2f", amountError);
            System.out.println("--Configuration--" +
                    "\nEpocas: " + EPOCHS[i] +
                    "\nTaxa de aprendizado: " + LEARNING_RATE[i] +
                    "\nTamanho do conjunto de treinamento: " + trainingInstances +
                    "\nTamanho do conjunto de teste: " + testingInstances +
                    "\nTaxa de erro: " + amountErrorNormalized + "%\n");
        }
        long tempoFinal = System.currentTimeMillis() - tempoInicial;
        System.out.println("o metodo executou em " + tempoFinal);
    }
}

