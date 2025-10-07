import java.util.*;

public class Graph {
    public int n; // landmark sayısı
    public Landmark[] landmarks;
    public double[][] baseScore;   // kenarların base score'u
    public int[][] travelTime;     // kenarların travel time'ı
    public Map<String, Integer> landmarkToIndex;

    public Graph(List<String[]> edgeList, List<Landmark> landmarkList) {
        this.n = landmarkList.size();
        this.landmarks = new Landmark[n];
        this.baseScore = new double[n][n];
        this.travelTime = new int[n][n];
        this.landmarkToIndex = new HashMap<>();

        for (int i = 0; i < n; i++) {
            landmarks[i] = landmarkList.get(i);
            landmarkToIndex.put(landmarks[i].name, i);
            Arrays.fill(baseScore[i], -1);
            Arrays.fill(travelTime[i], Integer.MAX_VALUE / 2);
        }

        // Kenar bilgilerini matrise koy
        for (String[] edge : edgeList) {
            int from = landmarkToIndex.get(edge[0]);
            int to = landmarkToIndex.get(edge[1]);
            double bScore = Double.parseDouble(edge[2]);
            int tTime = (int) Double.parseDouble(edge[3]);
            baseScore[from][to] = bScore;
            travelTime[from][to] = tTime;
        }
    }

    // Adjusted attractiveness score hesaplama (kenar u -> v)
    public double getAdjustedScore(int u, int v) {
        double decayFactor = 1 - landmarks[v].visitorLoad * 0.03 * travelTime[u][v];
        if (decayFactor < 0.1) decayFactor = 0.1;
        return baseScore[u][v] * landmarks[v].personalInterest * decayFactor;
    }
}
