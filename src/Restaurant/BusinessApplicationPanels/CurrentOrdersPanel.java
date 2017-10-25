package Restaurant.BusinessApplicationPanels;

import Restaurant.Order;
import Restaurant.OrderTimer;
import Restaurant.ServerClient;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CurrentOrdersPanel extends JPanel implements Runnable {

    private ServerClient serverClient;
    JPanel items;

    public CurrentOrdersPanel(ServerClient serverClient) {
        this.serverClient = serverClient;
        Thread t = new Thread(this);

        createPanel();
    }

    public final void createPanel() {
        this.setLayout(new FlowLayout());

        addItems();
        this.add(items);
    }

    public void addItems() {
        items = new JPanel();
        Order[] currentOrder = serverClient.getCurrentOrders();
        HashMap<Integer, OrderTimer> currentTimes = serverClient.getCurrentStartTimes();
        items.setLayout(new BoxLayout(items, BoxLayout.PAGE_AXIS));
        if (currentOrder != null && currentOrder.length != 0) {
            JPanel currentOrders = new JPanel(new GridLayout(currentOrder.length, 1));
            for (Order order : currentOrder) {
                currentOrders.add(new CurrentOrdersItem(order, currentTimes.get(order.ID), serverClient, this));
            }
            currentOrders.setMaximumSize(new Dimension(currentOrders.getMaximumSize().width, currentOrders.getPreferredSize().height));

            JScrollPane scrollPane = new JScrollPane(currentOrders);

            items.add(scrollPane);
        }
    }

    public synchronized void refreshValues() {
        items.removeAll();

        addItems();
        updateUI();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000);
                refreshValues();
            } catch (InterruptedException ex) {
                Logger.getLogger(CurrentOrdersPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
