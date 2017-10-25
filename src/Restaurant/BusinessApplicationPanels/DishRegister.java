package Restaurant.BusinessApplicationPanels;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DishRegister extends JPanel {

    public JTextField name, minTime, maxTime, restockLvl, initialStock, price, currency, weight;

    public DishRegister(String[] ingredientList) {

        name = new JTextField(15);
        weight = new JTextField(15);
        price = new JTextField(15);
        currency = new JTextField(15);
        minTime = new JTextField(15);
        maxTime = new JTextField(15);
        restockLvl = new JTextField(15);
        initialStock = new JTextField(15);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel weightLabel = new JLabel("Weight: ");
        JLabel priceLabel = new JLabel("Price: ");
        JLabel currencyLabel = new JLabel("Currency: ");
        JLabel minTimeLabel = new JLabel("Min Time: ");
        JLabel maxTimeLabel = new JLabel("Max Time: ");
        JLabel restockLvlLabel = new JLabel("Restock level: ");
        JLabel initialStockLabel = new JLabel("InitialStock: ");

        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel weightPanel = new JPanel(new FlowLayout());
        JPanel pricePanel = new JPanel(new FlowLayout());
        JPanel currencyPanel = new JPanel(new FlowLayout());
        JPanel minTimePanel = new JPanel(new FlowLayout());
        JPanel maxTimePanel = new JPanel(new FlowLayout());
        JPanel restockLvlPanel = new JPanel(new FlowLayout());
        JPanel restockAmountPanel = new JPanel(new FlowLayout());
        JPanel initialStockPanel = new JPanel(new FlowLayout());

        namePanel.add(nameLabel);
        namePanel.add(name);
        weightPanel.add(weightLabel);
        weightPanel.add(weight);
        pricePanel.add(priceLabel);
        pricePanel.add(price);
        currencyPanel.add(currencyLabel);
        currencyPanel.add(currency);
        minTimePanel.add(minTimeLabel);
        minTimePanel.add(minTime);
        maxTimePanel.add(maxTimeLabel);
        maxTimePanel.add(maxTime);

        restockLvlPanel.add(restockLvlLabel);
        restockLvlPanel.add(restockLvl);
        initialStockPanel.add(initialStockLabel);
        initialStockPanel.add(initialStock);

        this.setLayout(new GridLayout(9, 1));
        this.add(namePanel);
        this.add(weightPanel);
        this.add(pricePanel);
        this.add(currencyPanel);
        this.add(minTimePanel);
        this.add(maxTimePanel);
        this.add(initialStockPanel);
        this.add(restockLvlPanel);
        this.add(restockAmountPanel);
    }

}
