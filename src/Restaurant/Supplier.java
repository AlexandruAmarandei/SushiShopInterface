/**
 * A supplier.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
package Restaurant;

import java.util.HashMap;

public class Supplier {

    public String name;
    public Double distance;
    public HashMap<String, SupplierPrice> ingredients;

    public Supplier(String name, Double distance) {
        ingredients = new HashMap<>();
        this.name = name;
        this.distance = distance;
    }

    public void addIngredient(String name, Double price, String currency) {
        if (!containsIngredient(name)) {
            ingredients.put(name, new SupplierPrice(price, currency));
        }
    }

    public boolean containsIngredient(String name) {
        return ingredients.containsKey(name);
    }

    public void removeIngredient(String name) {
        if (containsIngredient(name)) {
            ingredients.remove(name);
        }
    }

}
