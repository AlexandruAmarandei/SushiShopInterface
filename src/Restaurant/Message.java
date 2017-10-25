package Restaurant;

import java.util.Iterator;

/**
 * The components of a message. It also contains an iterator that splits the
 * content by a given regex.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class Message {

    public final static String DEFAULT_IDENTIFIER = "\\$";

    public String command = "";
    public String content = "";
    public Integer senderIP;
    public String username = "";
    public String date = "";

    @Override
    public String toString() {

        return command + date + username + "$" + content;
    }

    public static Message parseMessage(String message) {
        Message returnMessage = new Message();
        returnMessage.command = message.substring(0, 1);
        returnMessage.date = message.substring(1, 24);
        if (message.contains("$")) {
            returnMessage.username = message.substring(24, message.indexOf('$'));
            returnMessage.content = message.substring(message.indexOf('$'));
        } else {
            returnMessage.username = message.substring(24);
        }
        return returnMessage;
    }

    public static Message parseMessage(String message, Message destinationMessage) {

        destinationMessage.command = message.substring(0, 1);
        destinationMessage.date = message.substring(1, 24);
        if (message.contains("$")) {
            destinationMessage.username = message.substring(24, message.indexOf('$'));
            destinationMessage.content = message.substring(message.indexOf('$') + 1);
        } else {
            destinationMessage.username = message.substring(24);
        }
        return destinationMessage;
    }

    public void parseMessageIntoThis(String message) {

        command = message.substring(0, 1);
        date = message.substring(1, 24);
        if (message.contains("$")) {
            username = message.substring(24, message.indexOf('$'));
            content = message.substring(message.indexOf('$') + 1);
        } else {
            username = message.substring(24);
        }

    }

    private String[] contentMatrix;

    public Iterator<String> iterator(String identifier) {

        contentMatrix = content.split(identifier);

        Iterator<String> it = new Iterator<String>() {

            int currentPosition = 0;

            @Override
            public boolean hasNext() {
                return currentPosition < contentMatrix.length && contentMatrix[currentPosition] != null;
            }

            @Override
            public String next() {
                return contentMatrix[currentPosition++];
            }

            @Override
            public void remove() {
                String[] copy = new String[contentMatrix.length - 1];
                for (int i = 0; i < currentPosition; i++) {
                    copy[i] = contentMatrix[i];
                }
                for (int i = currentPosition + 1; i < contentMatrix.length; i++) {
                    copy[i - 1] = contentMatrix[i];
                }
                contentMatrix = copy;

            }

        };
        return it;
    }

}
