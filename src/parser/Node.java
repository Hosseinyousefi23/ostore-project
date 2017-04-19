package parser;

import java.util.ArrayList;
import java.util.HashMap;

import scheduler.MyThread;

public class Node {
	protected ParseTree tree;
	protected Node parent;
	protected ArrayList<Node> children;
	protected HashMap<Integer, Node> nextCommands;
	protected String name;
	protected String content;
	protected boolean isDone = false;

	public Node(String name, ParseTree tree) {
		children = new ArrayList<Node>();
		this.name = name;
		this.tree = tree;
		nextCommands = new HashMap<Integer, Node>();
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void addChild(Node n) {
		children.add(n);
	}

	public Node getParrent() {
		return parent;
	}

	public void setParrent(Node parrent) {
		this.parent = parrent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public void execute(MyThread t) {
		for (Node n : children) {
			n.execute(t);
		}
	}

	public boolean isNonTerminal() {
		return name.startsWith("<") && name.endsWith(">");
	}

	public void setContent(String content) {
		this.content = content;

	}

	public String getContent() {
		return content;
	}

	protected boolean intToBoolean(int i) {
		return i != 0;
	}

	public boolean isDone() {
		return isDone;
	}

	protected void done() {
		isDone = true;
	}

	protected Node findNextInstruction(MyThread t) {
		int index = parent.children.indexOf(nextCommands.get(t.getID()));
		if (index + 1 < parent.children.size()) {
			return parent.children.get(index + 1);
		}
		return null;
	}

	public void executeInstruction(MyThread t) {
		nextCommands.get(t.getID()).executeInstruction(t);
		if (nextCommands.get(t.getID()).isDone()) {
			nextCommands.replace(t.getID(), findNextInstruction(t));
			if (nextCommands.get(t.getID()) == null) {
				done();
			}
		}

	}

	public void initializeNewThread(int parentTid, int childTid) {
		if (nextCommands.containsKey(parentTid)) {
			Node next = nextCommands.get(parentTid);
			nextCommands.put(childTid, next);
			for (Node n : children) {
				n.initializeNewThread(parentTid, childTid);
			}
		}
	}

	public void initializeThread(int tid) {
		for (Node child : children) {
			child.initializeThread(tid);
		}
	}
}
