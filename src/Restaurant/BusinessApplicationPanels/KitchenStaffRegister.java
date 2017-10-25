package Restaurant.BusinessApplicationPanels;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KitchenStaffRegister extends JPanel {

    JTextField name;

    public KitchenStaffRegister() {
        name = new JTextField(15);
        JLabel speedLabel = new JLabel("Name");
        this.setLayout(new FlowLayout());
        this.add(speedLabel);
        this.add(name);
    }
}
