package Client;

import javax.swing.JLabel;

/**
 * A label that can countdown.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class JTimedLabel extends JLabel implements Timed {

    private int seconds, period, delay;
    private MyTimer timer;
    private boolean stop = false;
    private NotifiedClass notifiedClass = null;

    public JTimedLabel() {
        super();
    }

    public JTimedLabel(String text) {
        super(text);
    }

    public JTimedLabel(int seconds) {
        super();
        this.seconds = seconds;
        this.period = 1000;
        this.delay = 1000;

        timer = new MyTimer(this, seconds, 10, 1000);
        String min = Integer.toString(seconds / 60);
        String sec = Integer.toString(seconds % 60);
        this.setText(min + ":" + sec);

    }

    public JTimedLabel(NotifiedClass notifiedClass, int seconds) {
        super();
        this.notifiedClass = notifiedClass;
        this.seconds = seconds;
        this.period = 1000;
        this.delay = 1000;
        timer = new MyTimer(this, seconds, 10, 1000);
        String min = Integer.toString(seconds / 60);
        String sec = Integer.toString(seconds % 60);
        this.setText(min + ":" + sec);

    }

    public JTimedLabel(String name, NotifiedClass notifiedClass, int seconds) {
        super();
        this.setName(name);
        this.notifiedClass = notifiedClass;
        this.seconds = seconds;
        this.period = 1000;
        this.delay = 1000;
        timer = new MyTimer(this, seconds, 10, 1000);
        String min = Integer.toString(seconds / 60);
        String sec = Integer.toString(seconds % 60);
        this.setText(min + ":" + sec);

    }

    public JTimedLabel(int seconds, int period, int delay) {
        super();
        this.seconds = seconds;
        this.period = period;
        this.delay = delay;
        timer = new MyTimer(this, seconds, period, delay);
        String min = Integer.toString(seconds / 60);
        String sec = Integer.toString(seconds % 60);
        this.setText(min + ":" + sec);
    }

    public JTimedLabel(NotifiedClass notifiedClass, int seconds, int period, int delay) {
        super();
        this.notifiedClass = notifiedClass;
        this.seconds = seconds;
        this.period = period;
        this.delay = delay;
        timer = new MyTimer(this, seconds, period, delay);
        String min = Integer.toString(seconds / 60);
        String sec = Integer.toString(seconds % 60);
        this.setText(min + ":" + sec);
    }

    public JTimedLabel(String name, NotifiedClass notifiedClass, int seconds, int period, int delay) {
        super();
        this.setName(name);
        this.notifiedClass = notifiedClass;
        this.seconds = seconds;
        this.period = period;
        this.delay = delay;
        timer = new MyTimer(this, seconds, period, delay);
        String min = Integer.toString(seconds / 60);
        String sec = Integer.toString(seconds % 60);
        this.setText(min + ":" + sec);
    }

    public void stopTimer() {
        stop = true;
        timer.stopTime();
    }

    public void setText(int seconds) {
        this.setText(Integer.toString(seconds));
    }

    public void resetTimer(int seconds) {
        this.seconds = seconds;
        this.period = 1000;
        this.delay = 1000;

        timer = new MyTimer(this, seconds, 10, 1000);
        String min = Integer.toString(seconds / 60);
        String sec = Integer.toString(seconds % 60);
        stop = false;
        this.setText(min + ":" + sec);
    }

    @Override
    public void tickPassed() {

        if (stop == false) {
            seconds--;
            String min = Integer.toString(seconds / 60);
            String sec = Integer.toString(seconds % 60);
            this.setText(min + ":" + sec);
        }
    }

    @Override
    public void timerFinished() {
        if (stop == false) {
            if (notifiedClass != null) {
                notifiedClass.notifyThis(this.getName());
            }
        }
    }

}
