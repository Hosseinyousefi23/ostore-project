package executer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import parser.Node;
import parser.ParseTree;
import parser.Parser;
import scheduler.MyThread;
import scheduler.Process;
import scheduler.Scheduler;

public class ExecNode extends Node {

	private ExprNode filePath;

	public ExecNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		filePath = (ExprNode) children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		Scheduler sc = t.getProcess().getScheduler();
		filePath.execute(t);
		String path = (String) filePath.getResult();
		try {
			Scanner scan = new Scanner(new File(path));
			String code = scan.useDelimiter("\\Z").next();
			scan.close();
			Parser p = new Parser();
			ParseTree tree = p.parse(code);
			Process process = t.getProcess();
			process.getMainThread().setProgramTree(tree);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
