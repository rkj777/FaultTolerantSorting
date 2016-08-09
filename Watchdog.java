import java.util.TimerTask;

/**
 * Created by rajanjassal on 16-02-25.
 */
public class Watchdog extends TimerTask {
    Thread watched;
    public Watchdog(Thread target){
        watched = target;
    }

    public void run() {

        watched.stop();
        System.out.println("Timer has killed the thread");
    }


}
