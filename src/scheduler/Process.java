package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.ParseTree;

public class Process {
	private static int idGenerator = 1;
	private int id;
	private int startedReadyOn;
	private int programCounter = 0;
	private HashMap<Integer, MyThread> allThreads;
	private HashMap<Integer, MyThread> runningThreads;
	private MyThread mainThread;
	private HashMap<String, Object> globalVars;
	private ArrayList<MyThread> waiters;
	private ArrayList<Process> children;
	private Process parent;
	private Scheduler scheduler;

	public static int getNewPid() {
		return idGenerator++;
	}

	private void init(int pid, Process parent, Scheduler scheduler) {
		this.id = pid;
		allThreads = new HashMap<Integer, MyThread>();
		runningThreads = new HashMap<Integer, MyThread>();
		globalVars = new HashMap<String, Object>();
		waiters = new ArrayList<MyThread>();
		children = new ArrayList<Process>();
		this.parent = parent;
		this.scheduler = scheduler;
	}

	public Process(int pid, ParseTree programTree, Process parent, Scheduler scheduler) {
		init(pid, parent, scheduler);
		int tid = MyThread.getNewTid();
		mainThread = new MyThread(tid, programTree, this);
		runThread(mainThread);

	}

	public Process(int pid, ParseTree programTree, Process parent, Scheduler scheduler, int parentTid) {
		init(pid, parent, scheduler);
		int tid = MyThread.getNewTid();
		int pc = programTree.getPc(parentTid);
		this.programCounter = pc;
		mainThread = new MyThread(tid, programTree, pc, this, parentTid);
		addThread(mainThread);
		runThread(mainThread);

	}

	public void addThread(MyThread t) {
		allThreads.put(t.getID(), t);
	}

	public void runThread(MyThread t) {
		runningThreads.put(t.getID(), t);
		t.setStatus("running");
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
		return allThreads.size();
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

	public Object getGlobalVar(String varname) {
		if (globalVars.containsKey(varname)) {
			return globalVars.get(varname);
		}
		return null;
	}

	public Process getParent() {
		return parent;
	}

	public ArrayList<MyThread> getWaiters() {
		return waiters;
	}

	public HashMap<Integer, MyThread> getAllThreads() {
		return allThreads;
	}

	public void removeThread(MyThread t) {
		allThreads.remove(t);
		runningThreads.remove(t);

	}

	public void stopThread(MyThread t) {
		runningThreads.remove(t);

	}

	public void addWaiter(MyThread t) {
		waiters.add(t);
	}
}
