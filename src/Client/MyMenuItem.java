package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JPanel;

/**
 * An item in the menuFrame that represents just a dish.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class MyMenuItem extends JPanel {

    String name, price, currency, stock;
    ArrayList<String> ingredients;
    BasketPanel basket;

    public final String getNextField(String s) {
        return s.substring(0, s.indexOf('$'));
    }

    public final String deleteLastField(String s) {
        s = s.substring(s.indexOf('$') + 1);
        return s;
    }

    public final boolean hasNextField(String s) {
        return s.contains("$");
    }

    public MyMenuItem(BasketPanel basket, String s) {
        ArrayList<String> ingredients;
        String name = getNextField(s);
        s = deleteLastField(s);
        String price = getNextField(s);
        s = deleteLastField(s);
        String currency = getNextField(s);
        ingredients = new ArrayList<>();
        s = deleteLastField(s);
        while (hasNextField(s)) {
            ingredients.add(getNextField(s));
            s = deleteLastField(s);

        }
        String stock = s;
        assignValues(basket, name, price, currency, ingredients, stock);
        createPanel();
    }

    public MyMenuItem(BasketPanel basket, String name, String price, String currency, ArrayList<String> ingredients, String stock) {
        assignValues(basket, name, price, currency, ingredients, stock);
        createPanel();
    }

    public final void assignValues(BasketPanel basket, String name, String price, String currency, ArrayList<String> ingredients, String stock) {
        this.basket = basket;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.ingredients = ingredients;
        this.stock = stock;

    }

    private void createPanel() {
        JLabel nameLabel = new JLabel(name);
        JLabel priceLabel = new JLabel(price);
        JLabel currencyLabel = new JLabel(currency);

        JLabel stockLabel = new JLabel("Stock: " + stock);

        this.setLayout(new BorderLayout());

        JPanel itemDescription = new JPanel();
        JPanel nameAndPricePanel = new JPanel();
        nameAndPricePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel ingredientsPanel = new JPanel();

        ingredientsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        nameAndPricePanel.add(nameLabel);
        nameAndPricePanel.add(priceLabel);
        nameAndPricePanel.add(currencyLabel);
        String allIngredients = "";
        for (String ingredient : ingredients) {
            allIngredients = allIngredients + ingredient + ", ";
        }
        JLabel ingredientsPane = new JLabel();
        ingredientsPane.setText(allIngredients);
        ingredientsPanel.add(ingredientsPane);
        JPanel test = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemDescription.setLayout(new GridLayout(3, 1));
        itemDescription.add(nameAndPricePanel);
        itemDescription.add(ingredientsPanel);
        itemDescription.add(stockLabel);

        JPanel buttonsPanel = new JPanel();
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel addItemPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setLayout(new GridLayout(2, 1));
        JComboBox<Integer> amountCombo = new JComboBox<>();
        int stockNumber = Integer.parseInt(stock);
        for (int i = 0; i < stockNumber; i++) {
            amountCombo.addItem(i);
        }
        JButton addToBasketButton = new JButton("Add");
        addToBasketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer amount = (Integer) amountCombo.getSelectedItem();
                String amountString = amount.toString();
                basket.addItemToBasket(name, price, currency, amountString, Integer.parseInt(stock));
            }
        });
        comboBoxPanel.add(amountCombo);
        addItemPanel.add(addToBasketButton);
        buttonsPanel.add(comboBoxPanel);
        buttonsPanel.add(addItemPanel);
        test.add(itemDescription);
        JScrollPane scrollPane = new JScrollPane(itemDescription, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.EAST);
        this.setVisible(true);

    }
}
