package semaphore;

import java.util.ArrayList;

import scheduler.Process;

public class Semaphore {
	private ArrayList<Process> waitingQueue;

	public Semaphore() {
		waitingQueue = new ArrayList<Process>();
	}

}
