package Client;

import java.awt.List;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * This is the main start of the client application. It sends to the server the
 * messages from it's components. Here, the input from the server is also
 * interpreted. I tried to make a queue of messages, but on my computer it just
 * doesn't sync up. The code is commented.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class ClientApplication implements Runnable {

    private Socket socket;
    private final String UREGISTEREDUSERIDENTIFIER = "UNREGISTEREDUSER";
    private DataInputStream in;
    private DataOutputStream out;
    private Thread clientThread = null;
    private BufferedReader br;
    private String username, name;
    private ClientThread client;
    private ClientClient clientClient;
    private JPanel contentPane;
    private boolean verification = false;
    private int lastCommand;
    private String lastCommandText;
    private boolean lastCommandResolved = true;
    private List list = new List();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private LocalDateTime now;
    private LinkedBlockingQueue<String> commandsToSend;

    public ClientApplication(String name, int port) {

        clientClient = new ClientClient(this);
        username = "UNREGISTEREDUSER";
        this.name = "NoName";
        lastCommand = -1;
        commandsToSend = new LinkedBlockingQueue<>();
        clientClient.addLoginFrame();

        br = new BufferedReader(new InputStreamReader(System.in));
        try {
            socket = new Socket(name, port);
            System.out.println("Succesufully connected to:" + socket);
            start();
        } catch (IOException exception) {
            System.out.println("Error in trying to reach a port");
        }
    }

    private String constructMessage(String[] message, int command) {
        String result = Integer.toString(command);
        result = result + LocalDateTime.now();
        result = result + username;
        if (message != null) {
            for (int i = 0; i < message.length; i++) {
                result = result + "$" + message[i];
            }
        }
        return result;
    }

    public void sendMessage(String[] message, int command) {
        lastCommand = command;
        String msgToSend = constructMessage(message, command);

        if (command == 6) {
            clientClient.addLoginFrame();
            username = "UNREGISTEREDUSER";
        }
        if (command == 61) {
            stop();

        }
        try {
            //commandsToSend.add(msgToSend);
            out.writeUTF(msgToSend);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        /*
        while (clientThread != null) {

            if (lastCommandResolved) {
                try {
                lastCommandResolved = false;
                System.out.println("Waiting command...");
                String msgToSend = commandsToSend.take();
                lastCommand = msgToSend.charAt(0)-48;
                lastCommandText = msgToSend;
                out.writeUTF(msgToSend);
                out.flush();
                } catch (IOException ex) {
                    lastCommandResolved = true;
                    System.out.println("Error sending message!");
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }*/
    }

    public void resendCommand() {
        lastCommandResolved = true;
    }

    public void interpret(String message) {

        System.out.println(lastCommand + " " + message);
        if (lastCommand == message.charAt(0) - 48) {
            lastCommandResolved = true;
            //commandsToSend.pop();
        } else {
            resendCommand();
        }
        if (lastCommand == 0 && message.charAt(0) == '0') {

            if (message.charAt(1) == '0') {
                clientClient.notifyUserWrongCredentials();
            }

            if (message.charAt(1) == '1') {
                username = message.substring(2, message.indexOf('$'));
                message = message.substring(message.indexOf('$') + 1);
                name = message.substring(0, message.indexOf('$'));
                String menu = message.substring(message.indexOf('$') + 1);

                clientClient.addMenuFrame(menu, username, name);
            }
        }

        if (lastCommand == 3 && message.charAt(0) == '3') {

            clientClient.addRegisterFrame(message.substring(2));
        }

        if (lastCommand == 2 && message.charAt(0) == '2') {
            if (message.charAt(1) == '2') {
                clientClient.notifyUserUsernameAlreadyInUse();
            }
            if (message.charAt(1) == '8') {
                clientClient.addLoginFrame();
                clientClient.notifyUserRegistrationSuccesseful();

            }
        }

        if (lastCommand == 1 && message.charAt(0) == '1') {
            if (message.charAt(1) == '5') {
                clientClient.orderSuccesefull(message.substring(2));
            }
            if (message.charAt(1) == '4') {
                String errorMsg = message.substring(2, message.indexOf("&")).replace("$", "\n");
                String newMenu = message.substring(message.indexOf("&") + 1);
                clientClient.refreshValues(newMenu);
                clientClient.orderUnsuccesefull(errorMsg);
            }
        }
        if (lastCommand == 5 && message.charAt(0) == '5') {
            if (message.charAt(1) == '1') {
                String newMenu = message.substring(2);
                clientClient.refreshValues(newMenu);
            }
        }

        if (message.charAt(0) == '7' && message.charAt(1) == '7') {
            String orderID = message.substring(2, message.indexOf('$'));
            clientClient.ordersPanel.updateOrder(orderID, message.substring(message.indexOf('$') + 1));
        }

        if (message.equals("/Exit")) {
            System.out.println("You have been kicked out!");
            stop();
        }
    }

    public void start() throws IOException {
        in = new DataInputStream(System.in);
        out = new DataOutputStream(socket.getOutputStream());

        if (clientThread == null) {
            client = new ClientThread(this, socket);
            clientThread = new Thread(this);
            clientThread.start();
        }
    }

    public static void main(String args[]) {
        ClientApplication chatUser = new ClientApplication("localhost", 1500);
    }

    public void stop() {
        if (clientThread != null) {
            clientThread.stop();
            clientThread = null;
        }
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Problem in closing IO");
        }
        client.terminate();
        client.stop();
    }

    protected String getUsername() {
        return username;
    }
}
