package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.ParseTree;
import parser.Parser;

public class MyThread {
	private static int idGenerator = 1;
	private int id;
	private String code;
	private int programCounter;
	private ParseTree programTree;

	private ArrayList<Process> waitingQueue;
	private ArrayList<HashMap<String, Object>> localVars;
	private Process process;

	public static int getNewTid() {
		return idGenerator++;
	}

	private void init(int tid, ParseTree programTree, Process parent) {
		this.id = tid;
		setCode(code);
		setParent(parent);
		waitingQueue = new ArrayList<Process>();
		localVars = new ArrayList<HashMap<String, Object>>();
		this.programTree = programTree;
	}

	public MyThread(int tid, ParseTree programTree, Process parent) {
		init(tid, programTree, parent);
		programTree.addThread(tid);
	}

	public MyThread(int tid, ParseTree programTree, int pc, Process parent,
			int parentTid) {
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

}
