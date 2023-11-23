import java.util.Random;

public class GeneticAlgoritm {

    //Construtor para injetar a classe de Algoritmo Genetico na classe Main
    public GeneticAlgoritm() {
    }

    public double[][] initializePopulation() {
        double[][] population = new double[Constants.POPULATION_SIZE][Constants.CHROMOSOME_SIZE];
        Random random = new Random();

        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            for (int j = 0; j < Constants.CHROMOSOME_SIZE; j++) {
                population[i][j] = random.nextDouble() * 2 - 1; // Random weights between -1 and 1
            }
        }

        return population;
    }

    public double[] evaluateFitness(double[][] population, Perceptron perceptron,
                                            double[][] trainingPopulation, double[] trainingPopulationClass) {
        double[] fitnessValues = new double[Constants.POPULATION_SIZE];

        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {

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
        double[][] parents = new double[2][Constants.CHROMOSOME_SIZE];

        for (int i = 0; i < 2; i++) {
            int index1 = new Random().nextInt(Constants.POPULATION_SIZE);
            int index2 = new Random().nextInt(Constants.POPULATION_SIZE);

            if (fitnessValues[index1] > fitnessValues[index2]) {
                parents[i] = population[index1];
            } else {
                parents[i] = population[index2];
            }
        }

        return parents;
    }

    public double[][] selectParentsBetterFitness(double[][] population, double[] fitnessValues) {
        double[][] parents = new double[2][Constants.CHROMOSOME_SIZE];

        int fitnessValueParent0Index = 0;
        int fitnessValueParent1Index = 0;
        for (int i = 0; i < fitnessValues.length; i++) {
            if (i == 0) {
                fitnessValueParent0Index = i;
            } else {
                if(fitnessValues[i] >= fitnessValues[fitnessValueParent0Index]) {
                    fitnessValueParent1Index = fitnessValueParent0Index;
                    fitnessValueParent0Index = i;
                }
            }
        }
        System.out.println(fitnessValues[fitnessValueParent0Index]);
        System.out.println(fitnessValues[fitnessValueParent1Index]);
        parents[0] = population[fitnessValueParent0Index];
        parents[1] = population[fitnessValueParent1Index];

        return parents;
    }

    public double[][] crossover(double[][] parents) {
        double[][] offspring = new double[Constants.POPULATION_SIZE][Constants.CHROMOSOME_SIZE];
        Random random = new Random();

        int crossoverPoint = random.nextInt(Constants.CHROMOSOME_SIZE);

        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            System.arraycopy(parents[i % 2], 0, offspring[i], 0, crossoverPoint);
            System.arraycopy(parents[(i + 1) % 2], crossoverPoint, offspring[i], crossoverPoint, Constants.CHROMOSOME_SIZE - crossoverPoint);
        }

        return offspring;
    }

    public void mutate(double[][] population) {
        Random random = new Random();

        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
            for (int j = 0; j < Constants.CHROMOSOME_SIZE; j++) {
                if (random.nextDouble() < Constants.MUTATION_RATE) {
                    population[i][j] += random.nextDouble() * 0.1;
                }
            }
        }
    }

    public double[][] replacePopulation(double[][] population, double[] fitnessValues, double[][] offspring) {

        for (int i = 0; i < Constants.POPULATION_SIZE; i++) {
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
