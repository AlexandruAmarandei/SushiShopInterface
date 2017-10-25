package Restaurant.BusinessApplicationPanels;

import Restaurant.ServerClient;
import Restaurant.Supplier;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SupplierItem extends JPanel {

    Supplier supplier;
    ServerClient serverClient;

    public void changeSupplier() {

        SupplierRegister sr = new SupplierRegister();
        sr.namePane.setEditable(false);
        int answear = JOptionPane.showConfirmDialog(this, sr,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (answear == JOptionPane.OK_OPTION) {
            String name = supplier.name;
            Double distance = supplier.distance;
            if (!sr.distancePane.getText().isEmpty()) {
                distance = Double.parseDouble(sr.distancePane.getText());
            }
            serverClient.changeSupplier(name, distance);

        }
    }

    public SupplierItem(Supplier supplier, ServerClient serverClient) {
        this.supplier = supplier;
        this.serverClient = serverClient;
        this.setLayout(new BorderLayout());
        JPanel nameAndDistancePanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel("Name: " + supplier.name);
        JLabel distanceLabel = new JLabel("Distance: " + supplier.distance.toString());
        nameAndDistancePanel.add(nameLabel);
        nameAndDistancePanel.add(distanceLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        JPanel removeButtonPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remove ");
        JButton changeButton = new JButton("Change");
        JPanel changeButtonPanel = new JPanel(new FlowLayout());

        JPanel detailsButtonPanel = new JPanel(new FlowLayout());
        JButton detailsButton = new JButton("Details");
        removeButtonPanel.add(removeButton);
        changeButtonPanel.add(changeButton);
        detailsButtonPanel.add(detailsButton);
        buttonPanel.add(removeButtonPanel);
        buttonPanel.add(detailsButtonPanel);
        buttonPanel.add(changeButtonPanel);
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure that you want to remove the supplier.\n Make sure it's not connected to any ingredients!", "Confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    serverClient.removeIngredient(supplier.name);
                }

            }
        });

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SupplierInformationWindow siw = new SupplierInformationWindow(supplier);
                JOptionPane.showMessageDialog(null, siw);
            }
        });

        JScrollPane scrollPane = new JScrollPane(nameAndDistancePanel);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);
    }
}
