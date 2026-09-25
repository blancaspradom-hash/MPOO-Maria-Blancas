import java.io.*;
import java.util.*;

class Solution {

    static class Resultado {

        static class PilaDinamica {
            private double[] elementos;
            private int cima;

            public PilaDinamica(int capacidadInicial) {
                this.elementos = new double[Math.max(1, capacidadInicial)];
                this.cima = -1;
            }

            public void push(double valor) {
                if (this.cima + 1 == this.elementos.length) {
                    ampliarCapacidad();
                }
                this.cima++;
                this.elementos[this.cima] = valor;
            }

            public double pop() {
                if (isEmpty()) {
                    return Double.NaN;
                }
                double valor = this.elementos[this.cima];
                this.cima--;
                return valor;
            }

            public double peek() {
                if (isEmpty()) {
                    return Double.NaN;
                }
                return this.elementos[this.cima];
            }

            public boolean isEmpty() {
                return this.cima == -1;
            }

            public int size() {
                return this.cima + 1;
            }

            public int capacity() {
                return this.elementos.length;
            }

            private void ampliarCapacidad() {
                double[] nuevoArreglo = new double[this.elementos.length * 2];
                for (int i = 0; i <= this.cima; i++) {
                    nuevoArreglo[i] = this.elementos[i];
                }
                this.elementos = nuevoArreglo;
            }
        }

        public static List<String> procesarOperaciones(int capacidad, List<String> operaciones) {
            PilaDinamica pila = new PilaDinamica(capacidad);
            List<String> salida = new ArrayList<>();

            for (String linea : operaciones) {
                if (linea == null) {
                    continue;
                }

                String[] partes = linea.trim().split("\\s+");
                if (partes.length == 0 || partes[0].isEmpty()) {
                    continue;
                }

                String operacion = partes[0];

                switch (operacion) {
                    case "PUSH" -> {
                        if (partes.length < 2) {
                            salida.add("ERROR");
                            continue;
                        }
                        try {
                            double valor = Double.parseDouble(partes[1]);
                            pila.push(valor);
                            salida.add("OK");
                        } catch (NumberFormatException e) {
                            salida.add("ERROR");
                        }
                    }
                    case "POP" -> {
                        double eliminado = pila.pop();
                        salida.add(Double.isNaN(eliminado) ? "EMPTY" : String.valueOf(eliminado));
                    }
                    case "PEEK" -> {
                        double cima = pila.peek();
                        salida.add(Double.isNaN(cima) ? "EMPTY" : String.valueOf(cima));
                    }
                    case "SIZE" ->
                        salida.add(String.valueOf(pila.size()));
                    case "CAPACITY" ->
                        salida.add(String.valueOf(pila.capacity()));
                    case "ISEMPTY" ->
                        salida.add(String.valueOf(pila.isEmpty()));
                    default ->
                        salida.add("ERROR");
                }
            }

            return salida;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")))) {

            int capacidad = Integer.parseInt(bufferedReader.readLine().trim());
            int nOperaciones = Integer.parseInt(bufferedReader.readLine().trim());

            List<String> operaciones = new ArrayList<>();
            for (int i = 0; i < nOperaciones; i++) {
                String operacionesItem = bufferedReader.readLine();
                operaciones.add(operacionesItem);
            }

            List<String> result = Resultado.procesarOperaciones(capacidad, operaciones);

            for (int i = 0; i < result.size(); i++) {
                bufferedWriter.write(result.get(i));
                if (i != result.size() - 1) {
                    bufferedWriter.write("\n");
                }
            }

            bufferedWriter.newLine();
        }
    }
}
