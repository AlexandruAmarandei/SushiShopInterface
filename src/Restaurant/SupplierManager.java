/*
 * This class manages the suppliers.
 */
package Restaurant;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class SupplierManager {

    private HashMap<String, Supplier> suppliers;
    private HashMap<String, String> ingredientLocation;

    public SupplierManager() {
        suppliers = new HashMap<>();
        ingredientLocation = new HashMap<>();
    }

    public void addSupplier(Supplier supplier) {
        if (!contains(supplier.name)) {
            suppliers.put(supplier.name, supplier);
        }
    }

    public boolean contains(String name) {
        return suppliers.containsKey(name);
    }

    public void removeSupplier(String name) {
        if (contains(name)) {
            suppliers.remove(name);
        }
    }

    public void chengeDistance(String name, Double newDistance) {
        if (contains(name)) {
            suppliers.get(name).distance = newDistance;
        }
    }

    public void addIngredientToSupplier(String supplier, String ingredientName, Double price, String currency) {
        if (contains(supplier)) {
            ingredientLocation.put(ingredientName, supplier);
            suppliers.get(supplier).addIngredient(ingredientName, price, currency);
        }
    }

    public void removeIngredientFromSupplier(String supplier, String ingredient) {
        if (suppliers.get(supplier).containsIngredient(ingredient)) {
            suppliers.get(supplier).removeIngredient(ingredient);
        }
    }

    public Supplier getSupplierOfIngredient(String ingredient) {
        return suppliers.get(ingredientLocation.get(ingredient));
    }

    public Supplier getSupplier(String supplier) {
        return suppliers.get(supplier);
    }

    public Collection<Supplier> getListOfSupplier() {
        return suppliers.values();
    }

    public Collection<String> getNameListOfSupplier() {
        return suppliers.keySet();
    }

    void changeSupplier(String name, Double distance) {
        suppliers.get(name).distance = distance;
    }
}
