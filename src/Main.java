import dataset.ProblemInstance;
import dataset.Timetable;
import io.Decoder;
import io.Encoder;
import solver.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Step 1: Loading problem instance...");
            ProblemInstance instance = null;
            try {
                instance = new Encoder(args[0]).getProblemInstance();
//                instance = new Encoder("/Users/mingxuan/OR/itc/itc-2019/src/Half1/agh-fis-spr17_postcompetition2.xml").getProblemInstance();
                System.out.println("Problem instance loaded successfully!");
                instance.printStats();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Step 2: Initializing algorithm...");
            int maxSeconds = Integer.parseInt(args[1]);
//            int maxSeconds = 100;
//            RandomSearch randomSearch = new RandomSearch(instance, maxSeconds, -1);
//            HillClimbing hillClimbing = new HillClimbing(instance, maxSeconds, -1);
            HillClimbingWithRS_2 hillClimbingWithRS_2 = new HillClimbingWithRS_2(instance, maxSeconds, -1);
//            FRLS frls = new FRLS(instance, maxSeconds, -1);
            System.out.println("Algorithm initialized successfully!");
            System.out.println("Step 3: Running algorithm...");
//            Timetable timetable = randomSearch.run();
//            Timetable timetable = hillClimbing.run();
            Timetable timetable = hillClimbingWithRS_2.run();
//            Solution solution = new Solution(instance.instanceName(), maxSeconds, 4, randomSearch.name, "Mingxuan", "Beijing University of Posts and Telecommunications", "China", timetable);
//            Solution solution = new Solution(instance.instanceName(), maxSeconds, 4, hillClimbing.name, "Mingxuan", "Beijing University of Posts and Telecommunications", "China", timetable);
            Solution solution = new Solution(instance.instanceName(), maxSeconds, 4, hillClimbingWithRS_2.name, "Mingxuan", "Queen Mary University of London", "China", timetable);
            System.out.println("DTF: " + solution.timetable().isFeasible(instance) +
                    ", TimePenalty: " + solution.timetable().calcTimePenalty() +
                    ", RoomPenalty: " + solution.timetable().calcRoomPenalty() +
                    ", DistributionPenalty: " + solution.timetable().calcDistributionPenalty(instance));
            new Decoder(solution);

//            int DTF = hillClimbingWithRS_2.getDTF();
//            int nfe = hillClimbingWithRS_2.getNfe();
//            int secondsToFeasibility = hillClimbingWithRS_2.getSecondsToFeasibility();
//            int nfeToFeasibility = hillClimbingWithRS_2.getNfeToFeasibility();
//            int[][] nfeToDTF = hillClimbingWithRS_2.getNfeToDTF();
//            int[][] nfeToDTF = randomSearch.getNfeToDTF();
//            InfoSaver infoSaver = new InfoSaver(instance.instanceName(), DTF, nfe, secondsToFeasibility, nfeToFeasibility, nfeToDTF);
//            infoSaver.saveToFile();
//            DTFChart chart = new DTFChart(nfeToDTF, instance.instanceName());
//            chart.plotter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}