package scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Process {
	private static int idGenerator = 1;
	private int id;
	private int startedReadyOn;
	private int programCounter = 0;
	private HashMap<Integer, MyThread> runningThreads;
	private HashMap<Integer, MyThread> waitingThreads;
	private MyThread mainThread;
	private ArrayList<HashMap<String, Object>> globalVars;
	private ArrayList<Process> waitingQueue;
	private ArrayList<Process> children;
	private Process parent;

	public static int getNewPid() {
		return idGenerator++;
	}

	private void init(int pid, Process parent) {
		this.id = pid;
		runningThreads = new HashMap<Integer, MyThread>();
		waitingThreads = new HashMap<Integer, MyThread>();
		globalVars = new ArrayList<HashMap<String, Object>>();
		waitingQueue = new ArrayList<Process>();
		children = new ArrayList<Process>();
		this.parent = parent;
	}

	public Process(int pid, String code, int pc, Process parent) {
		init(pid, parent);
		int tid = MyThread.getNewTid();
		mainThread = new MyThread(tid, code, pc, this);

	}

	public Process(int pid, String code, Process parent) {
		init(pid, parent);
		int tid = MyThread.getNewTid();
		mainThread = new MyThread(tid, code, this);

	}

	public int getID() {
		return id;
	}

	public int getThreadsSize() {
		return runningThreads.size() + waitingThreads.size();
	}

	public MyThread getMainThread() {
		return mainThread;
	}

	public void setStartedReadyOn(int startedReadyOn) {
		this.startedReadyOn = startedReadyOn;
	}

	public int getStartedReadyOn() {
		return startedReadyOn;
	}

	public int getProgramCounter() {
		return programCounter;
	}

	public void executeNextInstruction() {
		MyThread t = extractThreadToRun();
		t.executeNextInstruction();
		programCounter++;

	}

	private MyThread extractThreadToRun() {
		Object[] keys = runningThreads.keySet().toArray();
		int index = programCounter % runningThreads.size();
		int tid = (Integer) keys[index];
		return runningThreads.get(tid);
	}

}
