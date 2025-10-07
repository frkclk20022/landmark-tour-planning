import java.io.*;
import java.util.*;

public class Main {

    // Dosya isimleri sabit, klasörün içine koyarsan direkt bu isimle arar
    private static final String LANDMARK_MAP_FILE = "landmark_map_data.txt";
    private static final String PERSONAL_INTEREST_FILE = "personal_interest.txt";
    private static final String VISITOR_LOAD_FILE = "visitor_load.txt";

    public static void main(String[] args) throws IOException {

        // landmark_map_data.txt -> kenarlar + landmark isimleri seti
        Set<String> landmarkSet = new HashSet<>();
        List<String[]> edgesRaw = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LANDMARK_MAP_FILE))) {
            String line = br.readLine(); // Başlık satırını atla
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 4) continue;
                String from = parts[0];
                String to = parts[1];
                landmarkSet.add(from);
                landmarkSet.add(to);
                edgesRaw.add(parts);
            }
        }

        List<String> landmarkList = new ArrayList<>(landmarkSet);
        Collections.sort(landmarkList);

        // Landmark nesnelerini oluştur
        List<Landmark> landmarks = new ArrayList<>();
        for (String lmName : landmarkList) {
            landmarks.add(new Landmark(lmName));
        }

        // personal_interest.txt oku
        try (BufferedReader br = new BufferedReader(new FileReader(PERSONAL_INTEREST_FILE))) {
            String line = br.readLine(); // Başlık satırını atla
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 2) continue;
                String name = parts[0];
                double pi = Double.parseDouble(parts[1]);
                int idx = landmarkList.indexOf(name);
                if (idx != -1) {
                    landmarks.get(idx).personalInterest = pi;
                }
            }
        }

        // visitor_load.txt oku
        try (BufferedReader br = new BufferedReader(new FileReader(VISITOR_LOAD_FILE))) {
            String line = br.readLine(); // Başlık satırını atla
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 2) continue;
                String name = parts[0];
                double vl = Double.parseDouble(parts[1]);
                int idx = landmarkList.indexOf(name);
                if (idx != -1) {
                    landmarks.get(idx).visitorLoad = vl;
                }
            }
        }

        Graph graph = new Graph(edgesRaw, landmarks);

        int hotelIndex = graph.landmarkToIndex.get("Hotel");

        System.out.println("Please enter the total number of landmarks (including Hotel): " + graph.n);
        System.out.println("\nThree input files are read.");
        System.out.println("\nThe tour planning is now processing…\n");

        long startTime = System.currentTimeMillis();

        TourPlanner planner = new TourPlanner(graph, hotelIndex);
        TourResult result = planner.findBestTour();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        printResult(result, landmarkList, duration);
    }

    private static void printResult(TourResult result, List<String> landmarkList, long durationMs) {
        System.out.println("The visited landmarks:");
        int order = 1;
        for (int idx : result.path) {
            System.out.println(order + "-" + landmarkList.get(idx));
            order++;
        }
        System.out.printf("\nTotal attractiveness score: %.2f\n", result.totalScore);
        System.out.println("Total travel time: " + result.totalTravelTime + " min.");

        long seconds = durationMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        System.out.printf("\nProgram took %d minutes and %d seconds to run.\n", minutes, seconds);
    }
}
