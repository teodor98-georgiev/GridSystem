
import java.util.concurrent.CountDownLatch;

public class delayedOpContaineer implements Runnable  {
    private double current;
    CircuitBreaker cirB; // had here problem, passing reference ???   field needed to access the openCircuitMethod
    // got stuck here, so how ?? through constructor I passed a reference (dependency), so that I can access CircuitBreaker
    // class.
    private CountDownLatch latch;


    public delayedOpContaineer(double current, CircuitBreaker cirB, CountDownLatch latch) {
        this.current = current;
        this.cirB = cirB;
        this.latch = latch;
    }


    @Override
    public void run() {
        // this delay simulates a 1 tonn circuit breaker that needs some time when moving when isolates components
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cirB.openCircuit();
        latch.countDown();
    }
}
