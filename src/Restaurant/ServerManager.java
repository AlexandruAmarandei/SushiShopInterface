/*
 * This class is in charge of interpreting the input gained from the comms class.
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
package Restaurant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerManager implements Communicator, Runnable {

    public StockManager stockManager;
    public UserManager userManager;
    public Comms comms;
    public LinkedBlockingQueue<SendObject> messagesToSend;
    private String[] errorMessages;
    public HashMap<String, Double> postcodes;
    public OrderManager orderManager;
    public Thread thread;

    public ServerManager(StockManager stockManager, UserManager userManager, Comms comms, OrderManager orderManager) {
        this.stockManager = stockManager;
        this.userManager = userManager;
        messagesToSend = new LinkedBlockingQueue<>();
        this.comms = comms;
        thread = new Thread(this);
        errorMessages = new String[10];
        this.orderManager = orderManager;
        errorMessages[0] = "Inccorect login info";

    }

    public ServerManager() {
        messagesToSend = new LinkedBlockingQueue<>();
        thread = new Thread(this);
        errorMessages = new String[10];
        errorMessages[0] = "Inccorect login info";
    }

    public void setClassesAndStart(StockManager stockManager, UserManager userManager, Comms comms, OrderManager orderManager) {
        this.stockManager = stockManager;
        this.userManager = userManager;
        this.orderManager = orderManager;
        this.comms = comms;
        thread.start();
    }

    @Override
    public synchronized void sendMessage(int threadID, String message) {

        if (message.charAt(0) == '3' || message.charAt(0) == '2') {
            comms.sendMessage(threadID, message);
        }

        comms.sendMessage(threadID, message);

    }

    public String parseListOfDishes(Dish[] dishList) {
        String dishListString = "";
        for (Dish dish : dishList) {
            if (dish.available == true) {
                dishListString = dishListString.concat(dish.name + "$");
                dishListString = dishListString.concat(Double.toString(dish.price) + "$");
                dishListString = dishListString.concat(dish.currency + "$");

                for (String ingredientName : dish.ingredients.keySet()) {
                    dishListString = dishListString.concat(ingredientName + "$");
                }
                dishListString = dishListString.concat(Integer.toString(dish.stock) + "&");
            }
        }
        return dishListString;
    }

    @Override
    public synchronized void nextMessage(int ID, Message msg) {

        if (msg.command.equals("0")) {

            Iterator<String> it = msg.iterator(Message.DEFAULT_IDENTIFIER);
            String username = it.next();
            String password = it.next();

            if (userManager.checkLogInInfo(username, password, ID)) {
                String dishListString = parseListOfDishes(stockManager.getListOfDishes());
                String message = username + "$" + userManager.getUserName(username) + "$" + dishListString;
                messagesToSend.add(new SendObject(message, ID, 0, 1));
            } else {
                messagesToSend.add(new SendObject(errorMessages[0], ID, 0, 0));
            }

        }
        if (msg.command.equals("1")) {
            double distance = userManager.getDistance(msg.username);
            Order order = OrderManager.parseOrder(msg.content, msg.username, distance);
            order.threadIP = ID;
            if (stockManager.checkOrder(order)) {
                try {

                    stockManager.startOder(order);
                    messagesToSend.add(new SendObject(order.ID.toString(), ID, 1, 5));
                } catch (InvalidOrderException ex) {
                    System.out.println("Error, order not properlyverified");
                }
            } else {
                String[] message = stockManager.getOrderDifference(order);
                String returnMessage = message[0];
                for (int i = 1; i < message.length; i++) {
                    returnMessage = returnMessage + "$" + message[i];
                }
                returnMessage = returnMessage + "&" + parseListOfDishes(stockManager.getListOfDishes());
                messagesToSend.add(new SendObject(returnMessage, ID, 1, 4));
            }
        }
        if (msg.command.equals("2")) {
            Iterator<String> it = msg.iterator(Message.DEFAULT_IDENTIFIER);
            String username = it.next();
            String password = it.next();
            String name = it.next();
            String address = it.next();
            String email = it.next();
            String postcode = it.next();

            if (!userManager.checkIfUserExists(username)) {
                if (!userManager.checkIfEmailExists(email)) {
                    userManager.addUser(username, password, name, address, email, postcode);

                    messagesToSend.add(new SendObject("correct", ID, 2, 8));
                } else {
                    messagesToSend.add(new SendObject("email", ID, 2, 2));
                }
            } else {
                messagesToSend.add(new SendObject("username", ID, 2, 2));
            }
        }
        if (msg.command.equals("3")) {
            Set<String> postCodes = userManager.getPostCodes();
            Iterator it = postCodes.iterator();
            String message = "";
            while (it.hasNext()) {
                message = message + it.next();
                if (it.hasNext()) {
                    message = message + "$";
                }
            }
            messagesToSend.add(new SendObject(message, ID, 3, 3));

        }
        if (msg.command.equals("4")) {
            Integer orderID = Integer.parseInt(msg.content);

            stockManager.assigner.cancelOrderFromCustomer(orderID, msg.username);
        }
        if (msg.command.equals("5")) {
            messagesToSend.add(new SendObject(parseListOfDishes(stockManager.getListOfDishes()), ID, 5, 1));
        }
        if (msg.command.equals("6")) {
            userManager.loggedOut(msg.username);
        }
    }

    public void updateOrderStatus(String update, Integer orderID, Integer threadID) {

        messagesToSend.add(new SendObject(orderID.toString() + update, threadID, 7, 7));
    }

    @Override
    public void run() {
        while (true) {
            SendObject sendObject;
            try {
                sendObject = messagesToSend.take();
                String message = sendObject.commandDigit.toString() + sendObject.responseDigit.toString();
                message = message + sendObject.message;

                sendMessage(sendObject.threadID, message);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private class SendObject {

        public String message;
        public Integer threadID;
        public Integer commandDigit;
        public Integer responseDigit;

        public SendObject(String message, Integer threadID, Integer commandDigit, Integer responseDigit) {
            this.message = message;
            this.threadID = threadID;
            this.commandDigit = commandDigit;
            this.responseDigit = responseDigit;
        }
    }

}
