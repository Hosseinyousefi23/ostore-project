package scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

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
	private HashMap<Integer, Process> allProcesses;

	public Scheduler(int cores, int schedulTime) {
		readyQueue = new HashMap<Integer, Process>();
		runningQueue = new HashMap<Integer, Process>();
		allProcesses = new HashMap<Integer, Process>();
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
		addProcess(init);
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

	public void exec(Process process, String filePath) {
		try {
			Scanner scan = new Scanner(new File(filePath));
			String code = scan.useDelimiter("\\Z").next();
			scan.close();
			Parser p = new Parser();
			ParseTree tree = p.parse(code);
			int pid = process.getID();
			Process parent = process.getParent();
			process = new Process(pid, tree, parent, this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void addProcess(Process p) {
		allProcesses.put(p.getID(), p);
	}

	public void killProcess(Integer id) {
		Process p = allProcesses.get(id);
		for (MyThread t : p.getAllThreads().values()) {
			killThread(t);
		}
		for (MyThread waiter : p.getWaiters()) {
			waiter.getProcess().runThread(waiter);
		}
		allProcesses.remove(p);
		runningQueue.remove(p);
		readyQueue.remove(p);
		p = null;
	}

	public void killThread(MyThread t) {

		for (MyThread thread : t.getWaiters()) {
			thread.getProcess().runThread(thread);
		}
		for (Process process : allProcesses.values()) {
			process.getWaiters().remove(t);
		}

		for (Semaphore s : semaphores.values()) {
			s.getWaiters().remove(t);
		}

		t.getProcess().removeThread(t);
		t = null;

	}

	public Process getProcess(Integer pid) {
		return allProcesses.get(pid);
	}

	public MyThread getThread(Integer tid) {
		for (Process p : allProcesses.values()) {
			if (p.getAllThreads().containsKey(tid)) {
				return p.getAllThreads().get(tid);
			}
		}
		return null;
	}

	public void addSemaphore(String name, Integer value) {
		semaphores.put(name, new Semaphore(value));

	}

	public void signal(String name) {
		Semaphore s = semaphores.get(name);
		s.signal();
	}

	public void waitSemaphore(MyThread t, String name) {
		semaphores.get(name).Wait(t);
	}
}
