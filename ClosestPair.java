/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package closestpair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author danie
 */
public class ClosestPair {

    static long[] tiempos = new long[100];
    static long[] comps = new long[100];
    static long[] tiempos2 = new long[100];
    static long[] comps2 = new long[100];
    static int sum = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        create("brute");
        create("divide");
        //realiza 100 repeticiones para cada caso variando tamano
        for (int repeticiones = 0; repeticiones < 1; repeticiones++) {
            //genera n coordenadas aleatorias en la funcion create
            int cords[][] = create(6 * (repeticiones + 1));
            //llamado a bruteforce para calcular distancia minima
            System.out.println(bruteforce(cords, repeticiones, 0, 0, cords.length - 1));
            //guarda tiempo de ejecucion de la repeticion
            tiempos[repeticiones] = sum;
            sum = 0;
            show(cords);
            //organiza la matriz para realizar la separacion
            bubbleSort(cords);
            show(cords);
            //llama subrutina encargada de separar matriz y calcular distancia minima
            DAC(cords, repeticiones);
            //guarda tiempo de ejecucion
            tiempos2[repeticiones] = sum;
            sum = 0;
        }
        /*
        try {
            PrintWriter out = new PrintWriter("brute.txt");
            for (int i = 0; i < tiempos.length; ++i) {
                int s = ((i + 1) * 6);
                //Escribir cada tiempo de ejecucion en el archivo
                out.println(s + " " + tiempos[i] + " " + comps[i]);
            }
            out.close();	// closes the output stream
        } catch (FileNotFoundException err) {
            // complains if file does not exist
            err.printStackTrace();
        }
        try {
            PrintWriter out = new PrintWriter("divide.txt");
            for (int i = 0; i < tiempos2.length; ++i) {
                int s = ((i + 1) * 6);
                //Escribir cada tiempo de ejecucion en el archivo
                out.println(s + " " + tiempos2[i] + " " + comps2[i]);
            }
            out.close();	// closes the output stream
        } catch (FileNotFoundException err) {
            // complains if file does not exist
            err.printStackTrace();
        }*/
    }

    //funcion que retorna una matriz con 6 coordenadas aleatorias
    public static int[][] create(int n) {
        //crea matriz de 6 columnas y 2 filas
        int coordenadas[][] = new int[n][2];
        //ciclo para generar 6 coordenadas
        for (int i = 0; i < n; i++) {
            Random randomGenerator = new Random();
            //asigna las coordenadas a las filas del punto
            coordenadas[i][0] = randomGenerator.nextInt(n);
            coordenadas[i][1] = randomGenerator.nextInt(n);
        }
        return coordenadas;
    }

    //funcion que calcula la distancia minima entre dos puntos i y j
    public static double distancias(int c[][], int i, int j) {
        //retorna la distancia
        return Math.sqrt(Math.pow(c[i][0] - c[j][0], 2) + Math.pow(c[i][1] - c[j][1], 2));
    }

    //algoritmo bruteforce para encontrar distancias minimas entre todos los puntos
    public static double bruteforce(int c[][], int index, int ca, int inicio, int fin) {
        //inicializar una distancia minima
        double d_min = 10000000;
        double d = 0;
        int comp = 0;
        //ciclos anidados para recorrer matriz y encontrar distancias minimas entre todos los puntos
        for (int i = inicio; i < fin - 1; i++) {
            for (int j = i + 1; j < fin; j++) {
                //llama funcion que calcula distancia minima entre dos puntos
                d = distancias(c, i, j);
                //compara si la distancia entre i y j es menor que la menor encontrada
                comp++;
                long startTime = System.nanoTime();
                if (d < d_min) {
                    //asigna la distancia calculada a la variable con la distancia menor
                    d_min = d;
                }
                long finalTime = System.nanoTime() - startTime;
                sum += finalTime;
            }
        }
        if (ca == 0) {
            comps[index] = comp;
        } else {
            comps2[index] = comp;
        }
        //retorna la distancia minima encontrada
        return d_min;
    }

    static void show(int m[][]) {
        for (int i = 0; i < m.length; i++) {
            System.out.println(m[i][0] + " " + m[i][1]);
        }
    }

    static void bubbleSort(int[][] mat) {
        //longitud de la matriz
        int n = mat.length;
        //variables temporales para el cambio entre elementos
        int temp = 0;
        int temp2 = 0;
        //ciclos anidados para recorrer matriz
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                //comparacion en coordenada x
                if (mat[j - 1][0] > mat[j][0]) {
                    //intercambia elementos  
                    temp = mat[j - 1][0];
                    temp2 = mat[j - 1][1];
                    mat[j - 1][0] = mat[j][0];
                    mat[j - 1][1] = mat[j][1];
                    mat[j][0] = temp;
                    mat[j][1] = temp2;
                }
            }
        }
    }

    public static void DAC(int m[][], int r) {
        //calcula posicion de la mitad
        int pos=(m.length-1)/2;
        //realiza bruteforce para la primera mitad de la matriz
        double d1 = bruteforce(m, r, 1, 0, pos);
        //realiza bruteforce para la segunda mitad de la matriz
        double d2 = bruteforce(m, r, 1, pos, m.length - 1);
        //encuentra distancia minima entre los dos puntos de la mitad
        double d3 = Math.sqrt(Math.pow(m[pos][0] - m[pos+1][0], 2) + Math.pow(m[pos][1] - m[pos+1][1], 2));
        //encuentra minimo entre las 3 distancias encontradas
        double d = Math.min(d1, d2);
        d = Math.min(d, d3);
        System.out.println(d);
    }

    private static void create(String r) // creates a file
    {
        try {
            // defines the filename
            String fname = (r + ".txt");
            // creates a new File object
            File f = new File(fname);

            // creates the new file
            f.createNewFile();

        } catch (IOException err) {
            // complains if there is an Input/Output Error
            err.printStackTrace();
        }
        return;
    }
}
