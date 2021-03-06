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
	private MyThread alreadyRunningCandidate;
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

	public Process(int pid, ParseTree programTree, Process process, Scheduler scheduler) {
		init(pid, process, scheduler);
		int tid = MyThread.getNewTid();
		mainThread = new MyThread(tid, programTree, this);
		addThread(mainThread);
		runThread(mainThread);

	}

	public Process(int pid, ParseTree programTree, Process process, Scheduler scheduler, MyThread parent) {
		init(pid, process, scheduler);
		int tid = MyThread.getNewTid();
		int pc = programTree.getPc(parent.getID());
		this.programCounter = pc;
		mainThread = new MyThread(tid, programTree, pc, this, parent);
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
		if (runningThreads.size() == 0) {
			return null;
		}
		Object[] keys = runningThreads.keySet().toArray();
		MyThread candidate = runningThreads.get(keys[0]);
		for (Object key : keys) {
			if (runningThreads.get(key).relationalwaitTime() > candidate.relationalwaitTime()) {
				candidate = runningThreads.get(key);
			}
		}
		return candidate;
	}

	public void executeNextInstruction() {
		if (alreadyRunningCandidate != null) {
			if (alreadyRunningCandidate.timeUp()) {
				alreadyRunningCandidate.resetCurrentCPUBurstTime();
				alreadyRunningCandidate = null;
			} else {
				executeInstructionThread(alreadyRunningCandidate);
				return;
			}
		}
		MyThread t = extractThreadToRun();
		if (t != null) {
			increaseRelationalWaitTimesBut(t);
			executeInstructionThread(t);
		}
	}

	private void increaseRelationalWaitTimesBut(MyThread t) {
		Object[] keys = runningThreads.keySet().toArray();
		for (Object key : keys) {
			MyThread thread = runningThreads.get(key);
			if (thread == t)
				continue;
			thread.increaseRelationalWaitTime();
		}
	}

	private void executeInstructionThread(MyThread t) {
		increaseWaitTimesBut(t);
		alreadyRunningCandidate = t;
		t.executeNextInstruction();
		t.increateCurrentCPUBurstTime();
		programCounter++;
	}

	private void increaseWaitTimesBut(MyThread t) {
		Object[] keys = runningThreads.keySet().toArray();
		for (Object key : keys) {
			MyThread thread = runningThreads.get(key);
			if (thread == t)
				continue;
			thread.increaseWaitTime();
		}

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
		allThreads.remove(t.getID());
		runningThreads.remove(t.getID());

		if (isDone()) {
			scheduler.killProcess(this.getID());
		}

	}

	private boolean isDone() {
		return runningThreads.size() == 0;

	}

	public void stopThread(MyThread t) {
		runningThreads.remove(t.getID());

	}

	public void addWaiter(MyThread t) {
		waiters.add(t);
	}

	public void addGlobalVar(String name, Object value) {
		globalVars.put(name, value);
	}

	public HashMap<String, Object> getGlobalVars() {
		return globalVars;
	}

	public void increaseWaitTime() {
		Object[] keys = runningThreads.keySet().toArray();
		for (Object key : keys) {
			MyThread t = runningThreads.get(key);
			t.increaseWaitTime();
		}
	}

	public int getPriority() {
		int ret = 0;
		Object[] keys = runningThreads.keySet().toArray();
		for (Object key : keys) {
			MyThread t = runningThreads.get(key);
			ret += t.getPriority();
		}
		return ret;
	}

	public MyThread getAlreadyRunningCandidate() {
		return alreadyRunningCandidate;
	}

	public void setAlreadyRunningCandidate(MyThread alreadyRunningCandidate) {
		this.alreadyRunningCandidate = alreadyRunningCandidate;
	}
}
