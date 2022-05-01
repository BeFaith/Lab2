package com.company;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static void main(String[] args)
    {
        NelderMid methodNM = new NelderMid(function.Size);

        long startTime = System.nanoTime();
        methodNM.Start();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000;
        System.out.println("Время выполнения " + duration + " микросекунд");

        startTime = System.nanoTime();
        GradientDescent.Start();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000;
        System.out.println("Время выполнения " + duration + " микросекунд");
    }
}

class Coordinates
{
    public double coord[];
    public Coordinates()
    {
    }
    public Coordinates(double[] x)
    {
        coord = x;
    }
    public Coordinates(int size)
    {
        coord = new double[size];
    }

    public String toString()
    {
        String str = "(";
        for(int i = 0; i < coord.length; i++)
        {
            str += coord[i] + " ";
        }
        str += ")";
        return str;
    }

    static public Coordinates add(Coordinates x, Coordinates y)
    {
        Coordinates result = new Coordinates(x.coord.length);
        if(x.coord.length == y.coord.length)
        {
            for (int i = 0; i < x.coord.length; i++)
            {
                result.coord[i] = x.coord[i] + y.coord[i];
            }
        }
        return result;
    }

    static public Coordinates add(Coordinates x, double y)
    {
        Coordinates result = new Coordinates(x.coord.length);
        for (int i = 0; i < x.coord.length; i++)
        {
            result.coord[i] = x.coord[i] + y;
        }
        return result;
    }

    static public Coordinates add(Coordinates x, Coordinates y, Coordinates z)
    {
        Coordinates result = new Coordinates(x.coord.length);
        result = Coordinates.add(x, Coordinates.add(y, z));
        return result;
    }

    public Coordinates multiply(double y)
    {
        Coordinates result = new Coordinates();
        result.coord = Arrays.copyOf(coord, coord.length);
        for(int i = 0; i < coord.length; i++)
        {
            result.coord[i] = coord[i] * y;
        }
        return result;
    }
}

class Simplex
{
    public static Coordinates[] Simplex(Coordinates x[], double sig1, double sig2)
    {
        Coordinates result[] = x;
        double s1, s2;
        for(int i = 1; i < x.length; i++)
        {
            for(int j = 0; j < result[i].coord.length; j++)
            {
                if(i != j+1)
                    result[i].coord[j] = x[0].coord[j] + sig2;
                else
                    result[i].coord[j] = x[0].coord[j] + sig1;
            }
        }
        return result;
    }
}

class function
{
    public static double Calculate(Coordinates x)
    {
        return Math.pow((x.coord[0] - 1), 2) + Math.pow((x.coord[1] - 2), 2)+ Math.pow((x.coord[2] - 3), 2);
        //return Math.pow(x.coord[0], 2) - x.coord[0]*x.coord[1] + 3*Math.pow(x.coord[1], 2) - x.coord[0];
    }
    public static int Size = 3;
}
