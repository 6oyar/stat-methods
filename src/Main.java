public class Main {
    public static void main(String[] args) {
        double[] a = new double[]{50,56,46,48,49,46,48,47,47,49};
        double[] a1 = new double[]{1,20,1,2,1,2,1,2,1,2};
        double[] b = new double[]{2.99,2.66,2.63,2.56,2.40,2.22,1.97,1.72,1.56,1.42};
        double[] irwinArr = new double[]{115,112,118,122,115,121,156,132,134,131};
        //double c = StatHelper.exponentialSmoothing(b);
        double[] d = new double[]{1065,851,531,922,1095,986,822,1137,1301,1038,780,1435,1593,1658,1363,1737,1719,1521,1049,1790,2016};
        double[] out1 = StatHelper.simpleMovingAverage(d,3);
        double[] out2 = StatHelper.weightedMovingAverage(d,3);

        double[] sss = StatHelper.getIrwinCorrectedData(irwinArr);

        for (int i = 0; i < sss.length; i++) {
            System.out.println(sss[i]);
        }
    }
}
