public class StudentTestData {
    private static double[] p;
    static {
        p = new double[]{
                12.70,
                4.303,
                3.182,
                2.776,
                2.571,
                2.447,
                2.365,
                2.306,
                2.262,
                2.228,
                2.201,
                2.179,
                2.160,
                2.145,
                2.131,
                2.120,
                2.110,
                2.101,
                2.093,
                2.086,
                2.080,
                2.074,
                2.069,
                2.064,
                2.060,
                2.056,
                2.052,
                2.049,
                2.045,
                2.042,
                2.040,
                2.037,
                2.035,
                2.032,
                2.030,
                2.028,
                2.026,
                2.024,
                2.023,
                2.021,
                2.020,
                2.018,
                2.017,
                2.015,
                2.014,
                2.013,
                2.012,
                2.011,
                2.010,
                2.009,
                2.008,
                2.007,
                2.006,
                2.005,
                2.004,
                2.003,
                2.002,
                2.002,
                2.001,
                2.000,
                2.000,
                1.999,
                1.998,
                1.998,
                1.997,
                1.997,
                1.996,
                1.995,
                1.995,
                1.994,
                1.994,
                1.993,
                1.993,
                1.993,
                1.992,
                1.992,
                1.991,
                1.991,
                1.990,
                1.990
        };

    }

    //alpha = 0.05
    public static double getCriticalValue(int n) {
        return p[n - 1];
    }
}
