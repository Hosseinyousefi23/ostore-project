package scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import executer.Node;

public class MyThread {
	private int id;
	private ArrayList<HashMap<String, Object>> localVars;
	private Node programRoot;
	private Process parent;

	public MyThread(Node bigRoot, Process parent) {
		localVars = new ArrayList<HashMap<String, Object>>();
		setProgramRoot(bigRoot);
		setParent(parent);
	}

	public void setProgramRoot(Node programRoot) {
		this.programRoot = programRoot;
	}

	public void setParent(Process parent) {
		this.parent = parent;
	}

	public Process getProcess() {
		return this.parent;
	}
	
	public int getID(){
	return id;
	}
	
}
