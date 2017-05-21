package executer;

import parser.ParseTree;
import parser.Parser;
import scheduler.MyThread;

public class Executer {
	private Parser parser;

	public Executer() {
		parser = new Parser();
	}

	public void execute(String code, MyThread thread) {
		ParseTree tree = parser.parse(code);
		tree.getRoot().execute(thread);
	}
}
