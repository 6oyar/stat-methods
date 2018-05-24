import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class StatHelper {

    /**
     * =====================================Выбросы======================================
     * */

    /**
     * Оценки лямбда по методу Ирвина
     * */
    public static double[] getIrwinLambdas(double[] data) {
        double standardDeviation = getStandardDeviation(data);
        double[] lambdas = new double[data.length];
        lambdas[0] = 0.0;

        for (int i = 1; i < data.length; i++) {
            double lambda = data[i] - data[i - 1];
            lambda = Math.abs(lambda) / standardDeviation;
            lambdas[i] = lambda;
        }
        return lambdas;
    }

    //https://math.semestr.ru/trend/irvin.php
    public static double[] getIrwinCorrectedData(double[] data) {
        double mean = getMeanValue(data);
        double deviation = getStandardDeviation(data);
        double[] u = new double[data.length];
        double[] lambdas = getIrwinLambdas(data);
        double lambdaCrit = IrwinTestData.getApproxCriticalValue(data.length);
        u[0] = data[0];
        //u[data.length - 1] = data[data.length - 1];
        boolean hasCritLambdas = false;
        for (int i = 1; i < u.length; i++) {
            if (lambdas[i] > lambdaCrit) {
                hasCritLambdas = true;
                System.out.println("Обнаружено аномальное значение: " + data[i] + ", на уровне ряда с номером " + i);
                System.out.println("Среднее значение ряда: " + mean);
                System.out.println("Стандартное отклонение ряда: " + deviation);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Если хотите скорректировать аномальное значение, введите \"y\", иначе нажмите enter...");

                try {
                    if (reader.readLine().equals("y")) {
                        u[i] = (data[i - 1] + data[i + 1] ) / 2;
                    } else {
                        u[i] = data[i];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                u[i] = data[i];
            }
        }

        if (hasCritLambdas) {
            System.out.println("Значения уровней ряда после корректировки:");

            for (int i = 0; i < u.length; i++) {
                System.out.print(u[i] + " ");
            }
            System.out.println();
            System.out.println();
        } else {
            System.out.println("Аномальные значения отсутствуют");
            System.out.println();
        }



        return u;
    }

    /**
     * =====================================Сглаживание======================================
     * */

    //Скользящая средння https://math.semestr.ru/trend/smoothing.php
    public static double[] simpleMovingAverage(double[] data, int interval) {
        int startIndex = interval / 2;
        double[] u = new double[data.length];
        for (int i = startIndex; i < data.length - startIndex; i++) {
            double sum = 0.0;
            for (int j = i - (interval / 2); j <= i + (interval / 2); j++) {
                sum += data[j];
            }
            u[i] = sum / interval;
        }

//        for (int i = 0; i < interval / 2; i++) {
//            u[i] = data[i];
//            u[data.length - 1 - i] = data[data.length - 1 - i];
//        }

        System.out.println("Сглаженный ряд:");

        for (int i = 0; i < u.length; i++) {

            if ( (i < interval / 2) || ( (data.length - 1 - i) < interval / 2) ) {
                System.out.print(" * ");
            } else {
                System.out.print(u[i] + " ");
            }
        }

        //Вычисление точности
        double sum = 0;
        for (int i = interval / 2; i < data.length - interval / 2; i++) {
            sum += Math.abs(data[i] - u[i]) * 100.0 / data[i];
        }
        sum = 1.0/(data.length - interval + 1) * sum;

        System.out.println();
        printAccuracyResult(sum);

        return u;
    }

//    //Взвешенная скользящая средння https://math.semestr.ru/trend/smoothing.php
//    public static double[] weightedMovingAverage(double[] data, int interval) {
//        int startIndex = interval / 2;
//        double[] u = new double[data.length];
//        for (int i = startIndex; i < data.length - startIndex; i++) {
//            double sum = 2 * data[i];
//            for (int j = 1; j <= interval / 2; j++) {
//                sum += data[i - j];
//            }
//            for (int j = 1; j <= interval / 2; j++) {
//                sum += data[i + j];
//            }
//
//            u[i] = sum / (interval + 1);
//        }
//
//
//        return u;
//    }

    //Взвешенная скользящая средння https://math.semestr.ru/trend/smoothing.php
    public static double[] weightedMovingAverage2(double[] data, int interval) {
        int startIndex = interval / 2;
        double[] weights;
        if (interval == 3) {
            weights = new double[]{2.0, 1.0};
            for (int i = 0; i < weights.length; i++) {
                weights[i] = weights[i] / 4;
            }
        } else if (interval == 5) {
            weights = new double[]{17.0, 12.0, -3.0};
            for (int i = 0; i < weights.length; i++) {
                weights[i] = weights[i] / 35;
            }
        } else if (interval == 7) {
            weights = new double[]{7, 6, 3, -2};
            for (int i = 0; i < weights.length; i++) {
                weights[i] = weights[i] / 21;
            }
        } else {
            //интервал == 9
            weights = new double[]{59, 54, 39, 14, -21};
            for (int i = 0; i < weights.length; i++) {
                weights[i] = weights[i] / 231;
            }
        }

        double[] u = new double[data.length];
        for (int i = startIndex; i < data.length - startIndex; i++) {
            double sum = weights[0] * data[i];
            for (int j = 1; j <= interval / 2; j++) {
                sum += data[i - j] * weights[j];
            }
            for (int j = 1; j <= interval / 2; j++) {
                sum += data[i + j] * weights[j];
            }

            u[i] = sum;
        }

        for (int i = 0; i < interval / 2; i++) {
            u[i] = data[i];
            u[data.length - 1 - i] = data[data.length - 1 - i];
        }

        System.out.println("Сглаженный ряд:");

        for (int i = 0; i < u.length; i++) {

            if ( (i < interval / 2) || ( (data.length - 1 - i) < interval / 2) ) {
                System.out.print(" * ");
            } else {
                System.out.print(u[i] + " ");
            }
        }

        //Вычисление точности
        double sum = 0;
        for (int i = interval / 2; i < data.length - interval / 2; i++) {
            sum += Math.abs(data[i] - u[i]) * 100.0 / data[i];
        }
        sum = 1.0/(data.length - interval + 1) * sum;

        System.out.println();
        printAccuracyResult(sum);

        return u;
    }


    public static double[] exponentialSmoothing(double []data) {
        double alpha = 2.0 / (data.length);
        double[] u = new double[data.length];

        System.out.println("Выберите способ определения U0:");
        System.out.println("1. Среднее арифметическое ряда");
        System.out.println("2. Первое значение ряда");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            if (reader.readLine().equals("1")) {
                u[0] = getMeanValue(data);
            } else {
                u[0] = data[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //u0 считаем как среднее арифметическое
        //u[0] = getMeanValue(data);
        //u[0] = data[0];

        for (int i = 1; i < data.length; i++) {
            u[i] = alpha * data[i - 1] + (1 - alpha) * u[i - 1];
        }

        System.out.println("Сглаженный ряд:");

        for (int i = 0; i < u.length; i++) {
                System.out.print(u[i] + " ");
        }


        //Вычисление точности
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += Math.abs(data[i] - u[i]) * 100.0 / data[i];
        }
        sum = 1.0/data.length * sum;

        System.out.println();
        printAccuracyResult(sum);

        return u;
    }



    /**
     * ========================================Тренд=========================================
     * */
    //Сравнение среднихх уровней ряда
    public static boolean comparisonOfTwoMeans(double[] data) throws ArithmeticException {
        double[] data1 = new double[data.length / 2];
        double[] data2 = new double[data.length - data.length / 2];
        System.arraycopy(data, 0, data1, 0, data.length / 2);
        System.arraycopy(data, data.length / 2, data2, 0, data.length - data.length / 2);

        double mean1 = getMeanValue(data1);
        double mean2 = getMeanValue(data2);
        double deviation1 = 0.0;
        double deviation2 = 0.0;

        for (int i = 0; i < data1.length; i++) {
            deviation1 += (data1[i] - mean1) * (data1[i] - mean1);
        }

        for (int i = 0; i < data2.length; i++) {
            deviation2 += (data2[i] - mean2) * (data2[i] - mean2);
        }

        deviation1 = deviation1 / data1.length;
        deviation2 = deviation2 / data2.length;

        if (!fischerTest(data1, data2)) {
            //System.out.println("Дисперсии выборок не равны, метод не применим");
            throw new ArithmeticException("Дисперсии не равны, метод не применим");

        }

        double t;
        int n1 = data1.length;
        int n2 = data2.length;
        t = Math.sqrt( n1 * n2 * (n1 + n2 - 2) / (n1 + n2));
        t *= Math.abs(mean1 - mean2) / Math.sqrt( (n1 - 1)*deviation1 + (n2 - 1) * deviation2 );
        double tCritical = StudentTestData.getCriticalValue(data.length - 2);

        return t > tCritical;

    }

    //Метод серий, основанный на медиане выборки https://math.semestr.ru/trend/median.php
    //https://www.youtube.com/watch?v=vq5qIcfiMr4
    public static boolean seriesOnMedian(double[] data)  {
        double[] rangedData = data.clone();
        Arrays.sort(rangedData);
        double median;
        if (rangedData.length % 2 == 0) {
            median = (rangedData[rangedData.length / 2] + rangedData[rangedData.length / 2 - 1]) / 2;
        } else {
            median = rangedData[rangedData.length / 2];
        }

        ArrayList<Integer> series = new ArrayList<>(data.length);
        for (int i = 0; i < data.length; i++) {
            if (data[i] > median) {
                series.add(+1);
            } else {
                series.add(-1);
            }
        }

        int[] params = getVandT(series);
        int v = params[0];
        int t = params[1];

        //alpha == 0.05
        int tCrit = (int) (3.3 * (Math.log10(data.length) + 1));
        int vCrit = (int) (0.5 * (data.length + 1 - 1.96 * Math.sqrt(data.length - 1)));

        boolean result = (tCrit > t) && (vCrit < v);

        return !result;

    }

    //Метод восходящих/нисходящих серий  https://math.semestr.ru/trend/series.php
    public static boolean seriesOnAscDesc(double[] data)  {
        ArrayList<Integer> series = new ArrayList<>();
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] == data[i + 1]) {
                if (data[i] > 0) series.add(1);
                else series.add(-1);
            } else if (data[i + 1] > data[i]) series.add(1);
            else if (data[i + 1] < data[i]) series.add(-1);
        }

        if (data[data.length - 1] > 0) series.add(1);
        else series.add(-1);

        int[] params = getVandT(series);
        int v = params[0];
        int t = params[1];

        //alpha == 0.05
        int tCrit;
        if (data.length < 26) {
            tCrit = 5;
        } else if (data.length >= 26 && data.length < 157) {
            tCrit = 6;
        } else {
            tCrit = 7;
        }
        int vCrit = (int) (1/3 * (2 * data.length - 1) - 1.96 * Math.sqrt((16 * data.length - 29) / 90));

        return !(t < tCrit) && (v > vCrit);
    }


    /**
     * ======================================HELPERS========================================
     * */

    /**
     * Стандартное отклонение
     * */
    public static double getStandardDeviation(double[] data) {
        double meanValue = getMeanValue(data);
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += (data[i] - meanValue) * (data[i] - meanValue);
        }
        return Math.sqrt( sum / (data.length - 1) );
    }

    /**
     * Среднее значение
     * */
    public static double getMeanValue(double[] data) {
        double sum = 0.0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        return sum / data.length;
    }

    /**
     * @return   дисперсии равны (H0 - true)
     * */
    public static boolean fischerTest(double [] data1, double [] data2) {

        return true;
    }

    private static int[] getVandT(ArrayList<Integer> series) {
        int flag = series.get(0);
        int countOfSeries = 1;
        int currentSeriesLength = 1;
        int maxSeriesLength = currentSeriesLength;
        for (int i = 1; i < series.size(); i++) {
            if (!series.get(i).equals(flag)) {
                countOfSeries++;
                if (maxSeriesLength < currentSeriesLength) {
                    maxSeriesLength = currentSeriesLength;
                }
                currentSeriesLength = 1;

                if (flag == 1) {
                    flag = -1;
                } else {
                    flag = 1;
                }
            } else {
                currentSeriesLength++;
            }
        }
        if (maxSeriesLength < currentSeriesLength) {
            maxSeriesLength = currentSeriesLength;
        }

        return new int[]{countOfSeries, maxSeriesLength};
    }

    private static void printAccuracyResult(double a) {
        //String formattedA = String.format("%.2f", a);
        String formattedA = new DecimalFormat("#0.00").format(a);
        System.out.print("Точность ");
        if (a < 10.0) {
            System.out.print("высокая");
        } else if (a >= 10.0 && a < 20.0) {
            System.out.print("хорошая");
        } else if (a >= 20.0 && a < 50.0) {
            System.out.print("удовлетворительная");
        } else {
            System.out.print("неудовлетворительная");
        }

        System.out.println(", с относительной ошибкой: \u03b5 \u2248 " + formattedA + "%");
    }
}
