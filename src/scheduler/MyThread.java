package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.ParseTree;

public class MyThread {
	private static int idGenerator = 1;
	private int id;
	private String code;
	private int programCounter;
	private ParseTree programTree;
	private String status;
	private int lastRun;
	private ArrayList<MyThread> waiters;
	private HashMap<String, Object> localVars;
	private Process process;

	public static int getNewTid() {
		return idGenerator++;
	}

	private void init(int tid, ParseTree programTree, Process parent) {
		this.id = tid;
		setCode(code);
		setParent(parent);
		waiters = new ArrayList<MyThread>();
		localVars = new HashMap<String, Object>();
		this.programTree = programTree;
	}

	public MyThread(int tid, ParseTree programTree, Process parent) {
		init(tid, programTree, parent);
		programTree.addThread(tid);
	}

	public MyThread(int tid, ParseTree programTree, int pc, Process parent, int parentTid) {
		init(tid, programTree, parent);
		this.programCounter = pc;
		programTree.addNewThread(parentTid, tid);
	}

	public void setParent(Process process) {
		this.process = process;
	}

	public Process getProcess() {
		return this.process;
	}

	public int getID() {
		return id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void executeNextInstruction() {
		lastRun = process.getScheduler().clock;
		programTree.getRoot().executeInstruction(this);
		programCounter++;
		if (programTree.getRoot().isDone()) {
			process.finish(this);
		}

	}

	public ParseTree getProgramTree() {
		return programTree;
	}

	public int getPc() {
		return programCounter;
	}

	public void addLocalVar(String name, Object value) {
		localVars.put(name, value);
	}

	public Object getLocalVar(String varname) {
		if (localVars.containsKey(varname)) {
			return localVars.get(varname);
		}
		return null;
	}

	public ArrayList<MyThread> getWaiters() {
		return waiters;
	}

	public int waitTime() {
		return process.getScheduler().clock - lastRun;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void addWaiter(MyThread t) {
		waiters.add(t);
	}
}
