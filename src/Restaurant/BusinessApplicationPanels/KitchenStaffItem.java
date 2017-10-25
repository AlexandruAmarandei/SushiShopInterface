/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant.BusinessApplicationPanels;

import Restaurant.KitchenStaff;
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

/**
 *
 * @author alexa
 */
public class KitchenStaffItem extends JPanel {

    KitchenStaff staff;
    JLabel timeLabel;

    KitchenStaffItem(KitchenStaff staff, ServerClient serverClient, KitchenStaffPanel kitchenStaffPanel) {
        this.staff = staff;

        JLabel staffNameLabel = new JLabel("Name: " + staff.name);
        JLabel staffIDLabe = new JLabel("ID: " + staff.ID);
        JLabel staffStateLabel = new JLabel("Working: " + staff.isWorking());
        JLabel dishLabel = new JLabel("Dish:" + staff.dishName);
        Long time = staff.getCurrentTime();
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        timeLabel = new JLabel("Time: " + minutes.toString() + ":" + seconds.toString());
        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.add(staffNameLabel);
        infoPanel.add(staffIDLabe);
        infoPanel.add(staffStateLabel);
        infoPanel.add(dishLabel);
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
                KitchenStaffRegister ksr = new KitchenStaffRegister();
                int answer = JOptionPane.showConfirmDialog(null, ksr);
                if (answer == JOptionPane.OK_OPTION) {
                    serverClient.changeStaffName(Integer.parseInt(staff.ID), ksr.name.getText());
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverClient.removeKitchenStaff(staff.name);
                kitchenStaffPanel.refreshPanel();
            }
        });
        JPanel changeInfoPanel = new JPanel(new FlowLayout());
        JPanel removeButtonPanel = new JPanel(new FlowLayout());
        changeInfoPanel.add(changeInfoButton);
        removeButtonPanel.add(removeButton);
        buttonPanel.add(changeInfoPanel);
        buttonPanel.add(removeButtonPanel);
        this.add(buttonPanel, BorderLayout.EAST);

    }

}
