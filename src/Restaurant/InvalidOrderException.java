package Restaurant;

/**
 * A simple exception
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class InvalidOrderException extends Exception {

    String errorMessage;

    public InvalidOrderException() {
        this("");
    }

    public InvalidOrderException(String message) {
        errorMessage = message;
    }
}
