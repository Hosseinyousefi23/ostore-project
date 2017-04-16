package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import executer.Node;

public class Process {
	private int id;
	private int programCounter = 0;
	private ArrayList<MyThread> myThreads;
	private MyThread mainThread;
	private ArrayList<HashMap<String, Object>> globalVars;

	public Process() {
		globalVars = new ArrayList<HashMap<String, Object>>();
	}

	public Process(int programCounter, Node bigRoot) {
		myThreads = new ArrayList<MyThread>();
		globalVars = new ArrayList<HashMap<String, Object>>();
		mainThread = new MyThread(bigRoot, this);
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
