package scheduler;

import java.util.HashMap;
import java.util.Iterator;

import parser.ParseTree;
import parser.Parser;
import executer.Executer;
import semaphore.Semaphore;

public class Scheduler {
	public int clock = 0;
	private int cores = 4;
	private int schedulTime = 100;
	private Process defaultProcess;
	private HashMap<Integer, Process> readyQueue;
	private HashMap<Integer, Process> runningQueue;
	private HashMap<String, Semaphore> semaphores;
	private Executer exe;

	public Scheduler(int cores, int schedulTime) {
		readyQueue = new HashMap<Integer, Process>();
		runningQueue = new HashMap<Integer, Process>();
		setCores(cores);
		setSchedulTime(schedulTime);
		exe = new Executer();
		semaphores = new HashMap<String, Semaphore>();
		// defaultProcess = new Process();

	}

	public void start(String code) {
		int pid = Process.getNewPid();
		Parser parser = new Parser();
		ParseTree programTree = parser.parse(code);
		Process init = new Process(pid, programTree, null, this);
		readyQueue.put(pid, init);
		int runningCounter = 0;
		while (!isDone()) {
			if (runningCounter++ % 4 == 0) {
				shortTermSchedule();
			}
			executeRunningQueue();
		}
	}

	private void executeRunningQueue() {
		Iterator<Integer> keys = runningQueue.keySet().iterator();
		while (keys.hasNext()) {
			Process p = runningQueue.get(keys.next());
			p.executeNextInstruction();
			clock++;
			if (timeToCollect(p)) {
				collect(p);
			}
		}

	}

	private void collect(Process p) {
		runningQueue.remove(p);
		readyQueue.put(p.getID(), p);
	}

	private boolean timeToCollect(Process p) {
		return runningQueue.containsValue(p) && timeUp(p);
	}

	private boolean timeUp(Process p) {
		return p.getProgramCounter() >= schedulTime;
	}

	private boolean isDone() {
		return readyQueueIsEmpty() && runningQueueIsEmpty();
	}

	private boolean readyQueueIsEmpty() {
		return readyQueue.size() == 0;
	}

	private boolean runningQueueIsEmpty() {
		return runningQueue.size() == 0;
	}

	private void shortTermSchedule() {
		if (cpuIsFull()) {
			return;
		}
		if (readyProcessExists()) {
			Process p = extractProcessToRun();
			addToRunningQueue(p);
		}
	}

	private void addToRunningQueue(Process p) {
		runningQueue.put(p.getID(), p);

	}

	private Process extractProcessToRun() {
		Iterator<Integer> keys = readyQueue.keySet().iterator();
		Process firstCandidate = readyQueue.get(keys.next());
		while (keys.hasNext()) {
			Process secondCandidate = readyQueue.get(keys.next());
			if (getWaitingTime(secondCandidate) > getWaitingTime(firstCandidate)) {
				firstCandidate = secondCandidate;
			}
		}
		return firstCandidate;
	}

	private boolean readyProcessExists() {
		return readyQueue.size() != 0;
	}

	private boolean cpuIsFull() {
		return runningQueue.size() == cores;
	}

	public void addToReadyQueue(Process p) {
		p.setStartedReadyOn(clock);
		readyQueue.put(p.getID(), p);
	}

	public void setCores(int cores) {
		this.cores = cores;
	}

	public void setSchedulTime(int schedulTime) {
		this.schedulTime = schedulTime;
	}

	public int getCores() {
		return cores;
	}

	public int getSchedulTime() {
		return schedulTime;
	}

	public Executer getExe() {
		return exe;
	}

	public MyThread getCmdThread() {
		return defaultProcess.getMainThread();
	}

	public int getWaitingTime(Process p) {
		return clock - p.getStartedReadyOn();
	}
}
