package Restaurant.BusinessApplicationPanels;

import Restaurant.Order;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CurrentOrdersInformationWindow extends JLabel {

    Order order;

    public CurrentOrdersInformationWindow(Order order) {
        this.order = order;
        this.setLayout(new BorderLayout());
        JLabel orderIDLabel = new JLabel("ID: " + order.ID.toString());
        JLabel distanceLabel = new JLabel("Distance: " + Double.toString(order.distance));
        JLabel usernameLabel = new JLabel("Username: " + order.username);
        JLabel totalLabel = new JLabel("Total: " + Integer.toString(order.subtotal));

        JPanel dishPanel = new JPanel(new GridLayout(order.items.size(), 1));
        for (String dishName : order.items.keySet()) {
            JPanel tempPanel = new JPanel(new GridLayout(1, 2));
            JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel amountLabel = new JLabel(order.items.get(dishName).toString());
            JLabel nameLabel = new JLabel(dishName + ": ");
            amountPanel.add(amountLabel);
            namePanel.add(nameLabel);
            tempPanel.add(namePanel);
            tempPanel.add(amountPanel);
            dishPanel.add(tempPanel);
        }
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(orderIDLabel);
        topPanel.add(distanceLabel);
        topPanel.add(usernameLabel);
        topPanel.add(totalLabel);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(dishPanel, BorderLayout.CENTER);
    }
}
