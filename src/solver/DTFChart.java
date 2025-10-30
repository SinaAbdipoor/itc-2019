package solver;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.time.Instant;

public class DTFChart {
    private final int[][] nfeToDTF;
    private final String instanceName;

    public DTFChart(int[][] nfeToDTF, String instanceName) {
        this.nfeToDTF = nfeToDTF;
        this.instanceName = instanceName;
    }

    public void plotter() {
        // --- High resolution ---
        int width = 1600, height = 1200;
        int padding = 100;
        int pointSize = 8;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // --- Anti-aliasing & quality rendering ---
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // --- Background: white ---
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // --- Calculate axis boundaries ---
        int minNfe = Integer.MAX_VALUE, maxNfe = Integer.MIN_VALUE;
        int maxDtf = Integer.MIN_VALUE;
        for (int[] point : nfeToDTF) {
            minNfe = Math.min(minNfe, point[0]);
            maxNfe = Math.max(maxNfe, point[0]);
            maxDtf = Math.max(maxDtf, point[1]);
        }
        int minDtf = 0; // Force Y-axis to start at 0

        double scaleX = (double) (width - 2 * padding) / (maxNfe - minNfe);
        double scaleY = (double) (height - 2 * padding) / (maxDtf - minDtf);

        // --- Grid lines ---
        g2d.setColor(new Color(220, 220, 220));
        int gridLines = 10;
        for (int i = 0; i <= gridLines; i++) {
            // Vertical lines
            int x = padding + i * (width - 2 * padding) / gridLines;
            g2d.drawLine(x, padding, x, height - padding);

            // Horizontal lines
            int y = padding + i * (height - 2 * padding) / gridLines;
            g2d.drawLine(padding, y, width - padding, y);
        }

        // --- Axes ---
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2d.drawLine(padding, padding, padding, height - padding); // Y-axis

        // --- Axis ticks ---
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));

        // X-axis ticks
        int xTicks = 10;
        for (int i = 0; i <= xTicks; i++) {
            int nfe = minNfe + i * (maxNfe - minNfe) / xTicks;
            int x = padding + (int) ((nfe - minNfe) * scaleX);
            g2d.drawLine(x, height - padding, x, height - padding + 10);
            g2d.drawString(String.valueOf(nfe), x - 20, height - padding + 35);
        }

        // Y-axis ticks
        int yTicks = 10;
        for (int i = 0; i <= yTicks; i++) {
            int dtf = minDtf + i * (maxDtf - minDtf) / yTicks;
            int y = height - padding - (int) ((dtf - minDtf) * scaleY);
            g2d.drawLine(padding - 10, y, padding, y);
            g2d.drawString(String.valueOf(dtf), padding - 60, y + 7);
        }

        // --- Scatter points with connecting lines ---
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.RED);
        int prevX = -1, prevY = -1;
        for (int[] point : nfeToDTF) {
            int x = padding + (int) ((point[0] - minNfe) * scaleX);
            int y = height - padding - (int) ((point[1] - minDtf) * scaleY);

            // Draw point
            g2d.fillOval(x - pointSize / 2, y - pointSize / 2, pointSize, pointSize);

            // Draw connecting line
            if (prevX != -1) {
                g2d.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
        }

        // --- Axis labels ---
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(Color.BLACK);

        // X-axis label (centered, below)
        g2d.drawString("NFE", width / 2 - 30, height - padding + 80);

        // Y-axis label (rotated, centered, shifted farther)
        AffineTransform orig = g2d.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        g2d.setTransform(at);
        g2d.drawString("DTF", -height / 2 - 30, padding - 75);
        g2d.setTransform(orig);

        // --- Title ---
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.drawString("DTF vs NFE - " + instanceName, width / 2 - 200, padding - 40);

        // --- Legend ---
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.setColor(Color.RED);
        g2d.fillRect(width - padding - 150, padding - 40, 25, 25);
        g2d.setColor(Color.BLACK);
        g2d.drawString("DTF curve", width - padding - 110, padding - 20);

        // --- Save image ---
        File outputDir = new File("./Chart");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        String fileName = getChartName();

        File outputFile = new File(outputDir, fileName);
        try {
            ImageIO.write(image, "PNG", outputFile);
            System.out.println("Chart saved to: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g2d.dispose();
        }
    }

    private String getChartName() {
        long unixTimestamp = Instant.now().getEpochSecond();
        String safeInstance = instanceName
                .replace(" ", "_")
                .replace(":", "-")
                .replace("/", "-");
        return String.format("NTD_%s_%d.png", safeInstance, unixTimestamp);
    }
}
