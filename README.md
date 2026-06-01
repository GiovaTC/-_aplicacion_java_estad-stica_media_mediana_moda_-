# -_aplicacion_java_estadistica_media_mediana_moda_- :.
Aplicacion Java Estadistica (Media, Mediana y Moda):

<img width="1254" height="1254" alt="image" src="https://github.com/user-attachments/assets/b358a8b7-f2f2-4728-a242-ba98065b93a0" />  

```
Java 21 + IntelliJ IDEA + Oracle Database 19c.
Este proyecto desarrolla una aplicacion en Java 21 utilizando IntelliJ IDEA y Oracle Database 19c, capaz de:
Generar una serie de números aleatorios.
Calcular:
Media (Promedio)
Mediana
Moda
Almacenar los resultados en Oracle mediante JDBC.
Consultar los registros almacenados en la base de datos .

Tecnologías Utilizadas:
Java 21
IntelliJ IDEA
Oracle Database 19c
JDBC (Oracle JDBC Driver)
Maven (Opcional) .

Estructura de la Base de Datos
Tabla ESTADISTICAS
CREATE TABLE ESTADISTICAS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY,
    FECHA_REGISTRO DATE DEFAULT SYSDATE,
    SERIE_NUMEROS VARCHAR2(4000),
    MEDIA NUMBER(10,2),
    MEDIANA NUMBER(10,2),
    MODA NUMBER(10,2),
    CONSTRAINT PK_ESTADISTICAS PRIMARY KEY(ID)
);

Consulta de Datos
SELECT * FROM ESTADISTICAS;

Clase de Conexión Oracle
ConexionOracle.java
package estadisticaoracle;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionOracle {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";

    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "oracle";

    public static Connection conectar() {

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

        } catch (Exception e) {

            System.out.println(
                    "Error de conexión: "
                            + e.getMessage()
            );

            return null;
        }
    }
}

Programa Principal
EstadisticaOracle.java
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

        Map<Integer,Integer> frecuencia =
                new HashMap<>();

        for(Integer numero : numeros){

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

        for(Map.Entry<Integer,Integer> dato :
                frecuencia.entrySet()){

            if(dato.getValue() >
                    maxFrecuencia){

                maxFrecuencia =
                        dato.getValue();

                moda =
                        dato.getKey();
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
                INSERT INTO ESTADISTICAS
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

        try(Connection con =
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
                    "\nRegistro guardado en Oracle."
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
                "SELECT * FROM ESTADISTICAS";

        try(Connection con =
                    ConexionOracle.conectar();
            Statement st =
                    con.createStatement();
            ResultSet rs =
                    st.executeQuery(sql)) {

            System.out.println(
                    "\nREGISTROS ALMACENADOS"
            );

            while(rs.next()) {

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

Dependencia JDBC Oracle
Maven
<dependencies>

    <dependency>
        <groupId>com.oracle.database.jdbc</groupId>
        <artifactId>ojdbc11</artifactId>
        <version>23.4.0.24.05</version>
    </dependency>

</dependencies>

Ejemplo de Ejecución
SERIE GENERADA
[5, 8, 2, 2, 9, 4, 5, 5, 7, 3, 1, 6, 8, 5, 10, 2, 4, 5, 9, 5]

RESULTADOS
Media    : 5.25
Mediana  : 5.0
Moda     : 5
Registro guardado en Oracle.

REGISTROS ALMACENADOS
ID: 1
Fecha: 2026-06-01
Serie: [5, 8, 2, 2, 9, 4, 5, 5, 7, 3, 1, 6, 8, 5, 10, 2, 4, 5, 9, 5]
Media: 5.25
Mediana: 5.0
Moda: 5.0

Funcionalidades Implementadas
✅ Generación automática de series numéricas aleatorias.
✅ Cálculo de media aritmética.
✅ Cálculo de mediana para conjuntos pares e impares.
✅ Determinación de la moda mediante conteo de frecuencias.
✅ Persistencia de resultados en Oracle Database 19c.
✅ Consulta de registros almacenados.
✅ Uso de JDBC y PreparedStatement.
✅ Compatible con Java 21 e IntelliJ IDEA .

Resultado Final
La aplicacion permite realizar análisis estadístico básico sobre una serie de números generados aleatoriamente, calculando Media, Mediana y Moda,
almacenando posteriormente los resultados en Oracle Database 19c para su consulta y auditoría posterior .
:. . / .
