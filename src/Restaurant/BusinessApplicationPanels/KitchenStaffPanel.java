package Restaurant.BusinessApplicationPanels;

import Restaurant.KitchenStaff;
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

public class KitchenStaffPanel extends JPanel {

    private ServerClient serverClient;
    private JPanel itemPanel;

    public KitchenStaffPanel(ServerClient serverClient) {
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

    public void addNewStaff() {

        KitchenStaffRegister ir = new KitchenStaffRegister();
        int answer = JOptionPane.showConfirmDialog(null, ir,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (answer == JOptionPane.OK_OPTION) {
            try {
                String name = ir.name.getText();
                serverClient.addKitchenStaff(name);
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
        JButton addNewStaff = new JButton("Add");
        addNewStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewStaff();
            }
        });
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPanel();
            }
        });
        JPanel addStaffPanel = new JPanel(new FlowLayout());
        JPanel refreshPanel = new JPanel(new FlowLayout());

        addStaffPanel.add(addNewStaff);
        refreshPanel.add(refreshButton);
        JPanel northBorderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        northBorderPanel.add(addStaffPanel);
        northBorderPanel.add(refreshPanel);
        this.add(northBorderPanel, BorderLayout.NORTH);

    }

    public void addItems() {
        KitchenStaff[] staff = serverClient.getKitchenStaff();

        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
        if (staff != null) {
            JPanel staffPanel = new JPanel(new GridLayout(staff.length, 1));
            for (KitchenStaff worker : staff) {
                staffPanel.add(new KitchenStaffItem(worker, serverClient, this));
            }
            staffPanel.setMaximumSize(new Dimension(staffPanel.getMaximumSize().width, staffPanel.getPreferredSize().height));

            itemPanel.add(staffPanel);
        }

    }
}
