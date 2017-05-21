package semaphore;

import java.util.ArrayList;

import scheduler.MyThread;

public class Semaphore {
	private ArrayList<MyThread> waiters;
	private int value;

	public Semaphore(int value) {
		waiters = new ArrayList<MyThread>();
		this.value = value;
	}

	public void Wait(MyThread t) {
		if (value > 0) {
			value--;
		} else {
			t.getProcess().stopThread(t);
			waiters.add(t);
			t.setStatus("waiting");
		}
	}

	public void signal() {
		if (noWaiter()) {
			value++;
		} else {
			MyThread t = waiters.remove(0);
			t.getProcess().runThread(t);
		}
	}

	private boolean noWaiter() {
		return waiters.size() == 0;
	}

	public ArrayList<MyThread> getWaiters() {
		return waiters;
	}

}
