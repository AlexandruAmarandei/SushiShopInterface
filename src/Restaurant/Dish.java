package Restaurant;

import java.util.HashMap;

/**
 * A dish.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class Dish {

    public String name;
    public double weight;
    public HashMap<String, Integer> ingredients;
    public Integer lowerBound, upperBound;
    public double price;
    public String currency;
    public int stock;
    public int initialStock;
    public int restockLvl;
    public int expectedRestock;
    public boolean restockNeeded;
    public int restockingsInPlace;
    public boolean available;

    public Dish(String name, double weight, double price, String currency, int stock, int restockLvl, int lowerBound, int upperBound) {
        this.name = name;
        this.ingredients = new HashMap<>();
        this.weight = weight;
        this.price = price;
        this.currency = currency;
        this.initialStock = stock;
        this.stock = initialStock;
        this.restockLvl = restockLvl;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.available = false;
        this.restockingsInPlace = 0;
        this.restockNeeded = false;
        this.expectedRestock = 0;

    }
}
