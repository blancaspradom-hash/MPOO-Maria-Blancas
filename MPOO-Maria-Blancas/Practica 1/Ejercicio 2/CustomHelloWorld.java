import java.util.Scanner;

public class CustomHelloWorld {
    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in)) {
            System.out.print("Introduce tu nombre: ");
            String nombre = entrada.nextLine();
            
            System.out.println("Hola, " + nombre + "! Bienvenido a Java.");
            
            entrada.close();
        }
    
    }
}