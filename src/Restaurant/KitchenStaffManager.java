package Restaurant;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class manages the kitchenstaff.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class KitchenStaffManager implements Iterable<KitchenStaff> {

    private HashMap<Integer, KitchenStaff> kitchenStaff;
    private DishStock dishStock;
    public Integer staffNextID;

    public KitchenStaffManager(DishStock dishStock) {
        kitchenStaff = new HashMap<>();
        this.dishStock = dishStock;
        staffNextID = 0;
    }

    public KitchenStaffManager() {
        kitchenStaff = new HashMap<>();
        staffNextID = 0;
    }

    public void setClasses(DishStock dishStock) {
        this.dishStock = dishStock;
    }

    public boolean contains(Integer staffID) {
        return kitchenStaff.containsKey(staffID);
    }

    public int addKitchenStaff(String name) {
        staffNextID++;
        kitchenStaff.put(staffNextID, new KitchenStaff(this, staffNextID));
        kitchenStaff.get(staffNextID).name = name;
        return staffNextID;
    }

    public void removeKitchenStaff(String staffName) {
        for (Integer key : kitchenStaff.keySet()) {
            if (kitchenStaff.get(key).name.equals(staffName)) {
                kitchenStaff.remove(key);
            }
        }
    }

    public Integer getIDOfStaff(String staffName) {
        for (Integer key : kitchenStaff.keySet()) {
            if (kitchenStaff.get(key).name.equals(staffName)) {
                return key;
            }
        }
        return -1;
    }

    public void removeKitchenStaff(Integer staffID) {
        if (contains(staffID)) {
            kitchenStaff.remove(staffID);
        }
    }

    public void restockDish(int amount, String dishID) {
        dishStock.restockDish(amount, dishID);
    }

    public KitchenStaff[] getListOfKitchenStaff() {
        return kitchenStaff.values().toArray(new KitchenStaff[kitchenStaff.size()]);
    }

    @Override
    public Iterator<KitchenStaff> iterator() {
        Iterator<KitchenStaff> it = kitchenStaff.values().iterator();
        return it;
    }

    void changeKitchenStaffName(Integer ID, String newName) {
        kitchenStaff.get(ID).name = newName;
    }

}
