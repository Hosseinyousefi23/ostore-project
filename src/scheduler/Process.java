package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.ParseTree;

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
	private Scheduler scheduler;

	public static int getNewPid() {
		return idGenerator++;
	}

	private void init(int pid, Process parent, Scheduler scheduler) {
		this.id = pid;
		runningThreads = new HashMap<Integer, MyThread>();
		waitingThreads = new HashMap<Integer, MyThread>();
		globalVars = new ArrayList<HashMap<String, Object>>();
		waitingQueue = new ArrayList<Process>();
		children = new ArrayList<Process>();
		this.parent = parent;
		this.scheduler = scheduler;
	}

	public Process(int pid, ParseTree programTree, Process parent,
			Scheduler scheduler) {
		init(pid, parent, scheduler);
		int tid = MyThread.getNewTid();
		mainThread = new MyThread(tid, programTree, this);
		runThread(mainThread);

	}

	public Process(int pid, ParseTree programTree, Process parent,
			Scheduler scheduler, int parentTid) {
		init(pid, parent, scheduler);
		int tid = MyThread.getNewTid();
		int pc = programTree.getPc(parentTid);
		this.programCounter = pc;
		mainThread = new MyThread(tid, programTree, pc, this, parentTid);
		runThread(mainThread);

	}

	public void runThread(MyThread t) {
		runningThreads.put(t.getID(), t);
	}

	private MyThread extractThreadToRun() {
		Object[] keys = runningThreads.keySet().toArray();
		int index = programCounter % runningThreads.size();
		int tid = (Integer) keys[index];
		return runningThreads.get(tid);
	}

	public void executeNextInstruction() {
		MyThread t = extractThreadToRun();
		t.executeNextInstruction();
		programCounter++;

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

	public void finish(MyThread t) {
		runningThreads.remove(t);
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void addChildProcess(Process child) {
		children.add(child);
	}

}
