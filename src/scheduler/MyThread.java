package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import parser.ParseTree;
import parser.Parser;

public class MyThread {
	private int id;
	private String code;
	private ParseTree programTree;
	private ArrayList<Process> waitingQueue;
	private ArrayList<HashMap<String, Object>> localVars;
	private Process parent;

	private void init(String code, Process parent) {
		setCode(code);
		setParent(parent);
		waitingQueue = new ArrayList<Process>();
		localVars = new ArrayList<HashMap<String, Object>>();
		Parser p = new Parser();
		programTree = p.parse(code);
	}

	public MyThread(String code, int pc, Process parent) {
		init(code, parent);

	}

	public MyThread(String code, Process parent) {
		init(code, parent);
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
