package Restaurant.BusinessApplicationPanels;

import Restaurant.Drone;
import Restaurant.ServerClient;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DronePanel extends JPanel {

    private ServerClient serverClient;
    private JPanel itemPanel;

    public DronePanel(ServerClient serverClient) {
        this.serverClient = serverClient;

        createPanel();
    }

    public final void createPanel() {
        this.setLayout(new BorderLayout());
        itemPanel = new JPanel();
        addTopButtons();
        addItems();
        JScrollPane scroll = new JScrollPane(itemPanel);

        this.add(scroll, BorderLayout.CENTER);

    }

    public void addNewDrone() {

        DroneRegister ir = new DroneRegister();
        int answer = JOptionPane.showConfirmDialog(null, ir,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (answer == JOptionPane.OK_OPTION) {
            try {
                double speed = Double.parseDouble(ir.speed.getText());
                serverClient.addDrone(speed);
                refreshPanel();
            } catch (NumberFormatException e) {
                System.out.println("Error, character in number field");
            }

        }
    }

    public void refreshPanel() {
        itemPanel.removeAll();
        addItems();

        this.updateUI();
    }

    public void addTopButtons() {
        JButton addDroneButton = new JButton("Add");
        addDroneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewDrone();
            }
        });
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPanel();
            }
        });
        JPanel addDronePanel = new JPanel(new FlowLayout());
        JPanel refreshPanel = new JPanel(new FlowLayout());

        addDronePanel.add(addDroneButton);
        refreshPanel.add(refreshButton);
        JPanel northBorderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        northBorderPanel.add(addDronePanel);
        northBorderPanel.add(refreshPanel);
        this.add(northBorderPanel, BorderLayout.NORTH);

    }

    public void addItems() {
        Drone[] drones = serverClient.getDrones();

        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
        if (drones != null) {
            JPanel dronePanel = new JPanel(new GridLayout(drones.length, 1));
            for (Drone drone : drones) {
                dronePanel.add(new DroneItem(drone, serverClient, this));
            }
            dronePanel.setMaximumSize(new Dimension(dronePanel.getMaximumSize().width, dronePanel.getPreferredSize().height));

            itemPanel.add(dronePanel);
        }

    }
}
