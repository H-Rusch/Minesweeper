import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer implements Runnable {

  private volatile boolean running = true;
  private GamePresenter presenter;

  public Timer(GamePresenter presenter) {
    this.presenter = presenter;
  }

  public void stopRunning() {
    running = false;
  }

  /** Wait 1 second before incrementing the timer. Stops after the the Timer is asked to stop. */
  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (running) {
        presenter.incrementTimer();
      } else {
        break;
      }
    }
  }
}
