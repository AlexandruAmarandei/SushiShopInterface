package Restaurant;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is in charge of assigning tasks to drones and staff. It does this
 * by having 3 queues that contain the ingredients, dishes and deliveries that
 * need to be restocked/delivered.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class Assigner extends Thread {

    private LinkedBlockingQueue<Dish> dishesToPrepare;
    private LinkedBlockingQueue<Ingredient> ingredientsToRestock;
    private LinkedBlockingQueue<Task> distanceToSuppliers;
    private LinkedBlockingQueue<Task> distanceToUser;
    private HashMap<Integer, Order> currentOrders;
    private KitchenStaffManager kitchenStaffManager;
    private HashMap<Integer, OrderTimer> orderTimer;
    private DroneManager droneManager;
    private HashMap<Integer, Integer> deliveringOrders;
    private HashMap<Integer, Integer> deliveringIngredients;
    private StockManager stockManager;
    private OrderManager orderManager;
    private ServerManager serverManager;

    public Assigner(KitchenStaffManager kitchenStaffManager, ServerManager serverManager, DroneManager droneManager, OrderManager orderManager) {
        super();
        dishesToPrepare = new LinkedBlockingQueue<>();
        ingredientsToRestock = new LinkedBlockingQueue<>();
        distanceToUser = new LinkedBlockingQueue<>();
        distanceToSuppliers = new LinkedBlockingQueue<>();
        orderTimer = new HashMap<>();
        this.droneManager = droneManager;
        this.kitchenStaffManager = kitchenStaffManager;
        deliveringOrders = new HashMap<>();
        deliveringIngredients = new HashMap<>();
        currentOrders = new HashMap<>();
        this.serverManager = serverManager;
        this.orderManager = orderManager;

    }

    public Assigner() {
        super();
        dishesToPrepare = new LinkedBlockingQueue<>();
        ingredientsToRestock = new LinkedBlockingQueue<>();
        distanceToUser = new LinkedBlockingQueue<>();
        distanceToSuppliers = new LinkedBlockingQueue<>();
        orderTimer = new HashMap<>();
        deliveringOrders = new HashMap<>();
        deliveringIngredients = new HashMap<>();
        currentOrders = new HashMap<>();
    }

    public void setClasses(KitchenStaffManager kitchenStaffManager, ServerManager serverManager, DroneManager droneManager, OrderManager orderManager, StockManager stockManager) {
        this.serverManager = serverManager;
        this.orderManager = orderManager;
        this.droneManager = droneManager;
        this.kitchenStaffManager = kitchenStaffManager;
        this.stockManager = stockManager;
        start();
    }

    public Order[] getCurrentOrders() {
        return currentOrders.values().toArray(new Order[currentOrders.size()]);
    }

    public HashMap<Integer, OrderTimer> getCurrentOrdersTimes() {
        return orderTimer;
    }

    public synchronized void addDish(Dish dish, Integer taskID) {
        dishesToPrepare.add(dish);
    }

    public synchronized void addIngredient(Ingredient ingredient, double distance, Integer taskID) {
        ingredientsToRestock.add(ingredient);
        distanceToSuppliers.add(new Task(taskID, distance));
    }

    public synchronized void addDelivery(double distance, Integer orderID, Order order) {

        currentOrders.put(orderID, order);
        distanceToUser.add(new Task(orderID, distance));
    }

    public synchronized void cancelOrderFromCustomer(Integer orderID, String username) {
        if (deliveringOrders.containsKey(orderID)) {
            Integer droneID = deliveringOrders.get(orderID);
            for (Drone drone : droneManager) {
                if (drone.droneID.equals(droneID)) {
                    Long returnTime = System.currentTimeMillis() - orderTimer.get(orderID).startTime;
                    deliveringOrders.remove(orderID);
                    orderTimer.remove(orderID);

                    drone.returnToShop(returnTime);
                    serverManager.updateOrderStatus("$2", orderID, currentOrders.get(orderID).threadIP);
                    currentOrders.remove(orderID);
                }
            }
        }
    }

    public synchronized void cancelOrderFromServer(Integer orderID) {
        if (deliveringOrders.containsKey(orderID)) {
            Integer droneID = deliveringOrders.get(orderID);
            for (Drone drone : droneManager) {
                if (drone.droneID.equals(droneID)) {
                    Long returnTime = System.currentTimeMillis() - orderTimer.get(orderID).startTime;
                    deliveringOrders.remove(orderID);
                    orderTimer.remove(orderID);

                    drone.returnToShop(returnTime);
                    String username = currentOrders.get(orderID).username;
                    serverManager.updateOrderStatus("$4", orderID, currentOrders.get(orderID).threadIP);
                    currentOrders.remove(orderID);
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {

            if (!dishesToPrepare.isEmpty()) {
                tryToAssignToStaff();
            }
            if (!ingredientsToRestock.isEmpty()) {

                tryToAssignToDronesForIngredients();
            }

            if (!distanceToUser.isEmpty()) {

                tryToAssignToDronesForDelivery();
            }
        }
    }

    public synchronized boolean tryToAssignToStaff() {
        for (KitchenStaff kitchenStaff : kitchenStaffManager) {
            if (!kitchenStaff.isWorking()) {
                Dish dish = dishesToPrepare.poll();
                stockManager.dishStock.startRestock(dish.name);
                for (String ingredient : dish.ingredients.keySet()) {
                    if (stockManager.ingredientStock.getAmount(ingredient) < dish.ingredients.get(ingredient)) {
                        return false;
                    }
                    stockManager.ingredientStock.substractIngredient(dish.ingredients.get(ingredient), ingredient);
                    if (stockManager.ingredientStock.needsRestock(ingredient)) {
                        Ingredient ing = stockManager.ingredientStock.get(ingredient);
                        stockManager.putIngredientsInRestockQueue(ing);

                    }
                }
                kitchenStaff.prepareFood(dish.name, dish.upperBound, dish.lowerBound);

                return true;
            }
        }
        return false;
    }

    public synchronized boolean tryToAssignToDronesForIngredients() {
        for (Drone drone : droneManager) {
            if (!drone.isWorking()) {
                Ingredient ingredient = ingredientsToRestock.poll();
                int amountToRestock = ingredient.initialStock - ingredient.stock - ingredient.expectedRestock;
                Task task = distanceToSuppliers.poll();
                deliveringIngredients.put(task.taskID, drone.droneID);
                double distance = task.distance;

                drone.addDeliverIngredient(stockManager.ingredientStock, ingredient.name, amountToRestock, distance, task.taskID);
                stockManager.ingredientStock.startRestock(ingredient.name);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean tryToAssignToDronesForDelivery() {
        for (Drone drone : droneManager) {
            if (!drone.isWorking()) {

                Task task = distanceToUser.poll();

                Long time = drone.addDeliverOrder(task);
                if (time > 0) {
                    deliveringOrders.put(task.taskID, drone.droneID);
                    orderTimer.put(task.taskID, new OrderTimer(System.currentTimeMillis(), time));
                    int ip = currentOrders.get(task.taskID).threadIP;
                    serverManager.updateOrderStatus("$1$" + time.toString(), task.taskID, ip);

                    return true;
                }

            }
        }
        return false;
    }

    public synchronized void orderDelivered(Integer orderID) {

        if (deliveringOrders.containsKey(orderID)) {
            deliveringOrders.remove(orderID);
            orderTimer.remove(orderID);

            serverManager.updateOrderStatus("$3", orderID, currentOrders.get(orderID).threadIP);
            orderManager.addCompleteOrder(currentOrders.get(orderID));
            currentOrders.remove(orderID);
        }
    }

    public synchronized void ingredientsDelivered(Integer amount, String ingredientName, Integer taskID) {
        deliveringIngredients.remove(taskID);
        stockManager.ingredientStock.restockIngredient(amount, ingredientName);
    }

}
