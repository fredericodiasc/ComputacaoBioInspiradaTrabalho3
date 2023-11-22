import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int POPULATION_SIZE = 3;
    private static final int MAX_GENERATIONS = 1;

    private static final int TOTAL_EXEC = 10;

    public static void main(String[] args) throws IOException {
        Perceptron perceptron = new Perceptron();

        GeneticAlgoritm ga = new GeneticAlgoritm();

        String csvFile = "C:\\Users\\frede\\OneDrive\\Área de Trabalho\\trabalho3\\iris.data"; // Caminho do arquivo CSV
        BufferedReader br = new BufferedReader(new FileReader(csvFile));

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        //Embaralhar os dados
        Collections.shuffle(lines, new Random());

        for (int exec = 0; exec < TOTAL_EXEC; exec++) {

            // Inicializa os pesos do algoritmo genetico
            double[][] population = ga.initializePopulation();
            double[] fitnessValues = new double[0];

            //Linhas do arquivo que serão usadas para treinamento do algoritmo genético
            double[][] trainingPopulation = new double[POPULATION_SIZE][];
            double[] trainingPopulationClass = new double[POPULATION_SIZE];

            //Linhas do arquivo que serão previstas pelo perceptron após o treinamento dele através do algoritmo genético
            double[][] populationToBeTrained = new double[lines.size() - POPULATION_SIZE][];
            double[] populationToBeTrainedClass = new double[lines.size() - POPULATION_SIZE];

            for (int p = 0; p < POPULATION_SIZE; p++) {
                String[] values = lines.get(p).split(",");
                double[] dataRow = new double[values.length - 1];
                for (int j = 0; j < values.length - 1; j++) {
                    dataRow[j] = Double.parseDouble(values[j]);
                }
                trainingPopulation[p] = dataRow;
                trainingPopulationClass[p] = values[values.length - 1].equals("Iris-setosa") ? 1 : 0;
            }

            int q = 0;
            for (int p = POPULATION_SIZE; p < lines.size(); p++) {
                String[] values = lines.get(p).split(",");
                double[] dataRow = new double[values.length - 1];
                for (int j = 0; j < values.length - 1; j++) {
                    dataRow[j] = Double.parseDouble(values[j]);
                }
                populationToBeTrained[q] = dataRow;
                populationToBeTrainedClass[q] = values[values.length - 1].equals("Iris-setosa") ? 1 : 0;
                q++;
            }

            for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
                //A cada geração do loop calcula o fitness de cada individuo(pesos) do algoritmo genético com auxilio do grupo de treinamento
                fitnessValues = ga.evaluateFitness(population, perceptron, trainingPopulation, trainingPopulationClass);

                //Realiza seleção por torneio de dois individuos(pesos)
                double[][] parents = ga.selectParents(population, fitnessValues);

                //Realiza o cruzamento entre os pais selecionados para gerar filhos
                double[][] offspring = ga.crossover(parents);

                //Realiza mutação nos filhos gerados
                ga.mutate(offspring);

                //Troca os individuos da população original com fitness ruim pelos filhos gerados com fitness melhor
                population = ga.replacePopulation(population, fitnessValues, offspring);

            }

            //Busca na população qual individuo(pesos) no final do aprendizado do AG tem o melhor fitness
            double[] bestWeights = ga.getBestIndividual(population, fitnessValues);

            //Seta o melhor individuo(pesos) nos pesos do perceptron
            perceptron.setPesos(bestWeights);


            System.out.println("\nTrained Perceptron Weights: " + Arrays.toString(bestWeights));
            System.out.println("Testing the trained perceptron:");

            //Chama a execução do percepetron para prever os restante das linhas do arquivo Iris
            predictPerceptron(perceptron, populationToBeTrained, populationToBeTrainedClass);
        }

    }

    private static void predictPerceptron(Perceptron perceptron,  double[][] populationToBeTrained,
                                       double[] populationToBeTrainedClass) {
        double countSuccess = 0;
        String success = "success";

        for(int i = 0; i < populationToBeTrained.length; i++) {
            int prediction = perceptron.predict(populationToBeTrained[i]);
            if (prediction == (int)populationToBeTrainedClass[i]) {
                countSuccess += 1;
            } else {
                success = "error";
            }
//            System.out.println(Arrays.toString(populationToBeTrained[i]) +
//                    " Prediction: " + prediction +
//                    " Expected: " + (int)populationToBeTrainedClass[i] +
//                    " Status: " + success);

            success = "success";
        }
        System.out.print("Taxa de sucesso: " + countSuccess/populationToBeTrained.length * 100 + "%");
    }
}