package Restaurant.BusinessApplicationPanels;

import Restaurant.Order;
import Restaurant.ServerClient;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.*;

public class CompleteOrdersPanel extends JPanel {

    private ServerClient serverClient;
    private JPanel itemPanel;

    public CompleteOrdersPanel(ServerClient serverClient) {
        this.serverClient = serverClient;

        createPanel();
    }

    public void createPanel() {
        itemPanel = new JPanel();
        this.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton removeAllButton = new JButton("Remove all");
        JButton refreshButton = new JButton("Refresh");
        topPanel.add(removeAllButton);
        topPanel.add(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPanel();
            }
        });
        removeAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverClient.removeAllCompleteOrders();
                refreshPanel();
            }
        });

        ArrayList<Order> orders = serverClient.getCompletedOrders();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
        if (!orders.isEmpty()) {
            JPanel ordersPanel = new JPanel(new GridLayout(orders.size(), 1));
            for (Order order : orders) {
                ordersPanel.add(new CompleteOrdersItem(order, serverClient, this));
            }
            ordersPanel.setMaximumSize(new Dimension(ordersPanel.getMaximumSize().width, ordersPanel.getPreferredSize().height));

            JScrollPane scrollPane = new JScrollPane(ordersPanel);
            itemPanel.add(scrollPane, BorderLayout.CENTER);
        }
        itemPanel.add(topPanel, BorderLayout.NORTH);

        this.add(itemPanel);
    }

    public void refreshPanel() {

        itemPanel.removeAll();
        createPanel();

        this.updateUI();

    }
}
