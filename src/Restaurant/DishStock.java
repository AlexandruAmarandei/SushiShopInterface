package Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is in charge of managing the dishes.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class DishStock {

    private Map<String, Dish> dishes;
    private ArrayList<String> understockDishes;
    private int understock;
    private IngredientStock ingredientStock;
    public int restockID = 0;

    public DishStock(IngredientStock ingredientStock) {
        dishes = new HashMap<>();
        understockDishes = new ArrayList<>();
        understock = 0;
        this.ingredientStock = ingredientStock;
    }

    public DishStock() {
        dishes = new HashMap<>();
        understockDishes = new ArrayList<>();
        understock = 0;
    }

    public void setClasses(IngredientStock ingredientStock) {
        this.ingredientStock = ingredientStock;
    }

    public Dish[] getDishList() {
        Dish[] returnList = new Dish[dishes.size()];
        int cnt = 0;
        for (Dish dish : dishes.values()) {
            returnList[cnt++] = dish;
        }
        return returnList;
    }

    public int getAmount(int amount, String key) {
        Dish dish;
        if (!dishes.containsKey(key)) {
            throw new IllegalArgumentException("Dish " + key + " doesn't exist!");
        }
        dish = dishes.get(key);

        return dish.stock;
    }

    public void substractDish(int amount, String key) {
        Dish dish;
        if (!dishes.containsKey(key)) {
            throw new IllegalArgumentException("Dish " + key + " doesn't exist!");
        }
        dish = dishes.get(key);

        if (dish.stock >= amount) {
            dish.stock -= amount;
            if (dish.stock < dish.restockLvl && !understockDishes.contains(key)) {
                understock++;
                understockDishes.add(key);
            }
            if (dish.stock + dish.expectedRestock < dish.restockLvl) {
                dish.restockNeeded = true;
            }

        } else {
            throw new IllegalArgumentException("Amount to large for " + key + ":\n" + "Requested: " + amount + "\nIn stock: " + key);
        }
    }

    public synchronized void restockDish(int amount, String key) {
        Dish dish;
        if (!dishes.containsKey(key)) {
            throw new IllegalArgumentException("Dish " + key + " doesn't exist!");
        }
        dish = dishes.get(key);

        dish.stock += amount;
        dish.restockingsInPlace--;
        dish.expectedRestock -= amount;
        if (dish.stock >= dish.restockLvl) {
            understock--;
            understockDishes.remove(key);
        }
    }

    public boolean needsRestock(String key) {
        Dish dish;
        if (!dishes.containsKey(key)) {
            throw new IllegalArgumentException("Dish " + key + " doesn't exist!");
        }
        dish = dishes.get(key);
        return dish.restockNeeded;
    }

    public void startRestock(String key) {
        Dish dish;
        if (!dishes.containsKey(key)) {
            throw new IllegalArgumentException("Dish " + key + " doesn't exist!");
        }
        dish = dishes.get(key);

        if (dish.restockNeeded == true) {
            dish.restockNeeded = false;
            dish.restockingsInPlace++;
            dish.expectedRestock++;
        }
    }

    public boolean addNewDish(String key, double weight, double price, String currency, int stock, int restockLvl, int lowerBound, int upperBound) {
        if (dishes.containsKey(key)) {
            return false;
        }

        dishes.put(key, new Dish(key, weight, price, currency, stock, restockLvl, lowerBound, upperBound));
        return true;
    }

    public boolean contains(String dish) {
        return dishes.containsKey(dish);
    }

    public void removeDish(String key) {
        if (dishes.containsKey(key)) {
            dishes.remove(key);

        } else {
            throw new IllegalArgumentException(key + " doesn't exist!");
        }
    }

    public Dish get(String dishKey) {
        if (!contains(dishKey)) {
            throw new IllegalArgumentException(dishKey + " doesn't exist!");
        }
        return dishes.get(dishKey);
    }

    public void chengeRestockOfDish(String dish, int newAmount) {
        if (dishes.containsKey(dish)) {
            dishes.get(dish).restockLvl = newAmount;
        }
    }

    void changeDish(String name, double weight, double price, String currency, int stock, int restock, int minTime, int maxTime) {
        if (dishes.containsKey(name)) {
            dishes.get(name).weight = weight;
            dishes.get(name).price = price;
            dishes.get(name).currency = currency;
            dishes.get(name).stock = stock;
            dishes.get(name).restockLvl = restock;
            dishes.get(name).lowerBound = minTime;
            dishes.get(name).upperBound = maxTime;

        }
    }
}
