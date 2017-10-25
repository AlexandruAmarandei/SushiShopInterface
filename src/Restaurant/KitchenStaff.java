package Restaurant;

import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a worker and wait for input from the assigner.
 *
 * @author alexa
 */
public class KitchenStaff extends Thread {

    public String name;
    public String ID;
    private boolean working = false;
    private boolean running = true;
    public String dishName;
    private Long startTime;
    private Random random = new Random();
    private int seconds = 0;
    //private Thread t ;
    private KitchenStaffManager kitchenStaffManager;

    public KitchenStaff(KitchenStaffManager kitchenStaffManager, Integer ID) {
        super();
        this.kitchenStaffManager = kitchenStaffManager;
        startTime = -1l;
        dishName = "NA";
        start();
    }

    @Override
    public void run() {
        while (running) {
            if (working) {

                try {
                    sleep(1000 * seconds);
                    kitchenStaffManager.restockDish(1, dishName);

                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
                dishName = "NA";
                working = false;
            }

            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Drone.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized boolean isWorking() {
        return working;
    }

    public synchronized void prepareFood(String dishName, int upperBound, int lowerBound) {
        if (working == true) {
            System.out.println("Drone working");
            return;
        }
        working = true;
        this.startTime = System.currentTimeMillis();
        this.dishName = dishName;
        seconds = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
        this.notify();

    }

    public synchronized void stopThread() {
        running = false;
    }

    public Long getCurrentTime() {
        if (startTime == -1) {
            return 0l;
        }
        if (working == false) {
            return 0l;
        }
        return (seconds * 1000) - System.currentTimeMillis() - startTime;
    }

}
