import dataset.ProblemInstance;
import io.Encoder;
import solver.RandomSearch;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("Step 1: Loading problem instance...");
        ProblemInstance instance = null;
        try {
            instance = new Encoder("/Users/sina/Downloads/bet-sum18.xml").getProblemInstance();
            System.out.println("Problem instance loaded successfully!");
            instance.printStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Step 2: Initializing algorithm...");
        RandomSearch randomSearch = new RandomSearch(instance, -1, 10000000);
        System.out.println("Algorithm initialized successfully!");
        System.out.println("Step 3: Running algorithm...");
        randomSearch.run();
    }
}