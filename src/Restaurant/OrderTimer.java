package Restaurant;

/**
 * A class used to store the start time of a task and the expected completion
 * time.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class OrderTimer {

    public Long startTime;
    public Long expectedDeliveryTime;

    public OrderTimer(Long startTime, Long expectedDeliveryTime) {
        this.startTime = startTime;
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public Long getTime() {
        if (startTime == null || expectedDeliveryTime == null) {
            return 0l;
        }

        return expectedDeliveryTime - (System.currentTimeMillis() - startTime);
    }
}
