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

    private static List<Integer> generarSerie() {

        Random random = new Random();

        List<Integer> numeros =
                new ArrayList<>();

        for (int i = 0; i < 20; i++) {

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

    private static double calcularMediana(
            List<Integer> numeros) {

        Collections.sort(numeros);

        int tamaño = numeros.size();

        if (tamaño % 2 == 0) {

            return (numeros.get(
                    tamaño / 2 - 1)
                    +
                    numeros.get(
                            tamaño / 2))
                    / 2.0;
        }

        return numeros.get(
                tamaño / 2);
    }

    private static int calcularModa(
            List<Integer> numeros) {

        Map<Integer, Integer> frecuencia =
                new HashMap<>();

        for (Integer numero : numeros) {

            frecuencia.put(
                    numero,
                    frecuencia.getOrDefault(
                            numero,
                            0
                    ) + 1
            );
        }

        int moda = numeros.get(0);
        int maxFrecuencia = 0;

        for (Map.Entry<Integer, Integer> dato :
                frecuencia.entrySet()) {

            if (dato.getValue() >
                    maxFrecuencia) {

                maxFrecuencia =
                        dato.getValue();

                moda = dato.getKey();
            }
        }

        return moda;
    }

    private static void guardarEnOracle(
            List<Integer> numeros,
            double media,
            double mediana,
            int moda) {

        String sql = """
                INSERT INTO ESTADISTICAS_R
                (
                    SERIE_NUMEROS,
                    MEDIA,
                    MEDIANA,
                    MODA
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;
        try (Connection con =
                     ConexionOracle.conectar();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {
            ps.setString(
                    1,
                    numeros.toString()
            );

            ps.setDouble(
                    2,
                    media
            );

            ps.setDouble(
                    3,
                    mediana
            );

            ps.setInt(
                    4,
                    moda
            );

            ps.executeUpdate();

            System.out.println(
                    "\nRegistro guardado en oracle! "
            );

        } catch (Exception e) {

            System.out.println(
                    "Error guardando: "
                            + e.getMessage()
            );
        }
    }

    private static void consultarDatos() {

        String sql =
                "SELECT * FROM ESTADISTICAS_R";

        try (Connection con =
                     ConexionOracle.conectar();
             Statement st =
                     con.createStatement();
             ResultSet rs =
                     st.executeQuery(sql)) {

            System.out.println(
                    "\nREGISTROS ALMACENADOS"
            );

            while (rs.next()) {

                System.out.println(
                        "\nID: "
                                + rs.getInt("ID")
                );

                System.out.println(
                        "Fecha: "
                                + rs.getDate(
                                "FECHA_REGISTRO")
                );

                System.out.println(
                        "Serie: "
                                + rs.getString(
                                "SERIE_NUMEROS")
                );

                System.out.println(
                        "Media: "
                                + rs.getDouble(
                                "MEDIA")
                );

                System.out.println(
                        "Mediana: "
                                + rs.getDouble(
                                "MEDIANA")
                );

                System.out.println(
                        "Moda: "
                                + rs.getDouble(
                                "MODA")
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error consultando: "
                            + e.getMessage()
            );
        }   
    }
}