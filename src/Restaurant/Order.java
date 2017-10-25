/**
 * The components of an order.
 */
package Restaurant;

import java.util.HashMap;

public class Order {

    public HashMap<String, Integer> items;
    public double distance;
    public String username;
    public int subtotal;
    public Integer ID;
    public static Integer defaultID = 0;
    public int threadIP;

    public Order() {
        defaultID++;
        items = new HashMap<>();
        subtotal = 0;
    }

}
