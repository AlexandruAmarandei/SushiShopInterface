package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This represents the class the communicates with the server.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class ClientThread extends Thread {

    private ClientApplication user;
    private Socket socket;
    private DataInputStream in;

    public ClientThread(ClientApplication user, Socket socket) {
        this.user = user;
        this.socket = socket;

        try {
            in = new DataInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Problem in reading input");
        }

        start();
    }

    public void run() {

        while (true) {
            try {

                user.interpret(in.readUTF());
            } catch (IOException e) {
                System.out.println("Error in reading the output");
                user.stop();
            }
        }
    }

    public void terminate() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            System.out.println("Error in closing the input");
        }
    }
}
