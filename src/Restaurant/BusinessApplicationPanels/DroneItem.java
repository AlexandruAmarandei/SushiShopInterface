package Restaurant.BusinessApplicationPanels;

import Restaurant.Drone;
import Restaurant.ServerClient;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DroneItem extends JPanel implements Runnable {

    private Drone drone;
    private JLabel timeLabel;

    public DroneItem(Drone drone, ServerClient serverClient, DronePanel dronePanel) {
        this.drone = drone;
        Thread t = new Thread();

        JLabel droneIDLabel = new JLabel("ID: " + drone.droneID.toString());
        JLabel speedLabel = new JLabel("Speed: " + Double.toString(drone.speed));
        JLabel droneStateLabel = new JLabel("State: " + drone.getCurrentState());
        JLabel taskIDLabel = new JLabel("Task: " + drone.taskID.toString());
        Long time = drone.getTime();
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        timeLabel = new JLabel("Time: " + minutes.toString() + ":" + seconds.toString());
        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.add(droneIDLabel);
        infoPanel.add(speedLabel);
        infoPanel.add(droneStateLabel);
        infoPanel.add(taskIDLabel);
        infoPanel.add(timeLabel);
        JPanel locker = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locker.add(infoPanel);
        this.setLayout(new BorderLayout());
        this.add(locker, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        JButton changeInfoButton = new JButton("Change");
        JButton removeButton = new JButton("Remove");
        changeInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DroneRegister dr = new DroneRegister();
                int answer = JOptionPane.showConfirmDialog(null, dr);
                if (answer == JOptionPane.OK_OPTION) {
                    serverClient.changeDroneSpeed(drone.droneID, Double.parseDouble(dr.speed.getText()));
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverClient.removeDrone(drone.droneID);
                dronePanel.refreshPanel();
            }
        });
        JPanel changeInfoPanel = new JPanel(new FlowLayout());
        JPanel removeButtonPanel = new JPanel(new FlowLayout());
        changeInfoPanel.add(changeInfoButton);
        removeButtonPanel.add(removeButton);
        buttonPanel.add(changeInfoPanel);
        buttonPanel.add(removeButtonPanel);
        this.add(buttonPanel, BorderLayout.EAST);

        //t.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000);
                Long time = drone.sleepTime - (System.currentTimeMillis() - drone.startTime);
                Long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
                Long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
                timeLabel.setText("Time: " + minutes.toString() + ":" + seconds.toString());
            } catch (InterruptedException ex) {
                Logger.getLogger(DroneItem.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
