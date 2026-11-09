import java.util.Random;

class Persona extends Thread {
    private final int id;
    private final int sentido;
    private final double peso;
    private final Puente puente;
    private final Random random = new Random();

    public Persona(int id, int sentido, double peso, Puente puente) {
        this.id = id;
        this.sentido = sentido;
        this.peso = peso;
        this.puente = puente;
    }

    @Override
    public void run() {
        try {
            puente.entrarPuente(id, sentido, peso);

            int tiempoCruzar = 10 + random.nextInt(41);
            Thread.sleep(tiempoCruzar * 100);
            puente.salirPuente(id, sentido, peso);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}