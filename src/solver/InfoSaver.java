package solver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class InfoSaver {
    private final String instanceName;
    private final int DTF;
    private final int nfe;
    private final int secondsToFeasibility;
    private final int NfeToFeasibility;
    private final int[][] nfeToDTF;

    /**
     * Constructor: initialize InfoSaver with instance name and hillClimbing object
     */
    public InfoSaver(String instanceName, int DTF, int nfe, int secondsToFeasibility, int nfeToFeasibility, int[][] nfeToDTF) {
        this.instanceName = instanceName;
        this.DTF = DTF;
        this.nfe = nfe;
        this.secondsToFeasibility = secondsToFeasibility;
        this.NfeToFeasibility = nfeToFeasibility;
        this.nfeToDTF = nfeToDTF;
    }

    /**
     * Generate a unique txt file name based on instance name and current Unix timestamp
     */
    private String getTxtName() {
        long unixTimestamp = Instant.now().getEpochSecond();
        String safeInstance = instanceName
                .replace(" ", "_")
                .replace(":", "-")
                .replace("/", "-");
        return String.format("Info_%s_%d.txt", safeInstance, unixTimestamp);
    }

    /**
     * Save the collected information into a txt file inside ./Info folder
     */
    public void saveToFile() {
        try {
            // Create ./Info directory if not exists
            File outputDir = new File("./Info");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Create output file
            File outputFile = new File(outputDir, getTxtName());
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            // Write scalar values
            writer.write(DTF + "\n");
            writer.write(nfe + "\n");
            writer.write(secondsToFeasibility + "\n");
            writer.write(NfeToFeasibility + "\n");

            // Write 2D array values
            for (int[] row : nfeToDTF) {
                for (int j = 0; j < row.length; j++) {
                    writer.write(row[j] + (j == row.length - 1 ? "" : " "));
                }
                writer.newLine();
            }

            writer.close();
            System.out.println("Data saved to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

