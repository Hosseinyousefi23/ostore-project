package parser;

public class ParseTree {

	private Node root;
	private int programCounter = 0;

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public void increasePC() {
		programCounter++;
	}

}
