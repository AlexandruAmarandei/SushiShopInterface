package Restaurant;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * This class is used to get the messages from the clients and send a specific
 * client a message. All the messages that it receives are passed on.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class Comms implements Runnable, Communicator {

    Communicator commnicator;
    private HashMap<Integer, CommsThread> users;
    private ServerSocket server = null;
    private Thread serverThread = null;

    public Comms(Communicator communicator, int port) {
        this.commnicator = communicator;
        users = new HashMap<>();
        try {
            server = new ServerSocket(port);
            System.out.println("Connection establieshed on port:" + port);
            start();
        } catch (IOException e) {
            System.out.println("Error in establishing a connection");
        }
    }

    public Comms(int port) {
        try {
            users = new HashMap<>();
            server = new ServerSocket(port);
            System.out.println("Connection establieshed on port:" + port);
        } catch (IOException e) {
            System.out.println("Error in establishing a connection");
        }
    }

    public void setClassesAndStart(Communicator communicator) {
        this.commnicator = communicator;
        start();
    }

    public void start() {
        serverThread = new Thread(this);
        serverThread.start();
        //serverClient = new CommsClient(this);
        // serverClient.start();

    }

    @Override
    public void run() {
        while (serverThread != null) {
            try {
                addUser(server.accept());
                System.out.println("User connected!");
            } catch (IOException e) {
                System.out.println("Problem with connection");
                serverThread = null;
            }
        }
    }

    public void addUser(Socket userToAdd) {
        CommsThread ct = new CommsThread(this, userToAdd);
        users.put(userToAdd.getPort(), ct);

        try {

            ct.setIO();
            ct.start();
        } catch (IOException e) {
            System.out.println();
        }
    }

    @Override
    public synchronized void nextMessage(int ID, Message message) {
        commnicator.nextMessage(ID, message);
    }

    @Override
    public synchronized void sendMessage(int id, String message) {
        users.get(id).sendMessege(message);
    }

    public synchronized void remove(int ID) {

        if (users.containsKey(ID)) {
            try {
                users.get(ID).close();
            } catch (IOException ex) {
                System.out.println("Error in closing a thread");
                users.get(ID).stop();
            }
            users.remove(ID);
        }
    }

}
