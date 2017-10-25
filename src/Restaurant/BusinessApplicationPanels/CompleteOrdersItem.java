package Restaurant.BusinessApplicationPanels;

import Restaurant.Order;
import Restaurant.ServerClient;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CompleteOrdersItem extends JPanel {

    CompleteOrdersItem(Order order, ServerClient serverClient, CompleteOrdersPanel completeOrdersPanel) {
        this.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        JPanel informationPanel = new JPanel(new GridLayout(4, 1));

        JLabel orderIDLabel = new JLabel("ID: " + order.ID.toString());
        JLabel distanceLabel = new JLabel("Distance: " + Double.toString(order.distance));
        JLabel usernameLabel = new JLabel("Username: " + order.username);
        JLabel totalLabel = new JLabel("Total: " + Integer.toString(order.subtotal));

        JPanel orderIDPanel = new JPanel(new FlowLayout());
        JPanel distancePanel = new JPanel(new FlowLayout());
        JPanel usernamePanel = new JPanel(new FlowLayout());
        JPanel totalPanel = new JPanel(new FlowLayout());

        orderIDPanel.add(orderIDLabel);
        distancePanel.add(distanceLabel);
        usernamePanel.add(usernameLabel);
        totalPanel.add(totalLabel);

        informationPanel.add(orderIDPanel);
        informationPanel.add(distancePanel);
        informationPanel.add(usernamePanel);
        informationPanel.add(totalPanel);

        JPanel removeButtonPanel = new JPanel(new FlowLayout());
        JPanel detailsButtonPanel = new JPanel(new FlowLayout());

        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverClient.removeOrder(order.ID);
                completeOrdersPanel.refreshPanel();
            }
        });
        JButton details = new JButton("Details");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CompleteOrdersInformationWindow infoWindow = new CompleteOrdersInformationWindow(order);
                JOptionPane.showMessageDialog(null, infoWindow);

            }
        });

        removeButtonPanel.add(remove);
        detailsButtonPanel.add(details);

        buttonPanel.add(removeButtonPanel);
        buttonPanel.add(detailsButtonPanel);

        this.add(buttonPanel, BorderLayout.EAST);
        this.add(informationPanel, BorderLayout.CENTER);

    }

}
