package backend.math;

import backend.CalculatedData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static backend.Utils.*;
import static runnable.Main.*;

public class Methods {

    public static Options getOptionByNumber(int number) {
        switch (number) {
            case 1: {
                return lagrange;
            }
            case 2: {
                return newtonPolynomialWithNotEquidistantPoints;
            }
            case 3: {
                return newtonPolynomialWithEquidistantPointsForwards;
            }
            case 4: {
                return newtonPolynomialWithEquidistantPointsBackwards;
            }
            default: {
                exit("че-то с выбором функций блин", 1);
                return null;
            }
        }
    }

    public static CalculatedData getDataByNumber(int number) {
        switch (number) {
            case 1: {
                return lagrangeData;
            }
            case 2: {
                return newtonData;
            }
            case 3: {
                return newtonForwardsData;
            }
            case 4: {
                return newtonBackwardsData;
            }
            default: {
                exit("че-то с выбором функций блинб", 1);
                return null;
            }
        }
    }

    public static String getNameByNumber(int number) {
        switch (number) {
            case 1: {
                return "Лагранж";
            }
            case 2: {
                return "Ньютон";
            }
            case 3: {
                return "Ньютон (равнуд. узлы; вперед)";
            }
            case 4: {
                return "Ньютон (равнуд. узлы; назад)";
            }
            default: {
                exit("че-то с выбором функций блин", 1);
                return null;
            }
        }
    }

    public static class Lagrange extends Options {
        public double getPolynomialSum(double variableX, double[] x, double[] y) {
            double result = 0;
            for(int i = 0; i < x.length; i++) {
                result += y[i] * getMultiple(x, variableX, i);
            }
            return result;
        }

        /**  Метод для вычисления многочлена Лагранжа.
         * x -- массив значений координаты x из таблицы,
         * variableX -- точка, в которой нужно вычислить значение многочлена Лагранжа,
         * pointNumber -- позиция слагаемого в многочлене Лагранжа
         */
        private double getMultiple(double[] x, double variableX, int pointNumber) {
            int size = x.length;

            double result = 1;
            for(int i = 0; i < size; i++) {
                if (i != pointNumber) {
                    result *= (variableX - x[i]) / (x[pointNumber] - x[i]);
                }
            }
            return result;
        }
    }

    public static class NewtonPolynomialWithNotEquidistantPoints extends Options {
        public double getPolynomialSum(double variableX, double[] x, double[] y) {
            int size = x.length;

            //  массив неразделенных сумм (по количеству узлов)
            double[][] array = newtonData.getArray();

/*            System.out.println();

            for(int i = 0; i < size; i++) {
                System.out.println(Arrays.toString(array[i]));
            }

            System.out.println();*/

            //  вычисление значения полинома в точке
            double result = 0;
            for(int i = 0; i < size; i++) {
                result += array[i][0] * getMultiple(i, variableX, x);
            }
            return result;
        }

        /** Вычисление в многочлене Ньютона множителей, содержащих переменную x.
         * position -- позиция слагаемого в многочлене,
         * variableX -- значение переменной x,
         * x -- массив значений координаты x из таблицы.
         */
        private double getMultiple(int position, double variableX, double[] x) {
            double result = 1;
            for(int i = 0; i < position; i++) {
                result *= variableX - x[position - 1];
            }
            return result;
        }
    }

    public static double[][] getTable(int size, double[] x, double[] y) {
        double[][] array = new double[size][];

        //  создание и заполнение первого уровня значениями из таблицы
        array[0] = new double[size];
        for(int i = 0; i < size; i++) {
            array[0][i] = y[i];
        }

        //  создание остальных уровней
        for(int i = 1; i < size; i++) {
            array[i] = new double[size - i];
        }

        //  заполнение остальных уровней
        for(int i = 1; i < size; i++) {
            for(int j = 0; j < array[i].length; j++) {
                array[i][j] = (BigDecimal.valueOf(array[i-1][j+1]).subtract(BigDecimal.valueOf(array[i-1][j]))).divide(BigDecimal.valueOf(x[i+j]).subtract(BigDecimal.valueOf(x[j])), 2, RoundingMode.HALF_DOWN).doubleValue();
                //array[i][j] = (array[i-1][j+1] - array[i-1][j]) / (x[i+j] - x[j]);
            }
        }
        newtonData.setArray(array);
        return array;
    }

    public static class NewtonPolynomialWithEquidistantPointsForwards extends Options {
        public double getPolynomialSum(double variableX, double[] x, double[] y) {
            int size = x.length;
            double step = x[1] - x[0];
            double t = (variableX - x[0]) / step;

            //  массив неразделенных сумм (по количеству узлов)
            double[][] array = newtonForwardsData.getArray();

            //  вычисление значения полинома в точке
            double result = 0;
            for(int i = 0; i < size; i++) {
                result += array[i][0] * getMultiple(i, t) / getFactorial(i);
            }

/*            //  вычисление значения полинома в точке
            int newAIndex = 0;
            for (int i = 1; i < (size / 2 + 1); i++) {
                if (variableX < x[i]) {
                    newAIndex = i - 1;
                    break;
                }
            }
            t = (variableX - x[newAIndex]) / step;
            double result = 0;
            int counter = 0;
            for(int i = newAIndex; i < size; i++) {
                if (array[i].length < newAIndex + 1) break;
                result += array[i][newAIndex] * getMultiple(counter, t) / getFactorial(counter);
                counter++;
            }*/

            return result;
        }

        private double getMultiple(int position, double t) {
            double result = 1;
            for(int i = 0; i < position; i++) {
                result *= t - i;
            }
            return result;
        }
    }

    public static class NewtonPolynomialWithEquidistantPointsBackwards extends Options {

        public double getPolynomialSum(double variableX, double[] x, double[] y) {
            int size = x.length;
            double step = x[1] - x[0];
            double t = (variableX - x[size - 1]) / step;

            //  массив неразделенных сумм (по количеству узлов)
            double[][] array = newtonBackwardsData.getArray();

            //  вычисление значения полинома в точке
            double result = 0;
            for(int i = 0; i < size; i++) {
                result += array[i][(array[i]).length - 1] * getMultiple(i, t) / getFactorial(i);
            }
            return result;
        }

        private double getMultiple(int position, double t) {
            double result = 1;
            for(int i = 0; i < position; i++) {
                result *= t + i;
            }
            return result;
        }

    }

    public static double[][] getTable(int size, double[] y) {
        double[][] array = new double[size][];

        //  создание и заполнение первого уровня значениями из таблицы
        array[0] = new double[size];
/*        for(int i = 0; i < size; i++) {
            array[0][i] = y[i];
        }*/
        System.arraycopy(y, 0, array[0], 0, size);

        //  создание остальных уровней
        for(int i = 1; i < size; i++) {
            array[i] = new double[size - i];
        }

        //  заполнение остальных уровней
        for(int i = 1; i < size; i++) {
            for(int j = 0; j < array[i].length; j++) {
                array[i][j] = (BigDecimal.valueOf(array[i-1][j+1]).subtract(BigDecimal.valueOf(array[i-1][j]))).doubleValue();
                //array[i][j] = array[i-1][j+1] - array[i-1][j];
            }
        }

        newtonForwardsData.setArray(array);
        newtonBackwardsData.setArray(array);

        return array;
    }

    public static int getFactorial(int f) {
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result = result * i;
        }
        return result;
    }

    public static class GaussGreaterThanA {
        public static double getPolynomialSum(double variableX, double[] x, double[] y) {
            int size = x.length;
            double step = x[1] - x[0];
            int index;
            if (size % 2 == 1) {
                index = (size - 1) / 2;
            } else {
                index = size / 2 - 1;
            }
            double[][] array = newtonForwardsData.getArray();
            double t = (variableX - x[index]) / step;

            int counter = 0;
            double result = 0;
            for (int i = index; i > 0; i--) {
                double multipleOne = getMultiple(0, counter, t);
                double coeffOne = array[counter][i];
                counter++;
                double multipleTwo = getMultiple(0, counter, t);
                double coeffTwo = array[counter][i];
                String str = String.format("%.2f * %.2f + %.2f * %.2f", coeffOne, multipleOne, coeffTwo, multipleTwo);
                System.out.println(str);

                result += coeffOne * multipleOne + coeffTwo * multipleTwo;
                counter++;
            }
            result += array[counter][0] * getMultiple(0, counter, t);
            return result;
        }

        //  номер пары слагаемых
        private static double getMultiple(int call, int position, double t) {
            double result = 1;
            if (position == 0) return 1;
            for(int i = 1; i <= position; i++) {
                result *= (i % 2 == 0) ? (t - i / 2) : (t + (int) i / 2);
            }
            return result / getFactorial(position);
        }
    }

    public static class GaussLesserThanA {
        public static double getPolynomialSum(double variableX, double[] x, double[] y) {
            int size = x.length;
            double step = x[1] - x[0];
            int index;
            if (size % 2 == 1) {
                index = (size - 1) / 2;
            } else {
                index = size / 2 - 1;
            }
            double[][] array = newtonForwardsData.getArray();
            for(double[] i : array) {
                System.out.println(Arrays.toString(i));
            }
            double t = (variableX - x[index]) / step;

            int counter = 1;
            double result = array[0][index];
            for (int i = index - 1; i > 0; i--) {
                double coeffOne = array[counter][i];
                double multipleOne = getMultiple(1, counter, t);
                counter++;
                double coeffTwo = array[counter][i];
                double multipleTwo = getMultiple(2, counter, t);
                String str = String.format("%.2f * %.2f + %.2f * %.2f", coeffOne, multipleOne, coeffTwo, multipleTwo);
                System.out.println(str);
                result += coeffOne * multipleOne + coeffTwo * multipleTwo;
                counter++;
            }
            return result;
        }

        //  номер пары слагаемых
        private static double getMultiple(int call, int position, double t) {
            double result = 1;
            for(int i = 1; i <= position; i++) {
                result *= (i % 2 == 0) ? (t + i / 2) : (t - (int) i / 2);
            }
            return result / getFactorial(position);
        }
    }
}
