package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class PrintProcessInfoNode extends Node {

	public PrintProcessInfoNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void execute(MyThread t) {
		System.out.println("pid: " + t.getProcess().getID());
		System.out.println("threads: " + t.getProcess().getThreadsSize());
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
