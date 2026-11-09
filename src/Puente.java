import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Puente {
    private static final int maxPersonas = 4;
    private static final int maxPersonasPorSentido = 3;
    private static final double pesoMaximo = 200.0;

    private int personasEnPuente = 0;
    private int[] personasPorSentido = new int[2];
    private double pesoActual = 0.0;
    private int sentidoActual = -1;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition colaEspera = lock.newCondition();

    public void entrarPuente(int id, int sentido, double peso) throws InterruptedException {
        lock.lock();
        try {
            while (!puedeEntrar(sentido, peso)) {
                System.out.printf("[Persona %d | Sentido %d | %.1f kg] Esperando para entrar...\n", id, sentido, peso);
                colaEspera.await();
            }

            if (personasEnPuente == 0) {
                sentidoActual = sentido;
            }
            personasEnPuente++;
            personasPorSentido[sentido]++;
            pesoActual += peso;

            System.out.printf(">> [Persona %d ENTRA] Sentido: %d | Peso: %.1f kg | (En puente: %d persona(s), %.1f kg)\n",
                    id, sentido, peso, personasEnPuente, pesoActual);

        } finally {
            lock.unlock();
        }
    }

    public void salirPuente(int id, int sentido, double peso) {
        lock.lock();
        try {
            personasEnPuente--;
            personasPorSentido[sentido]--;
            pesoActual -= peso;

            System.out.printf("<< [Persona %d SALE ] Sentido: %d | Peso: %.1f kg | (En puente: %d persona(s), %.1f kg)\n",
                    id, sentido, peso, personasEnPuente, pesoActual);

            if (personasEnPuente == 0) {
                sentidoActual = -1;
            }

            colaEspera.signalAll();

        } finally {
            lock.unlock();
        }
    }

    private boolean puedeEntrar(int sentido, double peso) {

        boolean sentidoValido = (sentidoActual == -1 || sentidoActual == sentido);
        boolean capacidadTotalValida = (personasEnPuente < maxPersonas);
        boolean capacidadSentidoValida = (personasPorSentido[sentido] < maxPersonasPorSentido);
        boolean pesoValido = (pesoActual + peso <= pesoMaximo);
        return sentidoValido && capacidadTotalValida && capacidadSentidoValida && pesoValido;
    }
}


