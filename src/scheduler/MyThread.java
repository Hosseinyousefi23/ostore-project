package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.ParseTree;

public class MyThread {
	private static int idGenerator = 1;
	private int id;
	private String code;
	private int waitTime = 0;
	private int relationalWaitTime = 0; // waitTime between threads of same
										// process (ignore priority)
	private int priority = 1;
	private int currentCPUBurstTime = 0;
	private int programCounter = 1;
	private ParseTree programTree;
	private String status;
	private ArrayList<MyThread> waiters;
	private HashMap<String, Variable> localVars;
	private Process process;

	public static int getNewTid() {
		return idGenerator++;
	}

	private void init(int tid, ParseTree programTree, Process process) {
		this.id = tid;
		setParent(process);
		waiters = new ArrayList<MyThread>();
		localVars = new HashMap<String, Variable>();
		this.programTree = programTree;
	}

	public MyThread(int tid, ParseTree programTree, Process process) {
		init(tid, programTree, process);
		this.programTree.addThread(tid);
	}

	public MyThread(int tid, ParseTree programTree, int pc, Process process, MyThread parent,
			HashMap<String, Variable> localVars) {
		init(tid, programTree, process);
		this.programCounter = pc + 1;
		programTree.addNewThread(parent, this);
		initLocalVars(localVars);
	}

	public MyThread(int tid, ParseTree programTree, int pc, Process process, MyThread parent) {
		init(tid, programTree, process);
		this.programCounter = pc + 1;
		programTree.addNewThread(parent, this);
	}

	private void initLocalVars(HashMap<String, Variable> localVariables) {
		Object[] keys = localVariables.keySet().toArray();
		for (Object key : keys) {
			Variable var = localVariables.get(key);
			String newKey = new String((String) key);
			if (var.isPrivate())
				continue;
			addLocalVar(newKey, var.getValue());
		}
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
		programTree.getRoot().executeInstruction(this);
		programCounter++;
		if (programTree.getRoot().isDone(id)) {
			process.getScheduler().killThread(this);
		}

	}

	public ParseTree getProgramTree() {
		return programTree;
	}

	public int getPc() {
		return programCounter;
	}

	public void addLocalVar(String name, Object value) {
		localVars.put(name, new Variable(value));
	}

	public void addPrivateLocalVar(String name, Object value) {
		Variable var = new Variable(value);
		var.setPrivate();
		localVars.put(name, var);

	}

	public Object getLocalVar(String varname) {
		if (localVars.containsKey(varname)) {
			return localVars.get(varname).getValue();
		}
		return null;
	}

	public ArrayList<MyThread> getWaiters() {
		return waiters;
	}

	public int waitTime() {
		return waitTime;
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

	public HashMap<String, Variable> getLocalVars() {
		return localVars;
	}

	public void setProgramTree(ParseTree programTree) {
		this.programTree = programTree;
		this.programTree.addThread(id);
	}

	public void increaseWaitTime() {
		waitTime++;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public boolean timeUp() {
		return currentCPUBurstTime == priority;
	}

	public void increateCurrentCPUBurstTime() {
		currentCPUBurstTime++;
	}

	public void resetCurrentCPUBurstTime() {
		currentCPUBurstTime = 0;
	}

	public void increaseRelationalWaitTime() {
		relationalWaitTime++;
	}

	public int relationalwaitTime() {
		return relationalWaitTime;
	}
}
