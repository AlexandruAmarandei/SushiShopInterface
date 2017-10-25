package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This class represents the basket. Once an order is succeseful, it will create
 * the current orders panel.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
class BasketPanel extends JPanel {

    private Map<String, JPanelAndLabel> basketItems;
    private Map<String, Double> totalSemiSum;
    private int subtotal = 0;
    private JLabel subtotalLabel;
    private JPanel lockItemList;
    private JScrollPane menuScroll;
    private JPanel itemList;
    private ClientClient clientClient;

    public BasketPanel(ClientClient clientClient) {
        this.clientClient = clientClient;
        basketItems = new HashMap<>();
        totalSemiSum = new HashMap<>();
        this.setLayout(new BorderLayout());
        itemList = new JPanel(new GridLayout());
        lockItemList = new JPanel(new FlowLayout());
        lockItemList.add(itemList);
        menuScroll = new JScrollPane(lockItemList);

        this.add(menuScroll, BorderLayout.CENTER);

        JPanel sendAndTotalPanel = new JPanel(new GridLayout(2, 1));
        JPanel subtotalPanel = new JPanel(new BorderLayout());
        JLabel totalLabel = new JLabel("Total:");
        subtotalLabel = new JLabel(Integer.toString(subtotal));
        JButton sendOrder = new JButton("Send Order");
        sendOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cnt = 0;
                String[] order = new String[basketItems.size() * 2];
                for (String item : basketItems.keySet()) {
                    order[cnt++] = item;
                    order[cnt++] = basketItems.get(item).jlabel.getText();

                }
                clientClient.application.sendMessage(order, 1);
            }
        });
        subtotalPanel.add(totalLabel, BorderLayout.WEST);
        subtotalPanel.add(subtotalLabel, BorderLayout.EAST);
        sendAndTotalPanel.add(subtotalPanel);
        sendAndTotalPanel.add(sendOrder);
        this.add(sendAndTotalPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private boolean updateItem(String name, int amount, int stock) {
        JPanelAndLabel item = basketItems.get(name);
        int currentAmount = Integer.parseInt(item.jlabel.getText());
        if (amount + currentAmount > stock) {
            System.err.println("Too much of " + name);
            return false;
        }
        if (currentAmount + amount < 0) {
            System.err.println("Too little of " + name);
            return false;
        }

        currentAmount = currentAmount + amount;
        item.jlabel.setText(Integer.toString(currentAmount));

        return true;
    }

    private boolean updateItem(String name, String amount, int stock) {
        int amountToChange = Integer.parseInt(amount);
        return updateItem(name, amountToChange, stock);
    }

    public void updateTotal() {
        Double tempTotal = 0d;
        for (Double itemSubtotal : totalSemiSum.values()) {
            tempTotal += itemSubtotal;
        }
        subtotalLabel.setText(Double.toString(tempTotal));
    }

    public void addItemToBasket(String name, String price, String currency, String amount, int stock) {
        if (basketItems.containsKey(name)) {
            updateItem(name, amount, stock);
        } else {
            creatNewBasketItem(name, price, currency, amount, stock);
        }

    }

    private void creatNewBasketItem(String name, String price, String currency, String amount, int stock) {

        JPanel panelToAdd = new JPanel(new BorderLayout());
        JPanel nameAndPrice = new JPanel(new GridLayout(2, 1));
        JPanel editItem = new JPanel(new GridLayout(2, 1));
        JPanel editNumber = new JPanel(new FlowLayout());
        JPanel deleteButtonPanel = new JPanel(new FlowLayout());

        JLabel nameLabel = new JLabel(name);
        JLabel priceLabel = new JLabel(price + currency);

        JLabel amountLabel = new JLabel(amount);
        int currentAmount = Integer.parseInt(amount);
        double currentPrice = Double.parseDouble(price);
        totalSemiSum.put(name, currentPrice * currentAmount);
        amountLabel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                int currentAmount = Integer.parseInt(amountLabel.getText());
                double currentPrice = Double.parseDouble(price);
                totalSemiSum.put(name, (double) currentPrice * currentAmount);
                updateTotal();
            }
        });
        JButton decreaseButton = new JButton("-");
        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentAmount = Integer.parseInt(amountLabel.getText());
                if (currentAmount > 0) {
                    currentAmount--;
                }
                amountLabel.setText(Integer.toString(currentAmount));
            }
        });
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentAmount = Integer.parseInt(amountLabel.getText());
                if (currentAmount < stock) {
                    currentAmount++;
                }
                amountLabel.setText(Integer.toString(currentAmount));
            }
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemList.setLayout(new GridLayout(basketItems.size() - 1, 1));
                itemList.remove(panelToAdd);
                itemList.setLayout(new GridLayout(basketItems.size() - 1, 1));
                itemList.repaint();
                basketItems.remove(name);
                totalSemiSum.remove(name);
                updateTotal();
            }
        });
        nameAndPrice.add(nameLabel);
        nameAndPrice.add(priceLabel);
        editNumber.add(decreaseButton);
        editNumber.add(amountLabel);
        editNumber.add(addButton);

        deleteButtonPanel.add(deleteButton);
        editItem.add(editNumber);
        editItem.add(deleteButtonPanel);

        panelToAdd.add(nameAndPrice, BorderLayout.WEST);
        panelToAdd.add(editItem, BorderLayout.EAST);
        itemList.setLayout(new GridLayout(basketItems.size() + 1, 1));
        itemList.add(panelToAdd);
        basketItems.put(name, new JPanelAndLabel(panelToAdd, amountLabel));

    }

    public void addItemToBasket(MyMenuItem menuItem, int amount) {
        addItemToBasket(menuItem.name, menuItem.price, menuItem.currency, Integer.toString(amount), Integer.parseInt(menuItem.stock));
    }

    public void setSubtotal(int amount) {
        setSubtotal(Integer.toString(amount));
    }

    public void setSubtotal(String amount) {
        subtotalLabel.setText(amount);
    }

    private void emptyBasket() {

        itemList.removeAll();
        itemList.setLayout(new GridLayout());
        itemList.repaint();
        totalSemiSum.clear();
        basketItems.clear();
        this.updateUI();
    }

    public void orderSuccesefull(String orderID) {
        String[] dishNames;
        int[] dishAmounts;
        double[] dishPrices;
        dishNames = new String[basketItems.size()];
        dishAmounts = new int[basketItems.size()];
        dishPrices = new double[basketItems.size()];
        int counter = 0;
        for (String name : basketItems.keySet()) {
            dishNames[counter] = name;
            int amount = Integer.parseInt(basketItems.get(name).jlabel.getText());
            dishAmounts[counter] = amount;
            double price = totalSemiSum.get(name) / amount;
            dishPrices[counter] = price;
        }
        clientClient.ordersPanel.addNewOrder(orderID, dishNames, dishAmounts, dishPrices);
        emptyBasket();
    }

    public class JPanelAndLabel {

        public JPanel jpanel;
        public JLabel jlabel;

        public JPanelAndLabel(JPanel p, JLabel l) {
            jpanel = p;
            jlabel = l;
        }
    }
}
