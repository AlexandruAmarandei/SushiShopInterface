package Restaurant.BusinessApplicationPanels;

import Restaurant.ServerClient;
import Restaurant.Supplier;
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

public class SuppliersPanel extends JPanel {

    private ServerClient serverClient;
    private JPanel itemPanel;

    public SuppliersPanel(ServerClient serverClient) {
        this.serverClient = serverClient;
        createPanel();
    }

    public final void createPanel() {
        this.setLayout(new BorderLayout());
        itemPanel = new JPanel();
        addTopButtons();
        addItems();
        JScrollPane scroll = new JScrollPane(itemPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scroll, BorderLayout.CENTER);

    }

    public void addNewSupplier() {
        SupplierRegister sp = new SupplierRegister();
        int answear = JOptionPane.showConfirmDialog(this, sp,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (answear == JOptionPane.OK_OPTION) {
            String name = sp.namePane.getText();
            try {
                Double distance = Double.parseDouble(sp.distancePane.getText());

                serverClient.addSupplier(name, distance);
                refreshPanel();
            } catch (NumberFormatException ex) {
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
        JButton addSupplierButton = new JButton("Add");
        addSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewSupplier();
            }
        });
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPanel();
            }
        });
        JPanel addSupplierPanel = new JPanel(new FlowLayout());
        JPanel refreshPanel = new JPanel(new FlowLayout());

        addSupplierPanel.add(addSupplierButton);
        refreshPanel.add(refreshButton);
        JPanel northBorderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        northBorderPanel.add(addSupplierButton);
        northBorderPanel.add(refreshPanel);
        this.add(northBorderPanel, BorderLayout.NORTH);
    }

    public void addItems() {

        Supplier[] suppliers = serverClient.getSupplierList();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
        if (suppliers != null) {
            JPanel supplierPanel = new JPanel(new GridLayout(suppliers.length, 1));
            for (Supplier supplier : suppliers) {
                supplierPanel.add(new SupplierItem(supplier, serverClient));
            }
            supplierPanel.setMaximumSize(new Dimension(supplierPanel.getMaximumSize().width, supplierPanel.getPreferredSize().height));

            itemPanel.add(supplierPanel);
        }

    }
}
