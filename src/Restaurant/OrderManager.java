package Restaurant;

/**
 * Manages the orders by saving them into files. Each file contains 100 orders.
 * In the users file, there is just the orderID.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderManager {

    UserManager userManager;
    public static final String DEFAULTPATHSERVER = System.getProperty("user.dir") + "\\server\\orders";
    private static Integer orderID = 0;

    public OrderManager(UserManager userManager) {
        File file = new File(DEFAULTPATHSERVER);
        if (!file.exists()) {
            file.mkdir();
        }
        this.userManager = userManager;
    }

    public OrderManager() {
        File file = new File(DEFAULTPATHSERVER);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void setClasses(UserManager userManager) {
        this.userManager = userManager;
    }

    public String getOrderFilePath(Integer orderID) {
        Integer nr = orderID / 100;
        String path = DEFAULTPATHSERVER + "\\" + nr.toString() + "00.txt";

        return path;
    }

    private void writeLineInFile(String path, String[] toWrite, boolean create) {
        File file = new File(path);

        if (!file.exists()) {
            if (create) {
                try {
                    PrintWriter pw = new PrintWriter(path, "UTF-8");
                    pw.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (file.exists()) {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
                for (String line : toWrite) {
                    pw.println(line);
                    pw.flush();
                }

                if (pw != null) {
                    pw.close();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addCompleteOrder(Order order) {
        String path = getOrderFilePath(order.ID);

        String orderString[] = new String[6];
        orderString[0] = order.ID.toString();
        orderString[1] = order.username;
        orderString[2] = Double.toString(order.distance);
        orderString[3] = Integer.toString(order.subtotal);
        String dishes = "";
        for (String dish : order.items.keySet()) {
            dishes = dish + " " + order.items.get(dish).toString() + " ";
        }
        orderString[4] = dishes;
        orderString[5] = "------------";
        writeLineInFile(path, orderString, true);
        userManager.writeorderIDForUser(order.username, order.ID.toString());
    }

    public void removeOrder(Integer orderID) {
        String path = getOrderFilePath(orderID);
        String tempPath = path.substring(0, path.length() - 4) + "temp.txt";
        File tempFile = new File(tempPath);
        try {
            PrintWriter pw = new PrintWriter(path, "UTF-8");
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = new File(path);
        if (file.exists()) {

            path = path + "orders.txt";
            try {
                BufferedReader br = new BufferedReader(new FileReader(path));
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
                String line = br.readLine();

                while (line != null) {
                    if (!line.equals(orderID.toString())) {
                        for (int i = 0; i < 6; i++) {
                            pw.println(line);
                            pw.flush();
                            line = br.readLine();
                        }
                    }
                }
                if (br != null) {
                    br.close();
                }
                if (pw != null) {
                    pw.close();
                }
                File oldFile = new File(path);
                oldFile.delete();
                File newFile = new File(tempPath);
                newFile.renameTo(oldFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void removeAllOrders() {
        File folder = new File(DEFAULTPATHSERVER);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    public ArrayList<Order> getAllCompleteOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        File folder = new File(DEFAULTPATHSERVER);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(file));

                    String line = br.readLine();

                    while (line != null) {
                        Order tempOrder = new Order();
                        tempOrder.ID = Integer.parseInt(line);
                        tempOrder.username = br.readLine();
                        tempOrder.distance = Double.parseDouble(br.readLine());
                        tempOrder.subtotal = Integer.parseInt(br.readLine());
                        String[] items = br.readLine().split(" ");
                        for (int i = 0; i < items.length - 1; i++) {
                            tempOrder.items.put(items[i], Integer.parseInt(items[i + 1]));
                        }
                        orders.add(tempOrder);
                        br.readLine();
                        line = br.readLine();
                    }
                    if (br != null) {
                        br.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(OrderManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OrderManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return orders;
    }

    public static Order parseOrder(String command, String username, double distance) {
        Order order = new Order();
        order.distance = distance;
        order.username = username;

        String[] copy = command.split("\\$");

        for (int i = 0; i < copy.length; i++) {
            String itemName = copy[i];
            Integer amount = Integer.parseInt(copy[i + 1]);
            i++;
            order.items.put(itemName, amount);
        }
        orderID++;
        order.ID = orderID;
        return order;
    }

}
