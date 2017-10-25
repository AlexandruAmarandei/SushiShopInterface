package Restaurant;

public interface Communicator {

    public void sendMessage(int ID, String message);

    public void nextMessage(int ID, Message message);
}
