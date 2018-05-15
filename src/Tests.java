public class Tests {
    public static void testAll(){
        irwinTest();
        simpleMovingAverageTest();
        weightedMovingAverageTest();
        exponentialSmoothingTest();
        comparisonOfTwoMeansTest();
    }

    //https://studme.org/62036/geografiya/metod_sravneniya_srednih_urovney_ryada
    private static void comparisonOfTwoMeansTest() {
        System.out.println("=========================comparisonOfTwoMeansTest==========================");
        double[] a = new double[]{1,2,1,2,1,2,1,2,1,2};
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        StatHelper.comparisonOfTwoMeans(a);
        System.out.println("===========================================================================");



        System.out.println("=========================comparisonOfTwoMeansTest==========================");
        a = new double[]{58,56,46,48,47,46,48,47,43,42};
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        StatHelper.comparisonOfTwoMeans(a);
        System.out.println("===========================================================================");
    }

    //http://www.ekonomika-st.ru/drugie/metodi/metodi-prognoz-1-4.html
    private static void exponentialSmoothingTest() {
        System.out.println("=========================exponentialSmoothingTest==========================");
        double[] b = new double[]{2.99,2.66,2.63,2.56,2.40,2.22,1.97,1.72,1.56,1.42};
        double c = StatHelper.exponentialSmoothing(b);
        System.out.println(c);
        System.out.println("===========================================================================");
    }
    //https://math.semestr.ru/trend/smoothing.php
    private static void simpleMovingAverageTest() {
        System.out.println("==========================simpleMovingAverageTest==========================");
        double[] a = new double[]{1065,851,531,922,1095,986,822,1137,1301,1038,780,1435,1593,1658,1363,1737,1719,1521,1049,1790,2016};
        double[] b = StatHelper.simpleMovingAverage(a,3);
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i]);
        }
        System.out.println("===========================================================================");
    }
    //https://math.semestr.ru/trend/smoothing.php
    private static void weightedMovingAverageTest() {
        System.out.println("=========================weightedMovingAverageTest=========================");
        double[] a = new double[]{1065,851,531,922,1095,986,822,1137,1301,1038,780,1435,1593,1658,1363,1737,1719,1521,1049,1790,2016};
        double[] b = StatHelper.weightedMovingAverage(a,3);
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i]);
        }
        System.out.println("==========================================================================");
    }

    //https://math.semestr.ru/trend/irvin.php
    private static void irwinTest() {
        System.out.println("================================irwinTest=================================");
        double[] irwinArr = new double[]{115,112,118,122,115,121,156,132,134,131};
        double[] a = StatHelper.getIrwinCorrectedData(irwinArr);

        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
        System.out.println("==========================================================================");
    }
}
