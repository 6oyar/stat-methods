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
}
