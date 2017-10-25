package Restaurant.BusinessApplicationPanels;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SupplierRegister extends JPanel {

    public JTextField namePane;
    public JTextField distancePane;

    public SupplierRegister() {
        JLabel nameLabel = new JLabel("Name:");
        JLabel distanceLabel = new JLabel("Distance:");
        this.setLayout(new GridLayout(2, 1));
        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel distancePanel = new JPanel(new FlowLayout());
        namePanel.add(nameLabel);
        distancePanel.add(distanceLabel);

        namePane = new JTextField(15);
        distancePane = new JTextField(15);
        namePanel.add(namePane);
        distancePanel.add(distancePane);

        this.add(namePanel);
        this.add(distancePanel);
    }
}
