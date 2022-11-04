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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author danie
 */
public class ClosestPair {

    static long[] tiempos = new long[10];
    static long[] comps = new long[10];
    static long[] tiempos2 = new long[10];
    static long[] comps2 = new long[10];
    static long sum = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        create("brute");
        create("divide");
        //realiza 10 repeticiones para cada caso variando tamano
        for (int repeticiones = 0; repeticiones < 8; repeticiones++) {
            int tam =(int) Math.pow(4,repeticiones+1);
            //genera n coordenadas aleatorias en la funcion create
            ArrayList <Punto> coordenadas = create(tam);
            //llamado a bruteforce para calcular distancia minima
            bruteforce(coordenadas, repeticiones, 0);
            //guarda tiempo de ejecucion de la repeticion
            tiempos[repeticiones] = sum;
            sum = 0;
            //organiza las coordenadas
            coordenadas.sort(Comparator.comparing(Punto->Punto.x));
            //llama subrutina encargada de separar matriz y calcular distancia minima
            //DAC(coordenadas, repeticiones,1000000);
            //guarda tiempo de ejecucion
            tiempos2[repeticiones] = sum;
            sum = 0;
            System.out.println(repeticiones);
        }
        try {
            PrintWriter out = new PrintWriter("brute.txt");
            for (int i = 0; i < tiempos.length; ++i) {
                int s = (int) Math.pow(4, i+1);
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
                int s = (int) Math.pow(4, i+1);
                //Escribir cada tiempo de ejecucion en el archivo
                out.println(s + " " + tiempos2[i] + " " + comps2[i]);
            }
            out.close();	// closes the output stream
        } catch (FileNotFoundException err) {
            // complains if file does not exist
            err.printStackTrace();
        }
    }

    //funcion que retorna un ArrayList con n coordenadas aleatorias
    public static ArrayList<Punto> create(int n) {
        ArrayList <Punto> creados= new ArrayList<Punto>();
        //ciclo para generar n coordenadas
        for (int i = 0; i < n; i++) {
            Random randomGenerator = new Random();
            Punto p = new Punto(0,0);
            //asigna las coordenadas a los puntos
            Punto.x = randomGenerator.nextInt(n);
            Punto.y = randomGenerator.nextInt(n);
            creados.add(p);
        }
        return creados;
    }

    //funcion que calcula la distancia minima entre dos puntos i y j
    public static double distancias(Punto i,Punto j) {
        //retorna la distancia
        return Math.sqrt(Math.pow(i.x - j.x, 2) + Math.pow(i.y - j.y, 2));
    }

    //algoritmo bruteforce para encontrar distancias minimas entre todos los puntos
    public static double bruteforce(List<Punto> coordenadas, int index,int ca) {
        //inicializar una distancia minima
        double d_min = 10000000;
        double d = 0;
        int comp = 0;
        //ciclos anidados para recorrer las coordenadas
        for(int i=0;i<coordenadas.size();i++){
            for(int j=i+1;j<coordenadas.size();j++){
                //calcula distancia entre las dos coordenadas
                d=distancias(coordenadas.get(i),coordenadas.get(j));
                //aumenta el numero de comparaciones
                comp++;
                long startTime = System.nanoTime();
                if (d < d_min) {
                    //asigna la distancia calculada a la variable con la distancia menor
                    d_min = d;
                }
                long finalTime = System.nanoTime() - startTime;
                sum += finalTime;
                j++;
            }
        }
        if(ca==0){
            comps[index]+=comp;
        }else{
            comps2[index] += comp;
        }
        //retorna la distancia minima encontrada
        return d_min;
    }

    public static double DAC(List<Punto> coordenadas, int r,double d) {
        double dmin=d;
        //busca el elemento de la mitad
        int pos=coordenadas.size()/2;
        if(coordenadas.size()%2==1){
            pos+=1;
        }
        //en caso de que hayan mas de 3 coordenadas se subdivide
        if(coordenadas.size()>3){
            //se crean listas para ambas mitades
            List<Punto> p1 = coordenadas.subList(0, pos);
            List<Punto> p2 = coordenadas.subList(pos, coordenadas.size());
            //se encuentra la distancia minima entre las dos sublistas
            dmin=Math.min(DAC(p1,r,d),DAC(p2,r,d));
            //se encuentra la distancia minima en comparacion a las coordenadas de la mitad
            dmin=Math.min(dmin,distancias(p1.get(p1.size()-1),p2.get(0)));
        }
        else
        {
            //realiza bruteforce y encuentra minimo entre las distancias encontradas
            dmin = Math.min(dmin,bruteforce(coordenadas, r,1));
        }
        return dmin;
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
