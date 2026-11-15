import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class Result {


    public static String cifrarMensaje(String mensaje, String alfabetoInterior, int posicionInicial, int intervaloRotacion) {
        StringBuilder cifrado = new StringBuilder();
        int letrasProcesadas = 0;
        int desplazamientoActual = posicionInicial % 26;

        for (int i = 0; i < mensaje.length(); i++) {
            char c = mensaje.charAt(i);

            if (c == ' ') {
                cifrado.append(' ');
            } else {
                int indiceExterior = c - 'A';

                int indiceInterior = (indiceExterior - desplazamientoActual) % 26;
                if (indiceInterior < 0) {
                    indiceInterior += 26;
                }

                cifrado.append(alfabetoInterior.charAt(indiceInterior));

                letrasProcesadas++;

                if (letrasProcesadas % intervaloRotacion == 0) {
                    desplazamientoActual = (desplazamientoActual + 1) % 26;
                }
            }
        }

        return cifrado.toString();
    }

}

public class Solution2 {
    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));) {
            String mensaje = bufferedReader.readLine();
            String alfabetoInterior = bufferedReader.readLine();
            int posicionInicial = Integer.parseInt(bufferedReader.readLine().trim());
            int intervaloRotacion = Integer.parseInt(bufferedReader.readLine().trim());

            String result = Result.cifrarMensaje(mensaje, alfabetoInterior, posicionInicial, intervaloRotacion);

            bufferedWriter.write(result);
            bufferedWriter.newLine();
        }
    }
}
