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

	public void init() {
		for (Node child : children) {
			child.init();
		}
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
		int index = children.indexOf(nextCommands.get(t.getID()));
		if (index + 1 < children.size()) {
			return children.get(index + 1);
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

	public void initializeNewThread(MyThread parent, MyThread child) {
		if (nextCommands.containsKey(parent.getID())) {
			Node next = nextCommands.get(parent.getID());
			nextCommands.put(child.getID(), next);
			if (nextCommands.get(child.getID()).getName().equals("<create_process>")
					|| nextCommands.get(child.getID()).getName().equals("<create_thread>")) {
				nextCommands.replace(child.getID(), findNextInstruction(child));
			}
			for (Node n : children) {
				n.initializeNewThread(parent, child);
			}
		}
	}

	public void initializeThread(int tid) {
		if (haveChildren()) {
			nextCommands.put(tid, children.get(0));
		}
		for (Node child : children) {
			child.initializeThread(tid);
		}
	}

	private boolean haveChildren() {
		return children.size() > 0;
	}

	public HashMap<Integer, Node> getNextCommands() {
		return nextCommands;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
