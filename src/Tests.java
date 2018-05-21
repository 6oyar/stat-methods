import javax.swing.text.Document;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Tests {

    private static double[] readAndGetArray() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Double> list = new ArrayList<>();
        double[] array;
        try {
            while (true) {
                double num = Double.parseDouble(reader.readLine());
                list.add(num);
            }
        } catch (NumberFormatException e) {
            if (list.size() > 0) {
                array = new double[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i);
                }
                return array;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void userTest() throws IOException {
        System.out.println("Введите временной ряд, каждый уровень ряда с новой строки:");
        double[] data = readAndGetArray();
        System.out.println("Введите \"y\", если хотите проверить временной ряд на выбросы и скорректировать его, иначе нажмите любую клавишу...");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (reader.readLine().equals("y")) {
            data = StatHelper.getIrwinCorrectedData(data);
            System.out.println("Значения уровней ряда после корректировки:");

            for (int i = 0; i < data.length; i++) {
                System.out.print(data[i] + " ");
            }
            System.out.println();
        }

        System.out.println("Для проведения сглаживания временного ряда нажмите соответствующую методу цифровую клавишу:");
        System.out.println("1. Метод простой скользящей средней");
        System.out.println("2. Метод взвешенной скользящей средней");
        System.out.println("3. Метод экспоненциального сглаживания");

        String request = reader.readLine();

        double[] means = new double[data.length];
        if (request.equals("1") || request.equals("2")) {
            System.out.println("Введите длину сглаживаемого интервала (3, 5, 7 или 9)");
            int m = Integer.parseInt(reader.readLine());

            if (request.equals("1")) {
                means = StatHelper.simpleMovingAverage(data, m);
            } else {
                means = StatHelper.weightedMovingAverage2(data, m);
            }
        } else {
            means = StatHelper.exponentialSmoothing(data);
        }

        System.out.println("Для проверки гипотезы о наличии тренда нажмите соответствующую методу цифровую клавишу:");
        System.out.println("1. Метод сравнения средних уровней ряда");
        System.out.println("2. Метод серий, основанный на медиане выборки");
        System.out.println("3. Метод восходящих/нисходящих серий");

        request = reader.readLine();
        boolean result;

        if (request.equals("1")) {
            result = StatHelper.comparisonOfTwoMeans(means);
        } else if (request.equals("2")) {
            result = StatHelper.seriesOnMedian(means);
        } else {
            result = StatHelper.seriesOnAscDesc(means);
        }

        if (result) {
            System.out.println("Тренд присутствует");
        } else {
            System.out.println("Тренд отсутствует");
        }
    }

    public static void testAll(){
//        irwinTest();
//        simpleMovingAverageTest();
//        weightedMovingAverageTest();
//        exponentialSmoothingTest();
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
        StatHelper.exponentialSmoothing(b);
        //System.out.println(c);
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
        double[] b = StatHelper.weightedMovingAverage2(a,3);
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i]);
        }
        System.out.println("==========================================================================");
    }

    //https://math.semestr.ru/trend/irvin.php
    private static void irwinTest() {
        System.out.println("================================irwinTest=================================");
        double[] irwinArr = new double[]{115,112,118,122,115,121,126,132,134,131};
        double[] a = StatHelper.getIrwinCorrectedData(irwinArr);

        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
        System.out.println("==========================================================================");
    }
}
