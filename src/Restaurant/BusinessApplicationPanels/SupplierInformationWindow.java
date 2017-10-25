package Restaurant.BusinessApplicationPanels;

import Restaurant.Supplier;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SupplierInformationWindow extends JPanel {

    public SupplierInformationWindow(Supplier supplier) {
        this.setLayout(new BorderLayout());
        JLabel supplierName = new JLabel(supplier.name);
        JLabel distanceLabel = new JLabel(supplier.distance.toString());
        JPanel ingredientsPanel = new JPanel(new GridLayout(supplier.ingredients.size(), 1));
        for (String ingredient : supplier.ingredients.keySet()) {
            JPanel tempPanel = new JPanel(new GridLayout(1, 2));
            JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel priceLabel = new JLabel(supplier.ingredients.get(ingredient).toString());
            JLabel nameLabel = new JLabel(ingredient);
            pricePanel.add(priceLabel);
            namePanel.add(nameLabel);
            tempPanel.add(namePanel);
            tempPanel.add(pricePanel);
            ingredientsPanel.add(tempPanel);
        }
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(supplierName);
        topPanel.add(distanceLabel);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(ingredientsPanel, BorderLayout.CENTER);
    }
}
