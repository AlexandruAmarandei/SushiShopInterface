package Restaurant.BusinessApplicationPanels;

import Restaurant.Dish;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DishInformationWindow extends JPanel {

    Dish dish;

    DishInformationWindow(Dish dish) {
        this.dish = dish;

        this.setLayout(new BorderLayout());
        JLabel dishNameLabel = new JLabel(dish.name);
        JLabel stockLabel = new JLabel(Integer.toString(dish.stock));
        JPanel ingredientsPanel = new JPanel(new GridLayout(dish.ingredients.size(), 1));
        for (String ingredient : dish.ingredients.keySet()) {
            JPanel tempPanel = new JPanel(new GridLayout(1, 2));
            JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel amountLabel = new JLabel(dish.ingredients.get(ingredient).toString());
            JLabel nameLabel = new JLabel(ingredient + ": ");
            amountPanel.add(amountLabel);
            namePanel.add(nameLabel);
            tempPanel.add(namePanel);
            tempPanel.add(amountPanel);
            ingredientsPanel.add(tempPanel);
        }
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(dishNameLabel);
        topPanel.add(stockLabel);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(ingredientsPanel, BorderLayout.CENTER);
    }
}
