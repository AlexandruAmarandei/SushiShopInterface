package Restaurant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class in charge or communicating with a user and only one user.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class CommsThread extends Thread {

    private Socket socket;
    private Comms server;
    public Integer userID;
    public String name = "";
    public DataInputStream in;
    public DataOutputStream out;

    public CommsThread(Comms server, Socket socket) {
        super();
        this.server = server;
        this.socket = socket;

        userID = socket.getPort();
    }

    //open the connection between client and server
    public void setIO() throws IOException {

        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public void run() {

        while (true) {
            try {
                Message msg = new Message();
                msg.senderIP = userID;
                msg.parseMessageIntoThis(in.readUTF());
                server.nextMessage(userID, msg);

            } catch (IOException e) {
                System.out.println("Error reading input from id:" + userID);
                server.remove(userID);
                stop();
            }
        }
    }

    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    public void sendMessege(String msg) {
        try {

            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Error in printing the msg");
            server.remove(userID);
        }
    }
}
