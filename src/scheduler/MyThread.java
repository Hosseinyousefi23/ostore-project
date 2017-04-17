package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.ParseTree;
import parser.Parser;

public class MyThread {
	private static int idGenerator = 1;
	private int id;
	private String code;
	private ParseTree programTree;
	private ArrayList<Process> waitingQueue;
	private ArrayList<HashMap<String, Object>> localVars;
	private Process parent;

	public static int getNewTid() {
		return idGenerator++;
	}

	private void init(int tid, String code, Process parent) {
		this.id = tid;
		setCode(code);
		setParent(parent);
		waitingQueue = new ArrayList<Process>();
		localVars = new ArrayList<HashMap<String, Object>>();
		Parser p = new Parser();
		programTree = p.parse(code);
	}

	public MyThread(int tid, String code, int pc, Process parent) {
		init(tid, code, parent);

	}

	public MyThread(int tid, String code, Process parent) {
		init(tid, code, parent);
	}

	public void setParent(Process parent) {
		this.parent = parent;
	}

	public Process getProcess() {
		return this.parent;
	}

	public int getID() {
		return id;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
