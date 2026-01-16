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
//                instance = new Encoder("/Users/mingxuan/OR/itc/xml/nbi-spr18.xml").getProblemInstance();
                System.out.println("Problem instance loaded successfully!");
                instance.printStats();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Step 2: Initializing algorithm...");
            int maxSeconds = Integer.parseInt(args[1]);
//            int maxSeconds = 500;
//            RandomSearch randomSearch = new RandomSearch(instance, maxSeconds, -1);
//            HillClimbing hillClimbing = new HillClimbing(instance, maxSeconds, -1);
//            HillClimbingWithRS hillClimbingWithRS = new HillClimbingWithRS(instance, maxSeconds, -1);
            HillClimbingWithRS_3 hillClimbingWithRS_3 = new HillClimbingWithRS_3(instance, maxSeconds, -1);
            System.out.println("Algorithm initialized successfully!");
            System.out.println("Step 3: Running algorithm...");
//            Timetable timetable = randomSearch.run();
//            Timetable timetable = hillClimbing.run();
            Timetable timetable = hillClimbingWithRS_3.run();
//            Solution solution = new Solution(instance.instanceName(), maxSeconds, 4, randomSearch.name, "Mingxuan", "Beijing University of Posts and Telecommunications", "China", timetable);
//            Solution solution = new Solution(instance.instanceName(), maxSeconds, 4, hillClimbing.name, "Mingxuan", "Beijing University of Posts and Telecommunications", "China", timetable);
            Solution solution = new Solution(instance.instanceName(), maxSeconds, 4, hillClimbingWithRS_3.name, "Mingxuan", "Queen Mary University of London", "China", timetable);
            System.out.println("DTF: " + solution.timetable().isFeasible(instance) +
                    ", TimePenalty: " + solution.timetable().calcTimePenalty() +
                    ", RoomPenalty: " + solution.timetable().calcRoomPenalty() +
                    ", DistributionPenalty: " + solution.timetable().calcDistributionPenalty(instance));
            new Decoder(solution);

//            int DTF = hillClimbingWithRS.getDTF();
//            int nfe = hillClimbingWithRS.getNfe();
//            int secondsToFeasibility = hillClimbingWithRS.getSecondsToFeasibility();
//            int nfeToFeasibility = hillClimbingWithRS.getNfeToFeasibility();
//            int[][] nfeToDTF = hillClimbingWithRS.getNfeToDTF();
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