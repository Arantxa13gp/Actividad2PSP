import java.util.Random;

public class SimulacionPuente {
    public static void main(String[] args) {
        Puente puente = new Puente();
        Random random = new Random();
        int idPersona = 1;

        System.out.println("Simulación Puente");

        while (idPersona <= 20) {
            int sentido = random.nextInt(2);
            double peso = 40 + random.nextInt(81);

            Persona persona = new Persona(idPersona++, sentido, peso, puente);
            persona.start();

            try {
                int tiempoLlegada = 1 + random.nextInt(30);
                Thread.sleep(tiempoLlegada * 100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}