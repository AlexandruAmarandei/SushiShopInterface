package Restaurant.BusinessApplicationPanels;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class IngredientRegister extends JPanel {

    public JTextField name, expectedRestock, minTime, maxTime, restockLvl, restockAmount, initialStock, price, currency;
    public JComboBox supplier;

    public IngredientRegister(String[] supplierList) {

        name = new JTextField(15);
        supplier = new JComboBox(supplierList);
        price = new JTextField(15);
        currency = new JTextField(15);

        restockLvl = new JTextField(15);
        restockAmount = new JTextField(15);
        initialStock = new JTextField(15);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel supplierLabel = new JLabel("Supplier: ");
        JLabel priceLabel = new JLabel("Price: ");
        JLabel currencyLabel = new JLabel("Currency: ");
        //JLabel minTimeLabel = new JLabel("Min Time: ");
        //JLabel maxTimeLabel = new JLabel("Max Time: ");
        JLabel restockLvlLabel = new JLabel("Restock level: ");
        JLabel restockAmountLabel = new JLabel("Restock amount: ");
        JLabel initialStockLabel = new JLabel("InitialStock: ");

        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel supplierPanel = new JPanel(new FlowLayout());
        JPanel pricePanel = new JPanel(new FlowLayout());
        JPanel currencyPanel = new JPanel(new FlowLayout());

        JPanel restockLvlPanel = new JPanel(new FlowLayout());
        JPanel restockAmountPanel = new JPanel(new FlowLayout());
        JPanel initialStockPanel = new JPanel(new FlowLayout());

        namePanel.add(nameLabel);
        namePanel.add(name);
        supplierPanel.add(supplierLabel);
        supplierPanel.add(supplier);
        pricePanel.add(priceLabel);
        pricePanel.add(price);
        currencyPanel.add(currencyLabel);
        currencyPanel.add(currency);

        restockLvlPanel.add(restockLvlLabel);
        restockLvlPanel.add(restockLvl);
        restockAmountPanel.add(restockAmountLabel);
        restockAmountPanel.add(restockAmount);
        initialStockPanel.add(initialStockLabel);
        initialStockPanel.add(initialStock);

        this.setLayout(new GridLayout(7, 1));
        this.add(namePanel);
        this.add(supplierPanel);
        this.add(pricePanel);
        this.add(currencyPanel);
        this.add(initialStockPanel);
        this.add(restockLvlPanel);
        this.add(restockAmountPanel);

    }
}
