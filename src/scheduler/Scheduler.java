package scheduler;

import java.util.ArrayList;

import executer.Executer;

public class Scheduler {
	private int cores = 4;
	private int schedulTime = 100;
	private Process defaultProcess;
	private ArrayList<Process> readyQueue;
	private Executer exe;

	public Scheduler(int cores, int schedulTime) {
		readyQueue = new ArrayList<Process>();
		setCores(cores);
		setSchedulTime(schedulTime);
		exe = new Executer();
		defaultProcess = new Process();

	}

	public void start(String code) {

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
