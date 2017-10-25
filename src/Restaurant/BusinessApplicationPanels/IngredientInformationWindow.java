package Restaurant.BusinessApplicationPanels;

import Restaurant.Ingredient;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IngredientInformationWindow extends JPanel {

    Ingredient ingredient;

    public IngredientInformationWindow(Ingredient ingredient) {
        this.ingredient = ingredient;

        this.setLayout(new BorderLayout());
        JLabel ingredientName = new JLabel(ingredient.name);
        JLabel supplierLabel = new JLabel(ingredient.supplier);
        JPanel ingredientsPanel = new JPanel(new GridLayout(ingredient.dishes.size(), 1));
        for (String dish : ingredient.dishes.keySet()) {
            JPanel tempPanel = new JPanel(new GridLayout(1, 2));
            JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel amountLabel = new JLabel(ingredient.dishes.get(dish).toString());
            JLabel nameLabel = new JLabel(dish + ": ");
            amountPanel.add(amountLabel);
            namePanel.add(nameLabel);
            tempPanel.add(namePanel);
            tempPanel.add(amountPanel);
            ingredientsPanel.add(tempPanel);
        }
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(ingredientName);
        topPanel.add(supplierLabel);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(ingredientsPanel, BorderLayout.CENTER);
    }
}
