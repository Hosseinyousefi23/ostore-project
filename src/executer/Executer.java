package executer;

import java.util.HashMap;

import parser.Parser;
import scheduler.MyThread;

public class Executer {
	private Parser parser;
	public static HashMap<String, Object> variables;

	public Executer() {
		variables = new HashMap<String, Object>();
		parser = new Parser();
	}

	public void execute(String code, MyThread thread) {
		Node root = parser.parse(code);
		root.execute(thread);
	}
}
