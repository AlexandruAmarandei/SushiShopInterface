package Restaurant.BusinessApplicationPanels;

import Restaurant.Ingredient;
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
import javax.swing.JScrollPane;

public class IngredientItem extends JPanel {

    ServerClient serverClient;
    Ingredient ingredient;

    public void changeIngredient() {
        String[] supplier = serverClient.getSupplierNameList();
        IngredientRegister ir = new IngredientRegister(supplier);
        ir.name.setEditable(false);
        int answear = JOptionPane.showConfirmDialog(this, ir,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (answear == JOptionPane.OK_OPTION) {
            String name = ingredient.name;
            String supplierName = ingredient.supplier;
            Double price = ingredient.price;
            String currency = ingredient.currency;

            Integer restockLvl = ingredient.restockLvl;
            Integer restockAmount = ingredient.restockSize;
            Integer stock = ingredient.stock;

            if (ir.supplier.getSelectedIndex() != -1) {
                supplierName = ir.supplier.getSelectedItem().toString();
            }
            if (!ir.price.getText().isEmpty()) {
                price = Double.parseDouble(ir.price.getText());
            }
            if (!ir.currency.getText().isEmpty()) {
                currency = ir.currency.getText();
            }
            if (!ir.restockLvl.getText().isEmpty()) {
                restockLvl = Integer.parseInt(ir.restockLvl.getText());
            }
            if (!ir.restockAmount.getText().isEmpty()) {
                restockAmount = Integer.parseInt(ir.restockAmount.getText());
            }
            if (!ir.initialStock.getText().isEmpty()) {
                stock = Integer.parseInt(ir.initialStock.getText());
            }
            serverClient.changeIngredient(name, supplierName, price, currency, stock, restockLvl, restockAmount);

        }
    }

    public IngredientItem(Ingredient ingredient, ServerClient serverClient) {
        this.serverClient = serverClient;
        this.ingredient = ingredient;
        this.setLayout(new BorderLayout());
        JLabel nameLabel = new JLabel("Name: " + ingredient.name);
        JLabel stockLabel = new JLabel("Stock: " + Integer.toString(ingredient.stock));
        JLabel restockLvlLabel = new JLabel("Restock: " + Integer.toString(ingredient.restockLvl));

        JLabel priceLabel = new JLabel("Price: " + ingredient.price.toString() + ingredient.currency);
        JLabel supplierLabel = new JLabel("  Supplier: " + ingredient.supplier);
        JLabel restockSizeLabel = new JLabel("Restock size: " + Integer.toString(ingredient.restockSize));

        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel stockPanel = new JPanel(new FlowLayout());
        JPanel restockLvlPanel = new JPanel(new FlowLayout());
        JPanel restockAmountPanel = new JPanel(new FlowLayout());
        JPanel pricePanel = new JPanel(new FlowLayout());
        JPanel supplierPanel = new JPanel(new FlowLayout());

        namePanel.add(nameLabel);
        stockPanel.add(stockLabel);
        restockLvlPanel.add(restockLvlLabel);
        restockAmountPanel.add(restockSizeLabel);
        pricePanel.add(priceLabel);
        supplierPanel.add(supplierLabel);

        JPanel infoPanel = new JPanel(new GridLayout(6, 1));
        infoPanel.add(namePanel);
        infoPanel.add(supplierPanel);
        infoPanel.add(pricePanel);
        infoPanel.add(stockPanel);
        infoPanel.add(restockLvlPanel);
        infoPanel.add(restockAmountPanel);

        JScrollPane scrollPane = new JScrollPane(infoPanel);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1));
        JPanel removeButtonPanel = new JPanel(new FlowLayout());
        JPanel changeButtonPanel = new JPanel(new FlowLayout());
        JPanel detailsButtonPanel = new JPanel(new FlowLayout());

        JButton changeButton = new JButton("Change ");
        JButton detailsButton = new JButton("Details");
        JButton removeButton = new JButton("Remove ");

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeIngredient();

            }
        });

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngredientInformationWindow iiw = new IngredientInformationWindow(ingredient);
                JOptionPane.showMessageDialog(null, iiw);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure that you want to remove the ingredient.\n Make sure it's not connected to any dishes!", "Confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    serverClient.removeIngredient(ingredient.name);
                }
            }
        });

        changeButtonPanel.add(changeButton);
        detailsButtonPanel.add(detailsButton);
        removeButtonPanel.add(removeButton);

        buttonsPanel.add(changeButtonPanel);
        buttonsPanel.add(detailsButtonPanel);
        buttonsPanel.add(removeButtonPanel);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.EAST);
    }
}
