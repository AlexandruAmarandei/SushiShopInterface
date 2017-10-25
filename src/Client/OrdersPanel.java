package Client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panels that contains the current and previous commands
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class OrdersPanel extends JPanel implements NotifiedClass {

    private JPanel currentOrdersPanel;
    private JPanel completeOrdersPanel;
    private Map<String, JPanel> completeOrders;
    private Map<String, JPanel> currentOrders;
    private Map<String, CompletedOrders> completedOrdersDetailes;
    private ClientApplication application;
    private JTimedLabel timer;

    public OrdersPanel() {
        completedOrdersDetailes = new HashMap<>();
        completeOrders = new HashMap<>();
        currentOrders = new HashMap<>();
        createOrdersPanel();
    }

    public final void createOrdersPanel() {
        this.setLayout(new GridLayout(2, 1));
        JLabel currentOrdersLabel = new JLabel("Current orders");
        JLabel completeOrdersLabel = new JLabel("Complete orders");
        currentOrdersPanel = new JPanel();
        completeOrdersPanel = new JPanel();
        JButton clearCompleteOrders = new JButton("Clear");
        clearCompleteOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeOrdersPanel.removeAll();
                completeOrdersPanel.setLayout(new GridLayout());
                for (String key : completeOrders.keySet()) {
                    currentOrders.remove(key);
                }
                completeOrdersPanel.updateUI();
                completeOrders.clear();
            }
        });
        JPanel lockCurrentPanel = new JPanel(new FlowLayout());
        JPanel lockCompletePanel = new JPanel(new FlowLayout());
        currentOrdersPanel.setLayout(new GridLayout());
        completeOrdersPanel.setLayout(new GridLayout());
        JPanel currentPanel = new JPanel(new BorderLayout());
        JPanel completePanel = new JPanel(new BorderLayout());
        lockCurrentPanel.add(currentOrdersPanel);
        lockCompletePanel.add(completeOrdersPanel);

        JScrollPane currentOrderScroll = new JScrollPane(lockCurrentPanel);
        JScrollPane completeOrderScroll = new JScrollPane(lockCompletePanel);
        currentPanel.add(currentOrdersLabel, BorderLayout.NORTH);
        currentPanel.add(currentOrderScroll, BorderLayout.CENTER);
        completePanel.add(completeOrdersLabel, BorderLayout.NORTH);
        completePanel.add(completeOrderScroll, BorderLayout.CENTER);
        completePanel.add(clearCompleteOrders, BorderLayout.SOUTH);

        this.add(currentPanel);
        this.add(completePanel);
    }

    public void addNewOrder(String orderID,
            String[] dishNames, int[] dishAmounts, double[] dishPrices) {

        completedOrdersDetailes.put(orderID, new CompletedOrders(dishNames, dishAmounts, dishPrices));

        JPanel panelToAdd = new JPanel(new BorderLayout());

        JLabel orderIDLabel = new JLabel(orderID);
        timer = new JTimedLabel("Pending...");
        JButton viewOrder = new JButton(" View ");
        viewOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderListView(orderID, dishNames, dishAmounts, dishPrices);
            }
        });
        JButton cancelOrder = new JButton("Cancel");
        cancelOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stopTimer();
                currentOrdersPanel.remove(panelToAdd);
                currentOrdersPanel.setLayout(new GridLayout(completeOrders.size() - 1, 1));
                currentOrdersPanel.repaint();
                currentOrders.remove(orderID);
                completedOrdersDetailes.remove(orderID);
                String[] orderIDS = new String[1];
                orderIDS[1] = orderID;
                application.sendMessage(orderIDS, 4);

            }
        });
        JPanel nameAndTimePanel = new JPanel(new GridLayout(2, 1));
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1));
        JPanel cancelPanel = new JPanel(new FlowLayout());
        JPanel viewPanel = new JPanel(new FlowLayout());

        cancelPanel.add(cancelOrder);
        viewPanel.add(viewOrder);
        nameAndTimePanel.add(orderIDLabel);
        nameAndTimePanel.add(timer);
        buttonsPanel.add(viewPanel);
        buttonsPanel.add(cancelPanel);

        panelToAdd.add(nameAndTimePanel, BorderLayout.WEST);
        panelToAdd.add(buttonsPanel, BorderLayout.EAST);
        currentOrdersPanel.setLayout(new GridLayout(currentOrders.size() + 1, 1));

        currentOrdersPanel.add(panelToAdd);
        currentOrders.put(orderID, panelToAdd);
    }

    public void completeOrder(String orderID, String state) {
        JPanel completedOrder = currentOrders.get(orderID);

        currentOrdersPanel.remove(completedOrder);
        currentOrdersPanel.setLayout(new GridLayout(completeOrders.size() - 1, 1));
        JPanel panelToAdd = new JPanel(new BorderLayout());

        JLabel orderIDLabel = new JLabel(orderID);
        JTimedLabel timer = new JTimedLabel(state);
        JButton viewOrder = new JButton(" View ");
        viewOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderListView(orderID, completedOrdersDetailes.get(orderID));
            }
        });
        JButton cancelOrder = new JButton("Delete");
        cancelOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeOrdersPanel.remove(panelToAdd);
                completeOrdersPanel.setLayout(new GridLayout(completeOrders.size() - 1, 1));
                completeOrdersPanel.repaint();
                completeOrders.remove(orderID);
                completedOrdersDetailes.remove(orderID);
            }
        });
        JPanel nameAndTimePanel = new JPanel(new GridLayout(2, 1));
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1));
        JPanel cancelPanel = new JPanel(new FlowLayout());
        JPanel viewPanel = new JPanel(new FlowLayout());

        cancelPanel.add(cancelOrder);
        viewPanel.add(viewOrder);
        nameAndTimePanel.add(orderIDLabel);
        nameAndTimePanel.add(timer);
        buttonsPanel.add(viewPanel);
        buttonsPanel.add(cancelPanel);

        panelToAdd.add(nameAndTimePanel, BorderLayout.WEST);
        panelToAdd.add(buttonsPanel, BorderLayout.EAST);
        completeOrdersPanel.setLayout(new GridLayout(completeOrders.size() + 1, 1));
        completeOrdersPanel.add(panelToAdd);
        //??????
        completeOrdersPanel.repaint();
        completeOrdersPanel.revalidate();
        completeOrders.put(orderID, panelToAdd);
        currentOrders.remove(orderID);
        this.repaint();
    }

    @Override
    public void notifyThis(Object notifiedClass) {
        timer = new JTimedLabel("Pending arrival");
        //completeOrder((String) notifiedClass);
    }

    private class CompletedOrders {

        String[] dishNames;
        double[] prices;
        int[] amounts;

        public CompletedOrders(String[] dishNames, int[] amounts, double[] prices) {
            this.dishNames = dishNames;
            this.amounts = amounts;
            this.prices = prices;
        }

    }

    public void updateOrder(String orderID, String command) {
        if (command.charAt(0) == '1') {
            int time = Integer.parseInt(command.substring(2)) / 1000;
            timer.resetTimer(time);
            updateUI();
        }
        if (command.charAt(0) == '2') {
            completeOrder(orderID, "Cancelled");
        }
        if (command.charAt(0) == '3') {
            completeOrder(orderID, "Arrived");
        }
        if (command.charAt(0) == '4') {
            completeOrder(orderID, "ServerCancel");
        }
    }

    private class OrderListView extends JFrame {

        String[] dishNames;
        double[] prices;
        int[] amounts;
        int size;

        public OrderListView(String title, String[] dishNames, int[] amounts, double[] prices) {
            setTitle(title);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.dishNames = dishNames;
            this.amounts = amounts;
            this.prices = prices;
            size = dishNames.length;
            addOrderList();
        }

        public OrderListView(String name, CompletedOrders order) {
            this(name, order.dishNames, order.amounts, order.prices);
        }

        public JPanel createJPanel(String name, int amount, double price) {
            JPanel returnPanel = new JPanel(new GridLayout(1, 2));
            JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel numbersPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JLabel nameLabel = new JLabel(name);
            JLabel numberLabel = new JLabel(Integer.toString(amount) + " X " + Double.toString(price));
            textPanel.add(nameLabel);
            numbersPanel.add(numberLabel);
            returnPanel.add(textPanel);
            returnPanel.add(numbersPanel);
            return returnPanel;
        }

        public void addOrderList() {
            Container content = getContentPane();
            JPanel mainPanel = new JPanel(new GridLayout(size + 1, 1));
            Double total = 0d;
            for (int i = 0; i < size; i++) {
                JPanel panelToAdd = createJPanel(dishNames[i], amounts[i], prices[i]);
                total += (double) amounts[i] * prices[i];
                mainPanel.add(panelToAdd);
            }
            JLabel totalLabel = new JLabel("Total: " + total.toString());
            mainPanel.add(totalLabel);
            content.add(mainPanel);
            pack();
            setVisible(true);
        }

    }

}
