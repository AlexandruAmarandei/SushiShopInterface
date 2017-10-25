package Restaurant.BusinessApplicationPanels;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DroneRegister extends JPanel {

    JTextField speed;

    public DroneRegister() {
        speed = new JTextField(15);
        JLabel speedLabel = new JLabel("Speed");
        this.setLayout(new FlowLayout());
        this.add(speedLabel);
        this.add(speed);
    }
}
