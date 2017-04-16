package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import semaphore.Semaphore;
import executer.Executer;

public class Scheduler {
	public int clock = 0;
	private int cores = 4;
	private int schedulTime = 100;
	private Process defaultProcess;
	private ArrayList<Process> readyQueue;
	private ArrayList<Process> startingQueue;
	private HashMap<String, Semaphore> semaphores;
	private Executer exe;

	public Scheduler(int cores, int schedulTime) {
		readyQueue = new ArrayList<Process>();
		startingQueue = new ArrayList<Process>();
		setCores(cores);
		setSchedulTime(schedulTime);
		exe = new Executer();
		semaphores = new HashMap<String, Semaphore>();
		// defaultProcess = new Process();

	}

	public void start(String code) {
		Process init = new Process(code);
		startingQueue.add(init);
		//TODO long term and short term
	}

	public void executeProcess(Process p) {

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
}
