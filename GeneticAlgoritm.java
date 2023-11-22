import java.util.Random;

public class GeneticAlgoritm {

    //Constante para controlar o tamanho da população usada no aprendizado do algoritmo genético
    private static final int TAMANHO_DA_POPULACAO = 70;
    
    //Constante para controlar o tamanho do individio presente na população
    private static final int TAMANHO_DO_CROMOSSOMO = 4;
    
    //Constante para controlar a taxa de mutação
    private static final double TAXA_DE_MUTACAO = 0.01;

    //Construtor para injetar a classe de Algoritmo Genetico na classe Main
    public GeneticAlgoritm() {
    }

    public double[][] initializePopulation() {
        double[][] population = new double[TAMANHO_DA_POPULACAO][TAMANHO_DO_CROMOSSOMO];
        Random random = new Random();

        for (int i = 0; i < TAMANHO_DA_POPULACAO; i++) {
            for (int j = 0; j < TAMANHO_DO_CROMOSSOMO; j++) {
                population[i][j] = random.nextDouble() * 2 - 1; // Random weights between -1 and 1
            }
        }

        return population;
    }

    public double[] evaluateFitness(double[][] population, Perceptron perceptron,
                                            double[][] trainingPopulation, double[] trainingPopulationClass) {
        double[] fitnessValues = new double[TAMANHO_DA_POPULACAO];

        for (int i = 0; i < TAMANHO_DA_POPULACAO; i++) {

            double[] weights = population[i];
            perceptron.setPesos(weights);
            int correctCount = 0;

            for (int j = 0; j < trainingPopulation.length; j++) {
                int prediction = perceptron.predict(trainingPopulation[j]);
                if (prediction == trainingPopulationClass[j]) {
                    correctCount++;
                }
            }

            fitnessValues[i] = (double) correctCount / trainingPopulation.length;
        }

        return fitnessValues;
    }

    public double[][] selectParents(double[][] population, double[] fitnessValues) {
        double[][] parents = new double[2][TAMANHO_DO_CROMOSSOMO];

        for (int i = 0; i < 2; i++) {
            int index1 = new Random().nextInt(TAMANHO_DA_POPULACAO);
            int index2 = new Random().nextInt(TAMANHO_DA_POPULACAO);

            if (fitnessValues[index1] > fitnessValues[index2]) {
                parents[i] = population[index1];
            } else {
                parents[i] = population[index2];
            }
        }

        return parents;
    }

    public double[][] crossover(double[][] parents) {
        double[][] offspring = new double[TAMANHO_DA_POPULACAO][TAMANHO_DO_CROMOSSOMO];

        int crossoverPoint = 0;

        for (int i = 0; i < TAMANHO_DA_POPULACAO; i++) {
            System.arraycopy(parents[i % 2], 0, offspring[i], 0, crossoverPoint);
            System.arraycopy(parents[(i + 1) % 2], crossoverPoint, offspring[i], crossoverPoint, TAMANHO_DO_CROMOSSOMO - crossoverPoint);
        }

        return offspring;
    }

    public void mutate(double[][] population) {
        Random random = new Random();

        for (int i = 0; i < TAMANHO_DA_POPULACAO; i++) {
            for (int j = 0; j < TAMANHO_DO_CROMOSSOMO; j++) {
                if (random.nextDouble() < TAXA_DE_MUTACAO) {
                    population[i][j] += random.nextDouble() * 0.1;
                }
            }
        }
    }

    public double[][] replacePopulation(double[][] population, double[] fitnessValues, double[][] offspring) {

        for (int i = 0; i < TAMANHO_DA_POPULACAO; i++) {
            int worstIndex = getWorstIndex(fitnessValues);
            if (fitnessValues[i] > fitnessValues[worstIndex]) {
                population[worstIndex] = offspring[i];
                fitnessValues[worstIndex] = fitnessValues[i];
            }
        }

        return population;
    }

    public double[] getBestIndividual(double[][] population, double[] fitnessValues) {
        int bestIndex = getBestIndex(fitnessValues);
        return population[bestIndex];
    }

    private static int getBestIndex(double[] values) {
        int bestIndex = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] > values[bestIndex]) {
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    private static int getWorstIndex(double[] values) {
        int worstIndex = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] < values[worstIndex]) {
                worstIndex = i;
            }
        }
        return worstIndex;
    }
}
