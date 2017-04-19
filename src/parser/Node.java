package parser;

import java.util.ArrayList;

import scheduler.MyThread;

public class Node {
	protected ParseTree tree;
	protected Node parrent;
	protected ArrayList<Node> children;
	protected Node nextCommand = children.get(0);
	protected String name;
	protected String content;
	protected boolean isDone = false;

	public Node(String name, ParseTree tree) {
		children = new ArrayList<Node>();
		this.name = name;
		this.tree = tree;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void addChild(Node n) {
		children.add(n);
	}

	public Node getParrent() {
		return parrent;
	}

	public void setParrent(Node parrent) {
		this.parrent = parrent;
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

	public void execute(MyThread thread) {
		for (Node n : children) {
			n.execute(thread);
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

	protected Node findNextInstruction() {
		int index = parrent.children.indexOf(nextCommand);
		if (index + 1 < parrent.children.size()) {
			return parrent.children.get(index + 1);
		}
		return null;
	}

	public void executeInstruction(MyThread t) {
		nextCommand.executeInstruction(t);
		if (nextCommand.isDone()) {
			nextCommand = findNextInstruction();
			if (nextCommand == null) {
				done();
			}
		}

	}

}
