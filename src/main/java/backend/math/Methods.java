package backend.math;

import backend.CalculatedData;

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
                    array[i][j] = (array[i-1][j+1] - array[i-1][j]) / (x[i+j] - x[j]);

                }
            }

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

    public static class NewtonPolynomialWithEquidistantPointsForwards extends Options {
        public double getPolynomialSum(double variableX, double[] x, double[] y) {
            int size = x.length;
            double step = x[1] - x[0];
            double t = (variableX - x[0]) / step;

            //  массив неразделенных сумм (по количеству узлов)
            double[][] array = getTable(size, y);

            //  вычисление значения полинома в точке
            double result = 0;
            for(int i = 0; i < size; i++) {
                result += array[i][0] * getMultiple(i, t) / getFactorial(i);
            }
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
            double[][] array = getTable(size, y);

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
                array[i][j] = array[i-1][j+1] - array[i-1][j];
            }
        }

        return array;
    }

    public static int getFactorial(int f) {
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result = result * i;
        }
        return result;
    }

}
