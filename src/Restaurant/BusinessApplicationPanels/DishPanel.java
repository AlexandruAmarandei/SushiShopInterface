package Restaurant.BusinessApplicationPanels;

import Restaurant.Dish;
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

public class DishPanel extends JPanel {

    private ServerClient serverClient;
    private JPanel itemPanel;

    public DishPanel(ServerClient serverClient) {
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

    public void addNewDish() {
        String[] ingredientList = serverClient.getIngredientNames();
        DishRegister dr = new DishRegister(ingredientList);
        int answer = JOptionPane.showConfirmDialog(null, dr,
                "Please input Dish details", JOptionPane.OK_CANCEL_OPTION);
        if (answer == JOptionPane.OK_OPTION) {
            try {
                String name = dr.name.getText();
                double weight = Double.parseDouble(dr.weight.getText());
                String currency = dr.currency.getText();
                double price = Double.parseDouble(dr.price.getText());
                int stock = Integer.parseInt(dr.initialStock.getText());
                int restock = Integer.parseInt(dr.restockLvl.getText());
                int minTime = Integer.parseInt(dr.minTime.getText());
                int maxTime = Integer.parseInt(dr.maxTime.getText());

                serverClient.addDish(name, weight, price, currency, stock, restock, minTime, maxTime);
                refreshPanel();
            } catch (NumberFormatException e) {
                System.out.println("Error, character in number field");
            }

        }
    }

    public void refreshPanel() {
        itemPanel.removeAll();
        addItems();
        //this.add(itemPanel, BorderLayout.CENTER);
        this.updateUI();
    }

    public void addTopButtons() {
        JButton addSupplierButton = new JButton("Add");
        addSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewDish();
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
        //this.add(refreshPanel, BorderLayout.NORTH);
    }

    public void addItems() {

        Dish[] dishes = serverClient.getDishList();
        //Dish[] dishes = new Supplier[3];
        //dishes[0] = new Dish("AAA",3.2d);
        //dishes[1] = new Dish("BBB", 3333d);
        //dishes[2] = new Dish("CCCC", 33333d);
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
        if (dishes != null) {
            JPanel dishPanel = new JPanel(new GridLayout(dishes.length, 1));
            for (Dish dish : dishes) {
                dishPanel.add(new DishItem(dish, serverClient));
            }
            dishPanel.setMaximumSize(new Dimension(dishPanel.getMaximumSize().width, dishPanel.getPreferredSize().height));

            itemPanel.add(dishPanel);
        }

    }
}
