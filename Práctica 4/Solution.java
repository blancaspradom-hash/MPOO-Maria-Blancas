import java.io.*;
import java.util.*;

class Result {

    /*
     * Complete the 'detectarZonaAjuste' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts INTEGER_ARRAY vagones as parameter.
     */

    public static int[] detectarZonaAjuste(int[] vagones) {
        int n = vagones.length;
        int[] ordenados = vagones.clone();
        Arrays.sort(ordenados);

        int inicio = -1;
        int fin = -1;

        for (int i = 0; i < n; i++) {
            if (vagones[i] != ordenados[i]) {
                if (inicio == -1) {
                    inicio = i;
                }
                fin = i;
            }
        }

        if (inicio == -1) {
            return new int[]{-1, -1, 0};
        }

        int longitud = fin - inicio + 1;
        return new int[]{inicio, fin, longitud};
    }

}

public class Solution {

    public static void main(String[] args) throws IOException {

        try (BufferedReader bufferedReader =
                 new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter bufferedWriter =
                 new BufferedWriter(
                     new FileWriter(System.getenv("OUTPUT_PATH"))
                 )) {

        String linea = bufferedReader.readLine();

        String[] datos = linea.trim().split("\\s+");

        int[] vagones = new int[datos.length];

        for (int i = 0; i < datos.length; i++) {
            vagones[i] = Integer.parseInt(datos[i]);
        }

        int[] result = Result.detectarZonaAjuste(vagones);

        for (int i = 0; i < result.length; i++) {

            bufferedWriter.write(String.valueOf(result[i]));

            if (i != result.length - 1) {
                bufferedWriter.write(" ");
            }
        }

        bufferedWriter.newLine();

        }
    }
}
