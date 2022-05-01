package com.company;

import java.util.Arrays;

public class NelderMid {
    int n, k, m = 1; //длина ребра симплекса
    double eps = 0.1; //точность
    double b=2.8; //параметр растяжения
    double al=0.4; //параметр сжатия

    double fh, fs, fl;
    Coordinates xh = new Coordinates(), xs = new Coordinates(), xl = new Coordinates();
    Coordinates xcl;
    Coordinates x[];
    double fx[];
    Coordinates x3;
    double fx3;
    int countIt = 0;

    public NelderMid(int Size)
    {
        n = Size;
        xcl = new Coordinates(n);
        x = new Coordinates[n+1];
    }

    public void Start()
    {
        System.out.println("\nNELDER MID method");
        for (int i = 0; i < x.length; i++)
        {
            x[i] = new Coordinates(n);
        }

        //-Вычисляем приращения----------------------------------
        double sig1 = ((Math.sqrt(n+1)-1)/(n*Math.sqrt(2)))*m;
        double sig2 = ((Math.sqrt(n+1) + n - 1)/(n*Math.sqrt(2)))*m;
        //System.out.println("sig1 =" + sig1 + " sig2 = " + sig2);

        //---2 этап--------Начальный многогранник---------------------
        x = Simplex.Simplex(x, sig1, sig2);
        /*for (int i = 0; i < x.length; i++)
        {
            System.out.println("Симплекс " + x[i].toString());
        }*/
        fx = new double[x.length];
        for (int i = 0; i < x.length; i++)
        {
            fx[i] = function.Calculate(x[i]);
        }
        Stage3();
    }
    void Stage3()
    {
        countIt++;
        double f[] = Arrays.copyOf(fx, fx.length);
        Arrays.sort(f);
        fh = f[f.length - 1];
        fs = f[f.length - 2];
        fl = f[0];
        //System.out.println("fs = " + fs + " fh = " + fh + " fl = " + fl );

        //----Сопоставляем точки с функциями---------------------
        for(int i = 0; i < x.length; i++)
        {
            if(fs == function.Calculate(x[i]))
                xs = x[i];
            if(fl == function.Calculate(x[i]))
                xl = x[i];
            if(fh == function.Calculate(x[i]))
            {
                xh = x[i];
                k = i;
            }
        }
        //-4 этап-----------Определяем центр тяжести-----------------------
        xcl = new Coordinates(n);
        for(int i = 0; i < x.length; i++)
        {
            if(i != k)
            {
                xcl = Coordinates.add(xcl, x[i]);
            }
        }
        xcl = xcl.multiply((double)1/n);
        //System.out.println("xcl = " + xcl.toString());

        //-5 этап--------Отражаем вершину относительно центра тяжести---------
        x3 = Coordinates.add(xcl.multiply(2), x[k].multiply(-1));
        //System.out.println("x3 = " + x3.toString());
        fx3 = function.Calculate(x3);
        //System.out.println("fx3 = " + fx3);

        //-6 этап-------------------------------------------------
        if(fx3 < fx[k])
        {
            //("Операция отражения закончилась успешно!");
            x[k] = x3;
            fx[k] = function.Calculate(x3);
            Stage7();

        }
        else
        {
            //System.out.println("Операция отражения закончилась НЕ успешно!");
            Stage9();
        }
    }
    void Stage7()
    {
        if(fx[k] < fl)
        {
            //System.out.println("Выполняем операцию растяжения!");
            x3 = Coordinates.add(xcl, Coordinates.add(x[k], xcl.multiply(-1)).multiply(b));
            fx3 = function.Calculate(x3);
            //---Stage8-------------
            if(fx3 < fx[k])
            {
                //System.out.println("Операция растяжения закончилась успешно!");
                x[k] = x3;
                fx[k] = function.Calculate(x3);
                Stage12();
            }
            else Stage9();
        }
        else Stage9();
    }

    void Stage9()
    {
        if(fs < fx3 && fx3 < fh)
        {
            Compression();
            /*System.out.println("выполняем сжатие симплекса");
            x3 = Coordinates.add(xcl, Coordinates.add(x[k], xcl.multiply(-1)).multiply(al));
            System.out.println("x3 = " + x3.toString());
            fx3 = function.Calculate(x3);
            System.out.println("fx3 = " + String.format("%.3f", fx3));
            Stage10();*/
        }
        else
            Stage11();
    }

    void Compression()
    {
        //System.out.println("выполняем сжатие симплекса");
        x3 = Coordinates.add(xcl, Coordinates.add(x[k], xcl.multiply(-1)).multiply(al));
        //System.out.println("x3 = " + x3.toString());
        fx3 = function.Calculate(x3);
        //("fx3 = " + String.format("%.3f", fx3));
        Stage10();
    }

    void Stage10()
    {
        if(fx[k] < fx3)
        {
            //System.out.println("Операция сжатия удачна");
            x[k] = x3;
            fx[k] = fx3;
            Stage12();
        }
        else
            Stage11();
    }
    void Stage11()
    {
        //System.out.println("Операция редукции!");
        int r = 0;
        for(int i = 1; i < x.length; i++)
        {
            if(function.Calculate(x[r]) > function.Calculate(x[i]))
            {
                r = i;
            }
        }
        for(int i = 0; i < x.length; i++)
        {
            if(i != r)
            {
                x[i] = Coordinates.add(x[r], Coordinates.add(x[i], x[r].multiply(-1)).multiply(0.5));
            }
        }
        Stage12();
    }
    void Stage12()
    {
        //-----определяем центр тяжести---------
        for(int i = 0; i < x.length; i++)
        {
            xcl = new Coordinates(n);
            xcl = Coordinates.add(xcl, x[i]);
            fx[i] = function.Calculate(x[i]);
        }
        xcl.multiply((double)1/(n+1));
        //System.out.println("xc = " + xcl.toString());
        double fxc = function.Calculate(xcl);
        //System.out.println("fxc = " + fxc);
        double result =  0;
        for(int i = 0; i < n; i++)
        {
            result += (Math.pow((fx[i] - fxc), 2));
        }
        result = Math.sqrt(((double)1 / (n + 1)) * result);
        //System.out.println("result = " + String.format("%.3f", result));
        if(result > eps)
        {
            //System.out.println("Итерация продолжается");
            Stage3();
        }
        else
        {
            Arrays.sort(fx);
            for(int i = 0; i < x.length; i++)
            {
                if(fx[0] == function.Calculate(x[i]))
                {
                    //System.out.println("f(x*) = " + fx[0]);
                    System.out.println("Количество итераций " + countIt);
                    System.out.println("Минимальная точка = " + x[i].toString());
                    return;
                }
            }
        }
    }
}
