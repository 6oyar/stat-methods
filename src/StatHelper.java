public class StatHelper {
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
        double[] u = new double[data.length];
        double[] lambdas = getIrwinLambdas(data);
        double lambdaCrit = IrwinTestData.getCriticalValue(data.length);
        u[0] = data[0];
        u[data.length - 1] = data[data.length - 1];
        for (int i = 1; i < u.length - 1; i++) {
            if (lambdas[i] > lambdaCrit) {
                u[i] = (data[i - 1] + data[i + 1] ) / 2;
            } else {
                u[i] = data[i];
            }
        }
        return u;
    }

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
        return u;
    }

    //Взвешенная скользящая средння https://math.semestr.ru/trend/smoothing.php
    public static double[] weightedMovingAverage(double[] data, int interval) {
        int startIndex = interval / 2;
        double[] u = new double[data.length];
        for (int i = startIndex; i < data.length - startIndex; i++) {
            double sum = 2 * data[i];
            for (int j = 1; j <= interval / 2; j++) {
                sum += data[i - j];
            }
            for (int j = 1; j <= interval / 2; j++) {
                sum += data[i + j];
            }

            u[i] = sum / (interval + 1);
        }


        return u;
    }

    //Сравнение среднихх уровней ряда
    public static void comparisonOfTwoMeans(double[] data) {
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

        if (!fischerTest()) {
            System.out.println("Дисперсии выборок не равны, метод не применим");
            return;
        }

        double t;
        int n1 = data1.length;
        int n2 = data2.length;
        t = Math.sqrt( n1 * n2 * (n1 + n2 - 2) / (n1 + n2));
        t *= Math.abs(mean1 - mean2) / Math.sqrt( (n1 - 1)*deviation1 + (n2 - 1) * deviation2 );
        double tCritical = StudentTestData.getCriticalValue(data.length - 2);
        if (t > tCritical) {
            System.out.println("Тенденция существует");
        } else {
            System.out.println("Тенденция отсутствует");
        }

    }

    /**
     * @return   дисперсии равны (H0 - true)
     * */
    public static boolean fischerTest() {

        return true;
    }

    public static double exponentialSmoothing(double []data) {
        double alpha = 2.0 / (data.length + 1);
        double[] u = new double[data.length + 1];

        //u0 считаем как среднее арифметическое
        u[0] = getMeanValue(data);

        for (int i = 1; i < data.length + 1; i++) {
            u[i] = alpha * data[i - 1] + (1 - alpha) * u[i - 1];
        }
        
        //Вычисление точности
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += Math.abs(data[i] - u[i]) * 100 / data[i];
        }
        sum = 1.0/data.length * sum;
        printAccuracyResult(sum);

        return u[data.length];
    }

    private static void printAccuracyResult(double a) {
        if (a < 10.0) {
            System.out.println("Точность прогноза высокая, с относительной ошибкой: " + a + "%");
        } else if (a >= 10.0 && a < 20.0) {
            System.out.println("Точность прогноза хорошая, с относительной ошибкой: " + a + "%");
        } else if (a >= 20.0 && a < 50.0) {
            System.out.println("Точность прогноза удовлетворительная, с относительной ошибкой: " + a + "%");
        } else {
            System.out.println("Точность прогноза неудовлетворительная, с относительной ошибкой: " + a + "%");
        }
    }

    
}
