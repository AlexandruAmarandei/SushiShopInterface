package Restaurant;

import Restaurant.BusinessApplicationPanels.*;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * This charge is the part 7 of the cw. It can change almost everything and it's
 * connected to almost anything.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class ServerClient extends JFrame {

    private StockManager stockManager;
    private OrderManager orderManager;
    private KitchenStaffManager kitchenStaffManager;
    private DroneManager droneManager;

    public ServerClient(StockManager stockManager, OrderManager orderManager, KitchenStaffManager kitchenStaffManager, DroneManager droneManager) {
        this.stockManager = stockManager;
        this.orderManager = orderManager;
        this.kitchenStaffManager = kitchenStaffManager;
        this.droneManager = droneManager;
    }

    public void setClasses(StockManager stockManager, OrderManager orderManager, KitchenStaffManager kitchenStaffManager, DroneManager droneManager) {
        this.stockManager = stockManager;
        this.orderManager = orderManager;
        this.kitchenStaffManager = kitchenStaffManager;
        this.droneManager = droneManager;
    }

    public ServerClient() {

    }

    public String[] getSupplierNameList() {
        Collection<String> returnArr = stockManager.supplierManager.getNameListOfSupplier();

        return returnArr.toArray(new String[returnArr.size()]);
    }

    public String[] getIngredientNames() {
        Collection<String> returnArr = stockManager.ingredientStock.getIngredientNames();
        return returnArr.toArray(new String[returnArr.size()]);
    }

    public Dish[] getDishList() {
        return stockManager.getListOfDishes();
    }

    public Ingredient[] getIngredientList() {
        return stockManager.getListOfIngredients();

    }

    public Supplier[] getSupplierList() {
        Supplier[] suppliers = stockManager.getListOfSuppliers();
        if (suppliers != null) {
            return stockManager.getListOfSuppliers();
        }
        return null;
    }

    public void changeRestockLivelOfIngredient(String ingredientName, Integer newAmount) {
        stockManager.changeRestockLevelOfIngredient(ingredientName, newAmount);
    }

    public void changeRestockLivelOfDish(String dishName, Integer newAmount) {
        stockManager.changeRestockLevelOfDish(dishName, newAmount);
    }

    public void addIngredient(String key, String supplier, Double price, String currency, int stock, int restockLvl, int restockSize) {
        if (!stockManager.checkIfIngredientExists(key)) {
            stockManager.addIngredient(key, supplier, price, currency, stock, restockLvl, restockSize);
        }
    }

    public void changeIngredient(String key, String supplier, Double price, String currency, int stock, int restockLvl, int restockSize) {
        if (!stockManager.checkIfIngredientExists(key)) {
            stockManager.changeIngredient(key, supplier, price, currency, stock, restockLvl, restockSize);
        }
    }

    public void addDish(String key, double weight, double price, String currency, int stock, int restockLvl, int lowerBound, int upperBound) {
        if (!stockManager.checkIfDishExists(key)) {
            stockManager.addDish(key, weight, price, currency, stock, restockLvl, lowerBound, upperBound);
        }
    }

    public void addSupplier(String name, Double distance) {
        if (!stockManager.checkIfSupplierExists(name)) {
            stockManager.addSupplier(name, distance);
        }
    }

    public void changeSupplier(String name, Double distance) {
        stockManager.supplierManager.changeSupplier(name, distance);
    }

    public void removeSupplier(String name) {
        stockManager.supplierManager.removeSupplier(name);
    }

    public void removeDish(String key) {
        stockManager.dishStock.removeDish(key);
    }

    public void removeIngredient(String key) {
        stockManager.ingredientStock.removeIngredient(key);
    }

    public Order[] getCurrentOrders() {
        return stockManager.assigner.getCurrentOrders();
    }

    public HashMap<Integer, OrderTimer> getCurrentStartTimes() {
        return stockManager.assigner.getCurrentOrdersTimes();
    }

    public void cancelOrder(Integer orderID) {
        stockManager.assigner.cancelOrderFromServer(orderID);
    }

    public ArrayList<Order> getCompletedOrders() {
        return orderManager.getAllCompleteOrders();
    }

    public void removeOrder(Integer orderID) {
        orderManager.removeOrder(orderID);
    }

    public void removeAllCompleteOrders() {
        orderManager.removeAllOrders();
    }

    public Integer addKitchenStaff(String staffName) {
        return kitchenStaffManager.addKitchenStaff(staffName);
    }

    public void addDrone(Double speed) {
        droneManager.addDrone(speed);
    }

    public void removeKitchenStaff(String name) {
        kitchenStaffManager.removeKitchenStaff(name);
    }

    public void removeDrone(Integer droneID) {
        droneManager.removeDrone(droneID);
    }

    public KitchenStaff[] getKitchenStaff() {
        return kitchenStaffManager.getListOfKitchenStaff();
    }

    public Drone[] getDrones() {
        return droneManager.getListOfDrones();
    }
    private JPanel currentPanel;
    private final ServerClient thisServerClient = this;

    public void createAndShowGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setName("BusinessApplication");
        this.setTitle("BusinessApplication");
        this.setLayout(new BorderLayout());

        SuppliersPanel suppliersPanel;
        DishPanel dishPanel;
        IngredientPanel ingredientPanel;
        CurrentOrdersPanel currentOrdersPanel;
        CompleteOrdersPanel completeOrdersPanel;
        DronePanel dronePanel;
        KitchenStaffPanel kitchenStaffPanel;
        Container content = this.getContentPane();
        currentPanel = new SuppliersPanel(thisServerClient);
        JButton suppliers = new JButton("Suppliers");

        JButton ingredients = new JButton("Ingredients");
        JButton dishes = new JButton("Dishes");
        JButton currentOrders = new JButton("Current Orders");
        JButton completeOrders = new JButton("Complete Orders");
        JButton kitchenStaff = new JButton("Kitchen Staff");
        JButton drone = new JButton("Drones");
        JFrame refresh = this;
        suppliers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.removeAll();
                currentPanel.add(new SuppliersPanel(thisServerClient));
                currentPanel.updateUI();
                //refresh.repaint();

            }
        });
        ingredients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.removeAll();
                currentPanel.add(new IngredientPanel(thisServerClient));
                currentPanel.updateUI();

            }
        });
        dishes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.removeAll();
                currentPanel.add(new DishPanel(thisServerClient));
                currentPanel.updateUI();
            }
        });
        currentOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.removeAll();
                currentPanel.add(new CurrentOrdersPanel(thisServerClient));
                currentPanel.updateUI();
            }
        });
        completeOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.removeAll();
                currentPanel.add(new CompleteOrdersPanel(thisServerClient));
                currentPanel.updateUI();
            }
        });
        kitchenStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.removeAll();
                currentPanel.add(new KitchenStaffPanel(thisServerClient));
                currentPanel.updateUI();
            }
        });
        drone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPanel.removeAll();
                currentPanel.add(new DronePanel(thisServerClient));
                currentPanel.updateUI();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1));
        JPanel locker = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locker.add(buttonPanel);
        JScrollPane buttonScroller = new JScrollPane(locker, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buttonScroller.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttonPanel.add(suppliers);
        buttonPanel.add(ingredients);
        buttonPanel.add(dishes);
        buttonPanel.add(currentOrders);
        buttonPanel.add(completeOrders);
        buttonPanel.add(kitchenStaff);
        buttonPanel.add(drone);

        content.add(buttonScroller, BorderLayout.WEST);
        content.add(currentPanel, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }

    /*
    public static void main(String[] args){
        ServerClient serverClient = new ServerClient();
        serverClient.startServerClient();
    }
     */
    public void startServerClient() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public void addIngredientToDish(String dishName, String ingredient, int amount) {
        int previous = 0;
        if (stockManager.dishStock.get(dishName).ingredients.containsKey(ingredient)) {
            previous = stockManager.dishStock.get(dishName).ingredients.get(ingredient);

        }
        stockManager.ingredientStock.addDishToIngredient(dishName, ingredient, amount + previous);
        stockManager.dishStock.get(dishName).ingredients.put(ingredient, amount + previous);
    }

    public void removeIngredientFromDish(String dishName, String ingredient, int amount) {

        if (stockManager.dishStock.get(dishName).ingredients.containsKey(ingredient)) {
            int previous = stockManager.dishStock.get(dishName).ingredients.get(ingredient);
            stockManager.ingredientStock.removeDishFromIngredient(dishName, ingredient, previous - amount);
            if (previous - amount > 0) {
                stockManager.dishStock.get(dishName).ingredients.put(ingredient, previous - amount);
            } else {
                stockManager.dishStock.get(dishName).ingredients.remove(ingredient);
            }
        }
    }

    public void makeDishAvailable(String dishName, boolean selected) {
        stockManager.dishStock.get(dishName).available = selected;
    }

    public void changeDish(String name, double weight, double price, String currency, int stock, int restock, int minTime, int maxTime) {
        stockManager.dishStock.changeDish(name, weight, price, currency, stock, restock, minTime, maxTime);
    }

    public void changeDroneSpeed(Integer key, double speed) {
        droneManager.changeDroneSpeed(key, speed);
    }

    public void changeStaffName(Integer name, String newName) {
        kitchenStaffManager.changeKitchenStaffName(name, newName);
    }

}
