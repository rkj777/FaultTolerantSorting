import java.util.Timer;
//The thread that runs the watch dog timer
public class WatchDogThread extends Thread {
	private Thread watched;
	private long time;
	private Timer timer;
    public WatchDogThread(Thread thread,long time){
      	this.watched = thread;
		this.time = time;
    }

    @Override
    public void run() {
        super.run();
        timer = new Timer();
        Watchdog watchdog = new Watchdog(watched);
        timer.schedule(watchdog, time);
    }

	public void stopTimer(){
		timer.cancel();	
	}
}
