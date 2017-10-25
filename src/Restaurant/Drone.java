package Restaurant;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class Drone extends Thread {

    public double speed;
    private boolean working;
    public long sleepTime;
    private boolean running = true;
    private boolean ingredients = false;
    private boolean returning = false;
    private boolean order = false;
    private IngredientStock ingredientStock;
    private String ingredientName;
    private int amount;
    public Long startTime;
    private DroneManager droneManager;
    public Integer droneID;
    public Integer taskID;

    //private Thread t ;
    public Drone(double speed, Integer droneID, DroneManager droneManager) {
        super();
        this.droneID = droneID;
        this.droneManager = droneManager;
        this.speed = speed;
        taskID = -1;
        sleepTime = 0;
        startTime = 0l;

        start();
    }

    public synchronized void returnToShop(Long time) {
        order = ingredients = false;
        this.sleepTime = time;
        this.notify();

    }

    public String getCurrentState() {
        String state = "Idle";
        if (returning == true) {
            state = "Returning";
        } else {
            if (ingredients == true) {
                state = "Delivering ingredients";
            }
            if (order == true) {
                state = "Delivering order";
            }
        }
        return state;
    }

    @Override
    public void run() {
        while (running) {
            if (working == true) {

                try {
                    synchronized (this) {
                        try {
                            wait(sleepTime);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Drone.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (ingredients) {
                        droneManager.ingredientsDelivered(amount, ingredientName, taskID);
                        ingredients = false;
                        returning = true;
                    }
                    if (order) {

                        droneManager.orderDelivered(taskID);
                        order = false;
                        returning = true;
                    }
                    synchronized (this) {
                        sleep(sleepTime);
                    }
                    returning = false;
                    ingredients = order = false;
                } catch (InterruptedException e) {
                    System.out.println("Error from drone sleeping!");
                }
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

    public synchronized void addDeliverIngredient(IngredientStock ingredientStock, String key, int amount, double distance, int taskID) {
        if (working == true) {
            System.out.println("Drone working");
            return;
        }
        working = true;
        startTime = System.currentTimeMillis();
        this.taskID = taskID;
        this.ingredientStock = ingredientStock;
        this.amount = amount;
        ingredients = true;
        order = false;
        this.ingredientName = key;

        this.sleepTime = (long) (distance / speed) * 1000;
        this.notify();

    }

    public synchronized Long addDeliverOrder(Task task) {
        if (working == true) {
            System.out.println("Drone working");
            return Long.MIN_VALUE;
        }
        working = true;
        startTime = System.currentTimeMillis();
        this.taskID = task.taskID;
        ingredients = false;
        order = true;

        this.sleepTime = (long) (task.distance / speed) * 1000;

        this.notify();
        return sleepTime;

    }

    public synchronized void stopThread() {
        running = false;
    }

    public Long getTime() {
        Long time = 0l;
        if (startTime == 0 || sleepTime == 0) {
            return time;
        }
        if (working == false) {
            return time;
        }
        return sleepTime - (System.currentTimeMillis() - startTime);
    }
}
