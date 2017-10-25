package Restaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is in charge of managing the ingredients.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class IngredientStock {

    private Map<String, Ingredient> ingredients;
    private ArrayList<String> understockIngredients;
    public int understock;
    public int restockID = 0;
    private SupplierManager supplierManager;

    public IngredientStock(SupplierManager supplierManager) {
        ingredients = new HashMap<>();
        understockIngredients = new ArrayList<>();
        understock = 0;
        this.supplierManager = supplierManager;
    }

    public IngredientStock() {
        ingredients = new HashMap<>();
        understockIngredients = new ArrayList<>();
        understock = 0;
    }

    public void setClasses(SupplierManager supplierManager) {
        this.supplierManager = supplierManager;
    }

    public Collection<Ingredient> getIngredients() {
        return ingredients.values();
    }

    public int checkAmount(int amount, String key) {
        Ingredient ingredient;
        if (!ingredients.containsKey(key)) {
            throw new IllegalArgumentException("Ingredient " + key + " doesn't exist!");
        }
        ingredient = ingredients.get(key);

        return ingredient.stock;
    }

    public int getAmount(String key) {
        Ingredient ingredient;
        if (!ingredients.containsKey(key)) {
            throw new IllegalArgumentException("Ingredient " + key + " doesn't exist!");
        }
        ingredient = ingredients.get(key);

        return ingredient.stock;
    }

    public void substractIngredient(int amount, String key) {
        Ingredient ingredient;
        if (!ingredients.containsKey(key)) {
            throw new IllegalArgumentException("Ingredient " + key + " doesn't exist!");
        }
        ingredient = ingredients.get(key);

        if (ingredient.stock >= amount) {
            ingredient.stock -= amount;
            if (ingredient.stock < ingredient.restockLvl && !understockIngredients.contains(key)) {
                understock++;
                understockIngredients.add(key);
            }
            if (ingredient.stock + ingredient.expectedRestock < ingredient.restockLvl) {
                ingredient.restockNeeded = true;
            }

        } else {
            throw new IllegalArgumentException("Amount to large for " + key + ":\n" + "Requested: " + amount + "\nIn stock: " + key);
        }
    }

    public void restockIngredient(int amount, String key) {
        Ingredient ingredient;
        if (!ingredients.containsKey(key)) {
            throw new IllegalArgumentException("Ingredient " + key + " doesn't exist!");
        }
        ingredient = ingredients.get(key);

        ingredient.stock += amount;
        ingredient.restockingsInPlace--;
        ingredient.expectedRestock -= amount;
        if (ingredient.stock >= ingredient.restockLvl) {
            understock--;
            understockIngredients.remove(key);
        }
    }

    public boolean needsRestock(String key) {
        Ingredient ingredient;
        if (!ingredients.containsKey(key)) {
            throw new IllegalArgumentException("Ingredient " + key + " doesn't exist!");
        }
        ingredient = ingredients.get(key);
        return ingredient.restockNeeded;
    }

    public void startRestock(String key) {
        Ingredient ingredient;
        if (!ingredients.containsKey(key)) {
            throw new IllegalArgumentException("Ingredient " + key + " doesn't exist!");
        }
        ingredient = ingredients.get(key);

        if (ingredient.restockNeeded == true) {
            ingredient.restockNeeded = false;
            ingredient.restockingsInPlace++;
            ingredient.expectedRestock += ingredient.restockSize;
        }
    }

    public boolean addNewIngredient(String key, String supplier, Double price, String currency, int stock, int restockLvl, int restockSize) {
        if (ingredients.containsKey(key)) {
            return false;
        }
        if (supplierManager.contains(supplier)) {
            supplierManager.addIngredientToSupplier(supplier, key, price, currency);
            ingredients.put(key, new Ingredient(supplier, key, price, currency, stock, restockLvl, restockSize));
            return true;
        }
        return false;
    }

    public boolean contains(String ingredient) {
        return ingredients.containsKey(ingredient);
    }

    public void removeIngredient(String key) {
        if (ingredients.containsKey(key)) {
            ingredients.remove(key);

        } else {
            throw new IllegalArgumentException(key + " doesn't exist!");
        }
    }

    public Ingredient get(String ingredientKey) {
        if (!contains(ingredientKey)) {
            throw new IllegalArgumentException(ingredientKey + " doesn't exist!");
        }
        return ingredients.get(ingredientKey);
    }

    public void chengeRestockOfIngredient(String ingredient, int newAmount) {
        if (ingredients.containsKey(ingredient)) {
            ingredients.get(ingredient).restockLvl = newAmount;
        }
    }

    public void addDishToIngredient(String dishName, String ingredientName, Integer amount) {
        ingredients.get(ingredientName).dishes.put(dishName, amount);
    }

    public void removeDishFromIngredient(String dishName, String ingredientName, Integer amount) {
        if (amount == 0) {
            ingredients.get(ingredientName).dishes.remove(dishName);
        } else {
            ingredients.get(ingredientName).dishes.put(dishName, amount);
        }

    }

    public Collection<String> getIngredientNames() {
        return ingredients.keySet();
    }

    public void changeIngredient(String key, String supplier, Double price, String currency, int stock, int restockLvl, int restockSize) {
        supplierManager.removeIngredientFromSupplier(ingredients.get(key).supplier, key);
        ingredients.get(key).currency = currency;
        ingredients.get(key).supplier = supplier;
        ingredients.get(key).price = price;
        ingredients.get(key).stock = stock;
        ingredients.get(key).restockLvl = restockLvl;
        ingredients.get(key).restockSize = restockSize;
        supplierManager.addIngredientToSupplier(supplier, key, price, currency);

    }
}
