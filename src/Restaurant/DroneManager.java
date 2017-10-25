package Restaurant;

import java.util.HashMap;
import java.util.Iterator;

/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class DroneManager implements Iterable<Drone> {

    private HashMap<Integer, Drone> drones;
    private Assigner assigner;

    public DroneManager(Assigner assigner) {
        drones = new HashMap<>();
        this.assigner = assigner;
    }

    public DroneManager() {
        drones = new HashMap<>();
    }

    public void setClasses(Assigner assigner) {
        this.assigner = assigner;
    }

    public boolean contains(Integer droneID) {
        return drones.containsKey(droneID);
    }

    public int addDrone(Double speed) {
        drones.put(drones.size(), new Drone(speed, drones.size(), this));
        return drones.size() - 1;
    }

    public void removeDrone(Integer droneID) {
        if (contains(droneID)) {
            drones.remove(droneID);
        }
    }

    public void ingredientsDelivered(int amount, String ingredientName, int taskID) {
        assigner.ingredientsDelivered(amount, ingredientName, taskID);
    }

    public void orderDelivered(int droneID) {
        assigner.orderDelivered(droneID);
    }

    public Drone[] getListOfDrones() {
        return drones.values().toArray(new Drone[drones.size()]);
    }

    @Override
    public Iterator<Drone> iterator() {
        Iterator<Drone> it = drones.values().iterator();
        return it;
    }

    void changeDroneSpeed(Integer key, double speed) {
        drones.get(key).speed = speed;
    }

}
