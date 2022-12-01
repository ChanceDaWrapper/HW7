package hw7;

public class Stopwatch {
	private long elapsedTime;
	private long startTime;
	private boolean timing;

	//Constructor
	public Stopwatch() {
	}
	
	//Getting our elapsed time
	public long getElapsedTime() {
		if (timing) {
			long endTime = System.currentTimeMillis();
			return elapsedTime + endTime - startTime;
		} else {
			return elapsedTime;
		}
	}

	//Starting the timer
	public void start() {
		if (timing) {
			return;
		}
		timing = true;
		startTime = System.currentTimeMillis();
	}

	//Stopping the timer
	public void stop() {
		if (!timing) {
			return;
		}
		timing = false;
		long endTime = System.currentTimeMillis();
		elapsedTime = elapsedTime + endTime - startTime;
	}

	//Resetting the timer (not used but nice to have)
	public void reset() {
		elapsedTime = 0;
		timing = false;
	}

}