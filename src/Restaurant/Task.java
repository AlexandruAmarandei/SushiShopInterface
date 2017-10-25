/*
 * Simple class that indentifies a task. Used in assigner.
 */
package Restaurant;

/**
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class Task {

    public Integer taskID;
    public double distance;

    public Task(Integer ID, double distance) {
        this.taskID = ID;
        this.distance = distance;
    }
}
