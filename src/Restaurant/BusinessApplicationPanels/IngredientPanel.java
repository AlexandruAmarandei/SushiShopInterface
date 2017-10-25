package Restaurant.BusinessApplicationPanels;

import Restaurant.Ingredient;
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

public class IngredientPanel extends JPanel {

    private ServerClient serverClient;
    private JPanel itemPanel;

    public IngredientPanel(ServerClient serverClient) {
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

    public void addNewIngredient() {
        String[] supplier = serverClient.getSupplierNameList();
        IngredientRegister ir = new IngredientRegister(supplier);
        int answear = JOptionPane.showConfirmDialog(this, ir,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (answear == JOptionPane.OK_OPTION) {
            try {
                String name = ir.name.getText();
                String supplierName = supplier[ir.supplier.getSelectedIndex()];
                Double price = Double.parseDouble(ir.price.getText());
                String currency = ir.currency.getText();

                Integer restockLvl = Integer.parseInt(ir.restockLvl.getText());
                Integer restockAmount = Integer.parseInt(ir.restockAmount.getText());
                Integer stock = Integer.parseInt(ir.initialStock.getText());
                serverClient.addIngredient(name, supplierName, price, currency, stock, restockLvl, restockAmount);
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
        JButton addSupplierButton = new JButton("Add");
        addSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewIngredient();
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
        Ingredient[] ingredients = serverClient.getIngredientList();

        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
        if (ingredients != null) {
            JPanel ingredientPanel = new JPanel(new GridLayout(ingredients.length, 1));
            for (Ingredient ingredient : ingredients) {
                ingredientPanel.add(new IngredientItem(ingredient, serverClient));
            }
            ingredientPanel.setMaximumSize(new Dimension(ingredientPanel.getMaximumSize().width, ingredientPanel.getPreferredSize().height));

            itemPanel.add(ingredientPanel);
        }

    }
}
