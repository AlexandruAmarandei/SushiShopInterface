package Restaurant.BusinessApplicationPanels;

import Restaurant.Dish;
import Restaurant.ServerClient;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class DishItem extends JPanel {

    Dish dish;
    ServerClient serverClient;

    public DishItem(Dish dish, ServerClient serverClient) {
        this.dish = dish;
        this.serverClient = serverClient;
        this.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("Name: " + dish.name);
        JLabel weigthLabel = new JLabel("Weight: " + Double.toString(dish.weight));
        JLabel priceLabel = new JLabel("Price: " + Double.toString(dish.price));
        JLabel currencyLabel = new JLabel(" : " + dish.currency);
        JLabel stockLabel = new JLabel("Stock: " + Integer.toString(dish.stock));
        JLabel restockLabel = new JLabel("Restock: " + Integer.toString(dish.restockLvl));
        JLabel minTimeLabel = new JLabel("Time: " + dish.lowerBound.toString());
        JLabel maxTimeLabel = new JLabel(" : " + dish.upperBound.toString());

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel weigthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel restockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        namePanel.add(nameLabel);
        weigthPanel.add(weigthLabel);
        pricePanel.add(priceLabel);
        pricePanel.add(currencyLabel);
        stockPanel.add(stockLabel);
        restockPanel.add(restockLabel);
        timePanel.add(minTimeLabel);
        timePanel.add(maxTimeLabel);

        JPanel infoPanel = new JPanel(new GridLayout(6, 1));
        infoPanel.add(namePanel);
        infoPanel.add(weigthPanel);
        infoPanel.add(pricePanel);
        infoPanel.add(stockPanel);
        infoPanel.add(restockPanel);
        infoPanel.add(timePanel);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1));

        JButton addIngredient = new JButton("Add Ing");
        JButton removeIngredient = new JButton("Remove Ing");
        JButton removeButton = new JButton("Remove");
        JButton detailsButton = new JButton("Details");
        JButton changeButton = new JButton("Change");
        JCheckBox availableCheckBox = new JCheckBox("Dish available");
        availableCheckBox.setSelected(dish.available);

        addIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIngredientToDish();

            }
        });
        removeIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeIngredientFromDish();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure that you want to remove the dish.\n", "Confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    serverClient.removeDish(dish.name);
                }
            }
        });
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DishInformationWindow diw = new DishInformationWindow(dish);
                JOptionPane.showMessageDialog(null, diw);
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDishInfo();
            }
        });
        availableCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverClient.makeDishAvailable(dish.name, availableCheckBox.isSelected());
            }
        });

        JPanel addIngPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel removeIngPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel changePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addIngPanel.add(addIngredient);
        removeIngPanel.add(removeIngredient);
        removePanel.add(removeButton);
        detailsPanel.add(detailsButton);
        changePanel.add(changeButton);
        checkBoxPanel.add(availableCheckBox);

        buttonPanel.add(addIngPanel);
        buttonPanel.add(removeIngPanel);
        buttonPanel.add(removePanel);
        buttonPanel.add(detailsPanel);
        buttonPanel.add(changePanel);
        buttonPanel.add(checkBoxPanel);

        JScrollPane scroll = new JScrollPane(infoPanel);
        this.add(infoPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);
    }

    public void addIngredientToDish() {
        JPanel inputData = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] ingridients = serverClient.getIngredientNames();
        JComboBox ingredientComboBox = new JComboBox(ingridients);
        JLabel amountLabel = new JLabel("Amount: ");
        JTextField amountField = new JTextField(3);
        inputData.add(amountLabel);
        inputData.add(amountField);
        inputData.add(ingredientComboBox);
        int result = JOptionPane.showConfirmDialog(null, inputData,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String ingredient = ingredientComboBox.getSelectedItem().toString();
            int amount = Integer.parseInt(amountField.getText());
            serverClient.addIngredientToDish(dish.name, ingredient, amount);
        }
    }

    public void removeIngredientFromDish() {
        JPanel inputData = new JPanel(new FlowLayout(FlowLayout.LEFT));

        ComboBoxModel[] models = new ComboBoxModel[dish.ingredients.size()];
        int cnt = 0;
        for (String ingridient : dish.ingredients.keySet()) {
            Integer amount = dish.ingredients.get(ingridient);
            Integer[] possiblities = new Integer[amount + 1];
            for (int i = 0; i < amount + 1; i++) {
                possiblities[i] = i;
            }
            models[cnt++] = new DefaultComboBoxModel(possiblities);
        }

        String[] ingridients = dish.ingredients.keySet().toArray(new String[dish.ingredients.size()]);
        JComboBox ingridientComboBox = new JComboBox(ingridients);
        JComboBox amountComboBox = new JComboBox();
        ingridientComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = ingridientComboBox.getSelectedIndex();
                if (choice >= 0) {
                    amountComboBox.setModel(models[choice]);
                }
            }
        });
        inputData.add(amountComboBox);
        inputData.add(ingridientComboBox);
        int result = JOptionPane.showConfirmDialog(null, inputData,
                "Please input Supplier details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String ingredient = ingridientComboBox.getSelectedItem().toString();
            int amount = amountComboBox.getSelectedIndex();
            serverClient.removeIngredientFromDish(dish.name, ingredient, amount);
        }
    }

    public void changeDishInfo() {
        String[] ingredientList = serverClient.getIngredientNames();
        DishRegister dr = new DishRegister(ingredientList);
        dr.name.setEditable(false);
        int answer = JOptionPane.showConfirmDialog(null, dr,
                "Please input Dish details", JOptionPane.OK_CANCEL_OPTION);
        if (answer == JOptionPane.OK_OPTION) {
            try {
                double weight = dish.weight;
                String currency = dish.currency;
                double price = dish.price;
                int stock = dish.stock;
                int restock = dish.restockLvl;
                int minTime = dish.lowerBound;
                int maxTime = dish.upperBound;

                if (!dr.weight.getText().isEmpty()) {
                    weight = Double.parseDouble(dr.weight.getText());
                }
                if (!dr.currency.getText().isEmpty()) {
                    currency = dr.currency.getText();
                }
                if (!dr.price.getText().isEmpty()) {
                    price = Double.parseDouble(dr.price.getText());
                }
                if (!dr.initialStock.getText().isEmpty()) {
                    stock = Integer.parseInt(dr.initialStock.getText());
                }
                if (!dr.restockLvl.getText().isEmpty()) {
                    restock = Integer.parseInt(dr.restockLvl.getText());
                }
                if (!dr.minTime.getText().isEmpty()) {
                    minTime = Integer.parseInt(dr.minTime.getText());
                }
                if (!dr.maxTime.getText().isEmpty()) {
                    maxTime = Integer.parseInt(dr.maxTime.getText());
                }

                serverClient.changeDish(dish.name, weight, price, currency, stock, restock, minTime, maxTime);
            } catch (NumberFormatException e) {
                System.out.println("Error, character in number field");
            }

        }
    }
}
