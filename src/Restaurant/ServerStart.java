package Restaurant;

/**
 * !Don't forget to make the dishes available and set everything server related
 * up before user login. !There is also a need for a postcodes file which I will
 * provide. !Please don't use the $ or & characters because I use them to parse
 * the inputs from client and servers.
 *
 * This class is the start of the project. I've left a couple of objects so it's
 * easier to test. This class creates all of the classes and assignes them to
 * one another. I will provide class explanation but no incode comments because
 * of the lack of time. I've take a slightly different approach to the specs.
 *
 * For the first part, the classes are ingredientStock, DishStock and Supplier
 * Manager. Trough them I manage the Ingredient, Dish and Supplier object. One
 * additional feature here is that, if an ingredient is understock and a drone
 * has a task of bringing back restock, if the amount that the drone will bring
 * is still smaller than the restock level, then the ingredient should be
 * restocked again for the difference.
 *
 * For the second part, the stock management class isn't that big. I've spread
 * it's tasks around.
 *
 * For part three, my kitchen staff is represented by a thread that is in a
 * permanent wait state. Once it recieves a new task, it self notifies to that
 * we don't spend useless computing power on a while(true). Instead of having a
 * kitchen employee monitor each dish, I created an assigner that, once a dish
 * should be cooked, it assigns tries to assign it to a free chicken staff. Once
 * the staff starts cooking it, the ingredients values are updated. Thus, if the
 * ingredients are understock now, they will be put in the restock queue.
 *
 * For part four, I made the comms class specifically to get the messages and
 * send them to another class. For each client we create a separate thread that
 * communicates just with him. Additionally, the interpretor is in the
 * serverManager class. I made the communication using sockets.
 *
 * Part five has a different package. The client package works on it's own. It
 * contains a simple interface where a client can register. The server will save
 * the client data with the userManager class along with verifying the login
 * data at each try. In the middle, the menu with all the dishes is shown. At
 * the right border I made a basket that holds the current items and shows a
 * subtotal. In the left side there is a panels that holds the current
 * deliveries. A delivery can be cancelled by the user or the server.(Haven't
 * tested this yet). After an order is placed, then it will be put on the order
 * panel and wait for the server to send another update with the time it will it
 * to be delivered.
 *
 * Part 6: A drone is a more advanced kitchenstaff. The only big difference is
 * that I used a wait() for the time the drone is delivering because I need to
 * notify the class if the order is cancelled.
 *
 * Part 7: My business application is the interface of the ServerClient class. I
 * recommend using the class because the interface is not finished.
 *
 * Part8: Only supports user data storage, complete orders and postcodes.
 *
 * Additionally I have a lot of work that I couldn't manage to fit in the last
 * version. For example a server can check if a user is logged in before it send
 * him a message and can differentiate between threads made with the same
 * username.
 *
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class ServerStart {

    private Comms communication;
    private StockManager stockManager;
    private IngredientStock ingredientStock;
    private DishStock dishStock;
    private Assigner assigner;
    private KitchenStaffManager kitchenStaffManager;
    private SupplierManager supplierManager;
    private ServerManager serverManager;
    private UserManager userManager;
    private DroneManager droneManager;
    private ServerClient serverClient;
    private OrderManager orderManager;

    public void initialize() {
        supplierManager = new SupplierManager();
        ingredientStock = new IngredientStock();
        dishStock = new DishStock();
        droneManager = new DroneManager();
        orderManager = new OrderManager();
        assigner = new Assigner();
        stockManager = new StockManager();

        userManager = new UserManager();
        serverManager = new ServerManager();
        communication = new Comms(1500);
        kitchenStaffManager = new KitchenStaffManager();
        serverClient = new ServerClient();

        ingredientStock.setClasses(supplierManager);
        dishStock.setClasses(ingredientStock);
        droneManager.setClasses(assigner);
        orderManager.setClasses(userManager);
        assigner.setClasses(kitchenStaffManager, serverManager, droneManager, orderManager, stockManager);
        stockManager.setClasses(ingredientStock, dishStock, assigner, supplierManager);
        serverManager.setClassesAndStart(stockManager, userManager, communication, orderManager);
        communication.setClassesAndStart(serverManager);
        kitchenStaffManager.setClasses(dishStock);
        serverClient.setClasses(stockManager, orderManager, kitchenStaffManager, droneManager);

        serverClient.addKitchenStaff("Staff1");
        serverClient.addKitchenStaff("Bob");
        serverClient.addKitchenStaff("Tom");

        serverClient.addDrone(20d);
        serverClient.addDrone(21d);

        serverClient.addSupplier("Supplier1", 40d);
        serverClient.addSupplier("Supplier2", 60d);
        serverClient.addSupplier("Supplier3", 80d);

        serverClient.addIngredient("Ingredient1", "Supplier1", 3d, "dollar", 20, 10, 5);
        serverClient.addIngredient("Ingredient2", "Supplier2", 4d, "dollar", 10, 5, 5);
        serverClient.addIngredient("Ingredient3", "Supplier3", 6d, "dollar", 42, 10, 5);
        serverClient.addIngredient("Ingredient4", "Supplier1", 1d, "dollar", 62, 15, 5);

        serverClient.addDish("Dish1", 40d, 3d, "Dollar", 7, 2, 10, 20);
        serverClient.addIngredientToDish("Dish1", "Ingredient1", 3);
        serverClient.addIngredientToDish("Dish1", "Ingredient2", 2);
        serverClient.addIngredientToDish("Dish1", "Ingredient3", 5);

        serverClient.addDish("Dish2", 52d, 3.5d, "Dollar", 10, 7, 15, 20);
        serverClient.addIngredientToDish("Dish2", "Ingredient1", 3);
        serverClient.addIngredientToDish("Dish2", "Ingredient2", 2);
        serverClient.addIngredientToDish("Dish2", "Ingredient3", 5);

        serverClient.startServerClient();

    }

    public static void main(String[] args) {
        ServerStart server = new ServerStart();
        server.initialize();

    }
}
