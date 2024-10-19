import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {

    private static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }
    private static double lagrangeInterpolation(List<Point> points, double x) {
        double totalSum = 0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            Point pi = points.get(i);
            double yi = pi.y;
            double Li = 1.0;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    Li *= (x - points.get(j).x) / (pi.x - points.get(j).x);
                }
            }

            totalSum += yi * Li;
        }
        return totalSum;
    }

    static class Point {
        int x;
        double y;

        Point(int x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static double findConstantTerm(List<Point> points) {
        return lagrangeInterpolation(points, 0);
    }

    public static void main(String[] args) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject jsonObject = new JSONObject(content);

            int n = jsonObject.getJSONObject("keys").getInt("n");
            int k = jsonObject.getJSONObject("keys").getInt("k");

            List<Point> points = new ArrayList<>();

            for (String key : jsonObject.keySet()) {
                if (key.equals("keys")) continue;

                JSONObject root = jsonObject.getJSONObject(key);
                int base = root.getInt("base");
                String value = root.getString("value");
                int y = decodeValue(value, base);
                int x = Integer.parseInt(key);
                points.add(new Point(x, y));
            }

            if (points.size() < k) {
                throw new IllegalArgumentException("Not enough points to find the polynomial.");
            }

            double c = findConstantTerm(points);
            System.out.println("The constant term c is: " + c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
