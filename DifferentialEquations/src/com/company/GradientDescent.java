package com.company;

public class GradientDescent
{
    public static void Start()
    {
        double tolerance = 0.1;//1e-6;
        double alpha = 0.4;
        double[] x = new double[function.Size];
        //Первоначальные предположения о местоположении минимумов
        x[0] = 0.1;
        x[1] = -1;
        //x[2] = 1;
        System.out.println("\nSTEEPEST DESCENT method");
        steepestDescent(x, alpha, tolerance);

        System.out.println("Минимальная точка = " + new Coordinates(x).toString());
    }

    public static void steepestDescent(double[] x, double alpha, double tolerance)
    {
        int countIt = 0;
        int n = x.length;
        double h = 1e-6;  //Коэффициент допуска
        double g0 = function.Calculate(new Coordinates(x)); //Первоначальная оценка результата

        //Вычислить начальный градиент
        double[] fi = new double[n];
        fi = GradG(x, h);

        //Вычислить начальную норму
        double DelG = 0;
        for (int i = 0; i < n; ++i)
            DelG += fi[i] * fi[i];
        DelG = Math.sqrt(DelG);

        double b = alpha / DelG;

        //Повторяйте до тех пор, пока значение не станет <= предел допуска
        while (DelG > tolerance)
        {
            countIt++;
            //Вычислить следующее значение
            for (int i = 0; i < n; ++i)
                x[i] -= b * fi[i];
            h /= 2;

            //Вычислить следующий градиент
            fi = GradG(x, h);

            //Рассчитайте следующую норму
            DelG = 0;
            for (int i = 0; i < n; ++i)
                DelG += fi[i] * fi[i];
            DelG = Math.sqrt(DelG);

            b = alpha / DelG;

            //Проверка значения заданной функции с текущими значениями
            double g1 = function.Calculate(new Coordinates(x));

            //Настройка параметра
            if (g1 > g0) alpha /= 2;
            else g0 = g1;
        }
        System.out.println("Количество итераций " + countIt);
    }

    // Обеспечивает приблизительный расчет градиента g(x).
    public static double[] GradG(double[] x, double h)
    {
        int n = x.length;
        double[] z = new double[n];
        double[] y = (double[])x.clone();
        double g0 = function.Calculate(new Coordinates(x));
        for (int i = 0; i < n; ++i)
        {
            y[i] += h;
            z[i] = (function.Calculate(new Coordinates(y)) - g0) / h;
        }
        return z;
    }
}