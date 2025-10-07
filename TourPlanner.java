import java.util.*;

public class TourPlanner {

    private Graph graph;
    private int hotelIndex;

    public TourPlanner(Graph graph, int hotelIndex) {
        this.graph = graph;
        this.hotelIndex = hotelIndex;
    }

    // DP ile turu bul, dönen yol landmark indexlerinden oluşur
    public TourResult findBestTour() {
        int n = graph.n;
        double[][] dp = new double[1 << n][n];
        int[][] parent = new int[1 << n][n];

        for (double[] row : dp) Arrays.fill(row, -1e9);
        for (int[] row : parent) Arrays.fill(row, -1);

        dp[1 << hotelIndex][hotelIndex] = 0;

        for (int mask = 0; mask < (1 << n); mask++) {
            for (int u = 0; u < n; u++) {
                if (dp[mask][u] < -1e8) continue;
                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) != 0) continue;
                    if (graph.baseScore[u][v] < 0) continue;

                    double adjustedScore = graph.getAdjustedScore(u, v);
                    double newScore = dp[mask][u] + adjustedScore;

                    if (newScore > dp[mask | (1 << v)][v]) {
                        dp[mask | (1 << v)][v] = newScore;
                        parent[mask | (1 << v)][v] = u;
                    }
                }
            }
        }

        double bestScore = -1e9;
        int bestLast = -1;
        int fullMask = (1 << n) - 1;

        for (int u = 0; u < n; u++) {
            if (u == hotelIndex) continue;
            if (dp[fullMask][u] < -1e8) continue;
            if (graph.baseScore[u][hotelIndex] < 0) continue;

            double adjustedScore = graph.getAdjustedScore(u, hotelIndex);
            double totalScore = dp[fullMask][u] + adjustedScore;

            if (totalScore > bestScore) {
                bestScore = totalScore;
                bestLast = u;
            }
        }

        if (bestLast == -1) {
            // Tur bulunamadı, sadece otel
            List<Integer> onlyHotel = new ArrayList<>();
            onlyHotel.add(hotelIndex);
            onlyHotel.add(hotelIndex);
            return new TourResult(onlyHotel, 0, 0);
        }

        // Yolu geri çıkar
        List<Integer> path = new ArrayList<>();
        int mask = fullMask;
        int pos = bestLast;
        path.add(hotelIndex); // dönüşte otel

        while (pos != hotelIndex) {
            path.add(pos);
            int prev = parent[mask][pos];
            mask = mask ^ (1 << pos);
            pos = prev;
        }
        path.add(hotelIndex); // başlangıç otel
        Collections.reverse(path);

        // Toplam travel time hesapla
        int totalTravelTime = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalTravelTime += graph.travelTime[path.get(i)][path.get(i + 1)];
        }

        return new TourResult(path, bestScore, totalTravelTime);
    }
}

class TourResult {
    public List<Integer> path;
    public double totalScore;
    public int totalTravelTime;

    public TourResult(List<Integer> path, double totalScore, int totalTravelTime) {
        this.path = path;
        this.totalScore = totalScore;
        this.totalTravelTime = totalTravelTime;
    }
}
