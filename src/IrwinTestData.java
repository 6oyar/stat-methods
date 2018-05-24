public class IrwinTestData {
    private static double[] p;
    static {
        p = new double[]{
                0,
                0,
                1.68,
                1.70,
                1.64,
                1.60,
                1.55,
                1.51,
                1.47,
                1.44,
                1.42,
                1.39,
                1.37,
                1.35,
                1.33
        };

    }

    //alpha = 0.05
    public static double getCriticalValue(int n) {
        return p[n - 1];
    }

    //alpha = 0.05
    public static double getApproxCriticalValue(int n) {
        return -229.21 * Math.pow(n, -3)
                + 422.39 * Math.pow(n, -2.5)
                - 320.96 * Math.pow(n, -2)
                + 124.594 * Math.pow(n, -1.5)
                - 26.15 * Math.pow(n, -1)
                + 4.799 * Math.pow(n, -0.5)
                + 0.7029;
    }
}
