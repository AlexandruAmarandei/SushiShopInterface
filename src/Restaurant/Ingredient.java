package Restaurant;

/**
 * An ingredient.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
import java.util.HashMap;

public class Ingredient {

    public String supplier;
    public String name;
    public Double price;
    public String currency;
    public int stock;
    public int restockLvl;
    public int restockingsInPlace;
    public int initialStock;
    public int restockSize;
    public boolean restockNeeded;
    public int expectedRestock;
    public HashMap<String, Integer> dishes;

    public Ingredient(String supplier, String name, Double price, String currency, int stock, int restockLvl, int restockSize) {
        this.supplier = supplier;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.initialStock = stock;
        this.stock = initialStock;
        this.restockLvl = restockLvl;
        this.restockSize = restockSize;
        this.restockingsInPlace = 0;
        this.restockNeeded = false;
        this.expectedRestock = 0;
        dishes = new HashMap<>();

    }

}
