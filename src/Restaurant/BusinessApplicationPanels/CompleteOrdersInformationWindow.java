package Restaurant.BusinessApplicationPanels;

import Restaurant.Order;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CompleteOrdersInformationWindow extends JPanel {

    Order order;

    public CompleteOrdersInformationWindow(Order order) {
        this.order = order;
        this.setLayout(new BorderLayout());
        JLabel IDLabel = new JLabel(order.ID.toString());

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
        topPanel.add(IDLabel);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(dishPanel, BorderLayout.CENTER);
    }
}
