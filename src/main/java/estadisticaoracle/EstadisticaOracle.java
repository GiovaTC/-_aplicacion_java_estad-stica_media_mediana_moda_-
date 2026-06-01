package estadisticaoracle;

import java.sql.*;
import java.util.*;

public class EstadisticaOracle {
    public static void main(String[] args) {

        List<Integer> numeros = generarSerie();

        System.out.println("\nSERIE GENERADA");
        System.out.println(numeros);

        double media = calcularMedia(numeros);
        double mediana = calcularMediana(numeros);
        int moda = calcularModa(numeros);

        System.out.println("\nRESULTADOS");
        System.out.println("Media    : " + media);
        System.out.println("Mediana  : " + mediana);
        System.out.println("Moda     : " + moda);

        guardarEnOracle(
                numeros,
                media,
                mediana,
                moda
        );

        consultarDatos();
    }

    private static void consultarDatos() {
    }

    private static void guardarEnOracle(List<Integer> numeros, double media, double mediana, int moda) {
        
    }

    private static int calcularModa(List<Integer> numeros) {
        return 0;
    }

    private static double calcularMediana(List<Integer> numeros) {
        return 0;
    }

    private static List<Integer> generarSerie(){

        Random random = new Random();

        List<Integer> numeros =
                new ArrayList<>();

        for (int i = 0; i < 20; i++){

            numeros.add(
                    random.nextInt(10) + 1
            );
        }

        return numeros;
    }

    private static double calcularMedia(
            List<Integer> numeros) {

        int suma = 0;

        for (Integer numero : numeros) {
            suma += numero;
        }

        return (double) suma /
                numeros.size();
    }

    
}