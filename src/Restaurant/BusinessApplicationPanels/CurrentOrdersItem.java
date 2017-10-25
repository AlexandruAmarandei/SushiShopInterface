package Restaurant.BusinessApplicationPanels;

import Restaurant.Order;
import Restaurant.OrderTimer;
import Restaurant.ServerClient;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class CurrentOrdersItem extends JPanel {

    CurrentOrdersItem(Order order, OrderTimer orderTimer, ServerClient serverClient, CurrentOrdersPanel currentOrdersPanel) {
        this.setLayout(new BorderLayout());
        JPanel orderIDPanel = new JPanel(new FlowLayout());
        JPanel timePanel = new JPanel(new FlowLayout());
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        JButton detailsButton = new JButton("Details");
        JButton cancelOrderButton = new JButton("Cancel");
        JPanel detailsPanel = new JPanel(new FlowLayout());
        JPanel cancelPanel = new JPanel(new FlowLayout());

        JLabel idLabel = new JLabel(order.ID.toString());
        Long remainingTime = orderTimer.getTime();
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime) % 60;
        JLabel timeLabel = new JLabel("Remaining: " + minutes.toString() + ":" + seconds.toString());

        detailsPanel.add(detailsButton);
        cancelPanel.add(cancelOrderButton);
        orderIDPanel.add(idLabel);
        timePanel.add(timeLabel);

        infoPanel.add(orderIDPanel);
        infoPanel.add(timePanel);
        buttonPanel.add(detailsPanel);
        buttonPanel.add(cancelPanel);

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentOrdersInformationWindow infoWindow = new CurrentOrdersInformationWindow(order);
                JOptionPane.showMessageDialog(null, infoWindow);

            }
        });

        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverClient.cancelOrder(order.ID);
                currentOrdersPanel.refreshValues();
            }
        });

        this.add(infoPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);

    }

}
