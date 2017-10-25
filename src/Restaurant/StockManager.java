/**
 * This class is in charge of managing the stocks and sending dishes and ingredients to the assigner in order to get them ressuplied.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
package Restaurant;

import java.util.ArrayList;
import java.util.Collection;

public class StockManager {

    public IngredientStock ingredientStock;
    public ArrayList<KitchenStaff> kitchenStaff;
    public SupplierManager supplierManager;
    public DishStock dishStock;
    public Comms comms;
    public Assigner assigner;
    public static Integer orderID = 0;

    public StockManager(IngredientStock ingredientStock, DishStock dishStock, Assigner assigner, SupplierManager supplierManager) {
        kitchenStaff = new ArrayList<>();
        this.ingredientStock = ingredientStock;
        this.dishStock = dishStock;
        this.assigner = assigner;
        this.supplierManager = supplierManager;

    }

    public StockManager() {
        kitchenStaff = new ArrayList<>();
    }

    public void setClasses(IngredientStock ingredientStock, DishStock dishStock, Assigner assigner, SupplierManager supplierManager) {
        this.ingredientStock = ingredientStock;
        this.dishStock = dishStock;
        this.assigner = assigner;
        this.supplierManager = supplierManager;
    }

    public void startOder(Order order) throws InvalidOrderException {
        if (checkOrder(order)) {
            executeOrder(order);
        } else {
            throw new InvalidOrderException();
        }

    }

    public Ingredient[] getListOfIngredients() {

        Collection<Ingredient> ingredients = ingredientStock.getIngredients();
        return ingredients.toArray(new Ingredient[ingredients.size()]);
    }

    public String[] getOrderDifference(Order order) {
        ArrayList<String> differenceDishes = new ArrayList<>();
        for (String itemName : order.items.keySet()) {
            if (!dishStock.contains(itemName)) {
                differenceDishes.add(itemName);
            } else {
                int amount = order.items.get(itemName);

                if (amount > dishStock.get(itemName).stock) {
                    differenceDishes.add(itemName);
                }
            }
        }

        return differenceDishes.toArray(new String[differenceDishes.size()]);
    }

    public boolean checkOrder(Order order) {

        for (String itemName : order.items.keySet()) {
            if (!dishStock.contains(itemName)) {
                return false;
            }
            if (!dishStock.get(itemName).available) {
                return false;
            }
            int amount = order.items.get(itemName);

            if (amount > dishStock.get(itemName).stock) {
                return false;
            }
        }
        return true;
        /*
        for (String itemName : order.items.keySet()) {
            if (!dishStock.contains(itemName)) {
                return false;
            }
            Dish dish = dishStock.get(itemName);
            int amount = order.items.get(itemName);
            for (String ingredient : dish.ingredients.keySet()) {
                if (dish.ingredients.get(ingredient) * amount > ingredientStock.get(ingredient).stock) {
                    return false;
                }
            }     
        }
        return true;*/
    }

    public void executeOrder(Order order) {
        updateStocks(order);
        putDeliveryInDeliveryQueue(order);

    }

    public void updateStocks(Order order) {
        for (String itemName : order.items.keySet()) {
            dishStock.substractDish(order.items.get(itemName), itemName);
            if (dishStock.needsRestock(itemName)) {
                putDishInRestockQueue(dishStock.get(itemName));
            }
        }

    }

    public void putDeliveryInDeliveryQueue(Order order) {
        assigner.addDelivery(order.distance, order.ID, order);
    }

    public void putDishInRestockQueue(Dish dish) {
        assigner.addDish(dish, dishStock.restockID++);
    }

    public void putIngredientsInRestockQueue(Ingredient ingredient) {
        assigner.addIngredient(ingredient, supplierManager.getSupplier(ingredient.supplier).distance, ingredientStock.restockID++);

    }

    public Dish[] getListOfDishes() {
        return dishStock.getDishList();
    }

    public void addKitchenStaff(KitchenStaff kitchenCook) {
        kitchenStaff.add(kitchenCook);
    }

    public void addIngredient(String key, String supplier, Double price, String currency, int stock, int restockLvl, int restockSize) {
        ingredientStock.addNewIngredient(key, supplier, price, currency, stock, restockLvl, restockSize);
        supplierManager.addIngredientToSupplier(supplier, key, price, currency);
    }

    public void addDish(String key, double weight, double price, String currency, int stock, int restockLvl, int lowerBound, int upperBound) {
        dishStock.addNewDish(key, weight, price, currency, stock, restockLvl, lowerBound, upperBound);
    }

    public void addSupplier(Supplier supplier) {
        supplierManager.addSupplier(supplier);
    }

    public void addSupplier(String name, Double distance) {
        supplierManager.addSupplier(new Supplier(name, distance));
    }

    public boolean checkIfIngredientExists(String ingredient) {
        return ingredientStock.contains(ingredient);
    }

    public boolean checkIfDishExists(String dish) {
        return dishStock.contains(dish);
    }

    public boolean checkIfSupplierExists(String supplier) {
        return supplierManager.contains(supplier);
    }

    public Supplier[] getListOfSuppliers() {

        Collection<Supplier> list = supplierManager.getListOfSupplier();
        if (list != null) {
            return list.toArray(new Supplier[list.size()]);
        }
        return null;
    }

    public void changeRestockLevelOfIngredient(String ingredient, int amount) {
        ingredientStock.chengeRestockOfIngredient(ingredient, amount);
    }

    public void changeRestockLevelOfDish(String dish, int amount) {
        dishStock.chengeRestockOfDish(dish, amount);
    }

    public void changeIngredient(String key, String supplier, Double price, String currency, int stock, int restockLvl, int restockSize) {
        ingredientStock.changeIngredient(key, supplier, price, currency, stock, restockLvl, restockSize);
    }
}
