package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.Node;

public class Process {
	private int id;
	private ArrayList<MyThread> myThreads;
	private MyThread mainThread;
	private ArrayList<HashMap<String, Object>> globalVars;
	private ArrayList<Process> waitingQueue;

	private void init() {
		myThreads = new ArrayList<MyThread>();
		globalVars = new ArrayList<HashMap<String, Object>>();
		waitingQueue = new ArrayList<Process>();
	}

	public Process(String code, int pc) {
		init();
		mainThread = new MyThread(code, pc, this);

	}

	public Process(String code) {
		init();
		mainThread = new MyThread(code, this);

	}

	public int getID() {
		return id;
	}

	public int getThreadsSize() {
		return myThreads.size();
	}

	public MyThread getMainThread() {
		return mainThread;
	}
}
