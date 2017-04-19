package parser;

import java.util.HashMap;

public class ParseTree {

	private Node root;
	private HashMap<Integer, Integer> programCounters;

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

	public void addNewThread(int parentTid, int childTid) {
		int parentPc = programCounters.get(parentTid);
		programCounters.put(childTid, parentPc);
		root.initializeNewThread(parentTid, childTid);
	}

	public int getPc(int parentTid) {
		return programCounters.get(parentTid);
	}
}
