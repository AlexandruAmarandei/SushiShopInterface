package Client;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * This class contains the three main JFrames. The login frame, the register
 * frame and the menu frame.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class ClientClient {

    JFrame logInFrame, registerFrame, menuFrame;
    ClientApplication application;
    BasketPanel basket;
    OrdersPanel ordersPanel;
    JPanel menuPanel;

    public ClientClient(ClientApplication application) {

        basket = new BasketPanel(this);
        ordersPanel = new OrdersPanel();
        this.application = application;
    }

    public void notifyUserWrongCredentials() {
        JOptionPane.showMessageDialog(logInFrame, "Log in failed!\n Please check username and password!");

    }

    public void orderSuccesefull(String orderID) {
        basket.orderSuccesefull(orderID);

    }

    public void orderUnsuccesefull(String errorMsg) {
        JOptionPane.showMessageDialog(menuPanel, errorMsg);
    }

    public void addLoginFrame() {
        if (logInFrame != null) {
            logInFrame.setVisible(false);
        }
        if (registerFrame != null) {
            registerFrame.setVisible(false);
        }
        if (menuFrame != null) {
            menuFrame.setVisible(false);
        }
        logInFrame = new JFrame();

        Container content = logInFrame.getContentPane();
        logInFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        logInFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedureLogin();
            }
        });
        logInFrame.setName("Sushi");
        logInFrame.setTitle("ShushiLogIn");
        logInFrame.setLayout(new BorderLayout());
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JTextField username = new JTextField(20);
        JTextField password = new JPasswordField(20);
        JButton logIn = new JButton("Log In");
        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameText = username.getText();
                char[] passwordText = password.getText().toCharArray();
                password.setText("");
                if (usernameText != null && passwordText != null) {
                    String[] logInData = new String[2];
                    logInData[0] = usernameText;
                    logInData[1] = Arrays.toString(passwordText);
                    application.sendMessage(logInData, 0);
                }
            }
        });
        JButton register = new JButton("Register");
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application.sendMessage(null, 3);
                //addRegisterFrame();

            }
        });

        JPanel textFields = new JPanel();
        JPanel usernamePanel = new JPanel(new FlowLayout());
        JPanel passwordPanel = new JPanel(new FlowLayout());
        textFields.setLayout(new GridLayout(2, 1));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(username);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(password);
        textFields.add(usernamePanel);
        textFields.add(passwordPanel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.add(logIn);
        buttons.add(register);

        content.add(textFields, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        logInFrame.setVisible(true);
        logInFrame.pack();

    }

    public void notifyUserUsernameAlreadyInUse() {
        JOptionPane.showMessageDialog(registerFrame, "Username already in use!");
    }

    public void notifyUserRegistrationSuccesseful() {
        JOptionPane.showMessageDialog(logInFrame, "Please log in with the new account!");
    }

    public void addRegisterFrame(String postcodes) {
        String[] postcodeArray = postcodes.split("\\$");

        addRegisterFrame(postcodeArray);
    }

    public void addRegisterFrame(String[] postcodes) {
        if (logInFrame != null) {
            logInFrame.setVisible(false);
        }
        if (registerFrame != null) {
            registerFrame.setVisible(false);
        }
        if (menuFrame != null) {
            menuFrame.setVisible(false);
        }
        if (registerFrame == null) {
            registerFrame = new JFrame();
        }

        Container content = registerFrame.getContentPane();
        registerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        registerFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedureRegister();
            }
        });
        registerFrame.setName("SushiRegister");
        registerFrame.setTitle("ShushiRegister");
        registerFrame.setLayout(new BorderLayout());
        JLabel nameLabel = new JLabel("Full Name");
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JLabel confirmLabel = new JLabel("Confirm ");
        JLabel postcodeLabel = new JLabel("Postcode");
        JLabel addressLabel = new JLabel("Address ");
        JLabel emailLabel = new JLabel("Email   ");
        JCheckBox termsAndCond = new JCheckBox("I have read and agreed to the Terms and Conditions.");

        JTextField name = new JTextField(20);
        JTextField username = new JTextField(20);
        JTextField password = new JPasswordField(20);
        JTextField confirm = new JPasswordField(20);
        JComboBox<String> postcodesBox = new JComboBox<>();
        for (String postcode : postcodes) {
            postcodesBox.addItem(postcode);
        }

        JTextField address = new JTextField(20);

        JTextField email = new JTextField(20);
        JButton register = new JButton("Register");
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameText = name.getText();
                String usernameText = username.getText();
                char[] passwordText = password.getText().toCharArray();
                char[] confirmText = confirm.getText().toCharArray();
                String addressText = address.getText();
                String emailText = email.getText();
                String postcode = String.valueOf(postcodesBox.getSelectedItem());
                boolean correct = false;
                for (String pc : postcodes) {
                    if (postcode.equals(pc)) {
                        correct = true;
                    }
                }
                if (nameText.contains("$")) {
                    JOptionPane.showMessageDialog(registerFrame, "$ not allowed in the name!");
                    correct = false;
                }
                if (usernameText.contains("$")) {
                    JOptionPane.showMessageDialog(registerFrame, "$ not allowed in the username!");
                    correct = false;
                }
                if (addressText.contains("$")) {
                    JOptionPane.showMessageDialog(registerFrame, "$ not allowed in the adress!");
                    correct = false;
                }
                if (emailText.contains("$")) {
                    JOptionPane.showMessageDialog(registerFrame, "$ not allowed in the email!");
                    correct = false;
                }
                if (!termsAndCond.isSelected()) {
                    correct = false;
                    JOptionPane.showMessageDialog(registerFrame,
                            "You have to agree with the terms and conditions!");
                }

                if (passwordText.length == confirmText.length) {
                    for (int i = 0; i < passwordText.length && correct == true; i++) {
                        if (passwordText[i] != confirmText[i]) {
                            JOptionPane.showMessageDialog(registerFrame, "Password fields incorect!");
                            correct = false;
                        }
                    }
                    if (correct && !nameText.equals("") && !usernameText.equals("")
                            && !passwordText.equals("") && !confirmText.equals("")
                            && !addressText.equals("") && !emailText.equals("") && !postcode.equals("")) {
                        String[] registerData = new String[6];
                        registerData[0] = usernameText;
                        registerData[1] = Arrays.toString(passwordText);
                        registerData[2] = nameText;
                        registerData[3] = addressText;
                        registerData[4] = emailText;
                        registerData[5] = postcode;

                        application.sendMessage(registerData, 2);

                    } else {
                        JOptionPane.showMessageDialog(registerFrame, "Please complete all the fields!");
                    }
                } else {
                    JOptionPane.showMessageDialog(registerFrame, "Password fields incorect!");
                }

            }
        });

        JPanel textFields = new JPanel();
        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel usernamePanel = new JPanel(new FlowLayout());
        JPanel passwordPanel = new JPanel(new FlowLayout());
        JPanel confirmPanel = new JPanel(new FlowLayout());
        JPanel addressPanel = new JPanel(new FlowLayout());
        JPanel postcodePanel = new JPanel(new FlowLayout());
        JPanel emailPanel = new JPanel(new FlowLayout());

        textFields.setLayout(new GridLayout(8, 1));
        namePanel.add(nameLabel);
        namePanel.add(name);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(username);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(password);
        confirmPanel.add(confirmLabel);
        confirmPanel.add(confirm);
        addressPanel.add(addressLabel);
        addressPanel.add(address);
        postcodePanel.add(postcodeLabel);
        postcodePanel.add(postcodesBox);
        emailPanel.add(emailLabel);
        emailPanel.add(email);

        textFields.add(namePanel);
        textFields.add(usernamePanel);
        textFields.add(passwordPanel);
        textFields.add(confirmPanel);
        textFields.add(addressPanel);
        textFields.add(postcodePanel);
        textFields.add(emailPanel);
        textFields.add(termsAndCond);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.add(register);

        content.add(textFields, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        registerFrame.setVisible(true);
        registerFrame.pack();
    }

    private JScrollPane menuScroll;
    private JPanel locker;

    public void addMenuFrame(String menuItems, String username, String name) {
        if (logInFrame != null) {
            logInFrame.setVisible(false);
        }
        if (registerFrame != null) {
            registerFrame.setVisible(false);
        }
        if (menuFrame != null) {
            menuFrame.setVisible(false);
        }
        menuFrame = new JFrame();
        Container content = menuFrame.getContentPane();
        menuFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        menuFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedureMenu();
            }
        });
        menuFrame.setLayout(new BorderLayout());
        menuFrame.setTitle("Sushi Menu");
        menuFrame.setName("Sushi menu");

        String[] items = menuItems.split("&");
        menuPanel = new JPanel();
        JMenuBar frameMenuBar = new JMenuBar();

        JLabel usernameLabel = new JLabel(" (" + username + ") ");
        JLabel nameLabel = new JLabel(name);
        JButton logoutButton = new JButton("Log out");
        logoutButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                application.sendMessage(null, 6);

            }
        });
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                application.sendMessage(null, 5);
            }
        });
        refreshButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        locker = new JPanel(new FlowLayout(FlowLayout.LEFT));
        locker.setLayout(new BoxLayout(locker, BoxLayout.PAGE_AXIS));
        menuPanel.setLayout(new GridLayout(items.length, 1));

        frameMenuBar.add(nameLabel);
        frameMenuBar.add(usernameLabel);
        frameMenuBar.add(Box.createHorizontalGlue());
        frameMenuBar.add(refreshButton);
        frameMenuBar.add(logoutButton);

        for (String item : items) {
            System.out.println(item);
            menuPanel.add(new MyMenuItem(basket, item));

        }
        menuPanel.setMaximumSize(new Dimension(menuPanel.getMaximumSize().width, menuPanel.getPreferredSize().height));

        locker.add(menuPanel);
        menuScroll = new JScrollPane(locker, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        menuFrame.setJMenuBar(frameMenuBar);
        content.add(ordersPanel, BorderLayout.WEST);
        content.add(basket, BorderLayout.EAST);
        content.add(menuScroll, BorderLayout.CENTER);

        menuFrame.setVisible(true);
        menuFrame.pack();

    }

    public void clearMenu() {
        menuPanel.removeAll();
        locker.removeAll();
        menuFrame.remove(menuScroll);
    }

    public void refreshValues(String menuItems) {
        clearMenu();
        menuPanel = new JPanel();
        String[] items = menuItems.split("&");
        menuPanel.setLayout(new GridLayout(items.length, 1));
        for (String item : items) {
            System.out.println(item);
            menuPanel.add(new MyMenuItem(basket, item));

        }
        menuPanel.setMaximumSize(new Dimension(menuPanel.getMaximumSize().width, menuPanel.getPreferredSize().height));

        menuPanel.updateUI();
        //JPanel locker = new JPanel(new FlowLayout());
        locker.add(menuPanel);
        menuScroll = new JScrollPane(locker, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Container content = menuFrame.getContentPane();
        content.add(menuScroll, BorderLayout.CENTER);
        menuFrame.setVisible(true);

    }

    public void exitProcedureMenu() {
        application.sendMessage(null, 61);
        stop();

    }

    public void exitProcedureLogin() {
        application.stop();
        stop();

    }

    public void exitProcedureRegister() {
        application.stop();
        stop();

    }

    private void stop() {
        if (registerFrame != null) {
            registerFrame.dispose();
        }
        if (logInFrame != null) {
            logInFrame.dispose();
        }
        if (menuFrame != null) {
            menuFrame.dispose();
        }
        System.exit(0);
    }
}
