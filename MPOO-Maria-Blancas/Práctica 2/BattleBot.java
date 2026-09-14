import java.util.Objects;
import java.util.Random;
import java.util.UUID;

class Bateria {
    private final String tipo;
    private final String fabricante;
    private double nivelCarga;

    public Bateria(String tipo, String fabricante, double nivelCarga) {
        this.tipo = tipo;
        this.fabricante = fabricante;
        this.nivelCarga = nivelCarga;
    }

    public double getNivelCarga() {
        return nivelCarga;
    }

    public void setNivelCarga(double nivelCarga) {
        this.nivelCarga = Math.max(0.0, Math.min(100.0, nivelCarga));
    }

    @Override
    public String toString() {
        return tipo + " (" + fabricante + ") - " + String.format("%.1f", nivelCarga) + "%";
    }
}

class Robot {
    private static int totalRobots = 0;

    private final UUID id;
    private final String nombre;
    private double velocidad;
    private double distanciaRecorrida;
    private final Bateria bateria;

    public Robot(String nombre, Bateria bateria) {
        this.id = UUID.randomUUID();
        this.nombre = nombre;
        this.velocidad = 0.0;
        this.distanciaRecorrida = 0.0;
        this.bateria = bateria;
        totalRobots++;
    }

    public static int getTotalRobots() {
        return totalRobots;
    }

    public void acelerar(double incremento) {
        this.velocidad += incremento;
    }

    public void detener() {
        this.velocidad = 0.0;
    }

    public void avanzar(Random random) {
        if (this.bateria.getNivelCarga() <= 0) {
            return;
        }
        double tiempo = 2.0 + random.nextDouble() * 8.0;
        double avance = this.velocidad * tiempo;
        this.distanciaRecorrida += avance;

        double consumo = random.nextInt(15) + 5;
        this.bateria.setNivelCarga(this.bateria.getNivelCarga() - consumo);
    }

    public double obtenerDistancia() {
        return this.distanciaRecorrida;
    }

    @Override
    public String toString() {
        return "Robot[ID=" + id + ", Nombre='" + nombre + "', Velocidad=" + velocidad + 
               " m/s, Distancia=" + String.format("%.2f", distanciaRecorrida) + 
               " m, Batería=" + bateria + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Robot robot = (Robot) obj;
        return Objects.equals(id, robot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class BattleBot {
    public static void main(String[] args) {
        Random random = new Random();

        Robot robot1 = new Robot("Ares", new Bateria("LiPo", "RoboTech", 100.0));
        Robot robot2 = new Robot("Zeus", new Bateria("Li-Ion", "PowerBot", 90.0));
        Robot robot3 = new Robot("Titan", new Bateria("LiPo", "EnergyX", 100.0));

        System.out.println("=======================================================");
        System.out.println("Robots participantes en la batalla: " + Robot.getTotalRobots());
        System.out.println("=======================================================");

        System.out.println("\n--- ESTADO INICIAL DE LOS ROBOTS ---");
        System.out.println(robot1);
        System.out.println(robot2);
        System.out.println(robot3);

        robot1.acelerar(12.5);
        robot2.acelerar(8.0);
        robot3.acelerar(15.0);

        robot1.avanzar(random);
        robot2.avanzar(random);
        robot3.avanzar(random);

        System.out.println("\n--- ESTADO TRAS LA BATALLA ---");
        System.out.println(robot1);
        System.out.println(robot2);
        System.out.println(robot3);

        System.out.println("\n--- EXPERIMENTACIÓN CON REFERENCIAS E IGUALDAD ---");
        Robot robotFavorito = robot1;
        Robot robot2Copia = new Robot("Zeus", new Bateria("Li-Ion", "PowerBot", 90.0));

        System.out.println("robot1.equals(robotFavorito): " + robot1.equals(robotFavorito));
        System.out.println("robot1.equals(robot2Copia): " + robot1.equals(robot2Copia));
    }
}