package Client;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used in for the JTimedLabel.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class MyTimer {

    private int seconds, delay, period;
    private Timed timedObject;
    private Timer timer;

    public MyTimer(Timed timedObject, int seconds) {
        this(timedObject, seconds, 1000, 1000);
    }

    public MyTimer(Timed timedObject, int seconds, int delay, int period) {
        this.seconds = seconds;
        this.delay = delay;
        this.period = period;
        this.timedObject = timedObject;
        timer = new Timer();
        startTimer();
    }

    public final void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                timedObject.tickPassed();
                updateTime();
            }
        }, delay, period);

    }

    private void updateTime() {
        seconds--;
        if (seconds == -1) {
            timedObject.timerFinished();
            timer.cancel();
        }

    }

    public void stopTime() {
        timer.cancel();
    }
}
