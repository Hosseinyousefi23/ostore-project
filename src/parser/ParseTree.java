package parser;

import java.util.HashMap;

import scheduler.MyThread;

public class ParseTree {

	private Node root;
	private HashMap<Integer, Integer> programCounters;

	public ParseTree() {
		programCounters = new HashMap<Integer, Integer>();
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public void addThread(int tid) {
		programCounters.put(tid, 1);
		root.initializeThread(tid);
	}

	public void addNewThread(MyThread parent, MyThread child) {
		int parentPc = programCounters.get(parent.getID());
		programCounters.put(child.getID(), parentPc);
		root.initializeNewThread(parent, child);
	}

	public int getPc(int parentTid) {
		return programCounters.get(parentTid);
	}

	public void init() {
		root.init();
	}
}
