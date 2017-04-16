package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class PrintProcessInfoNode extends Node {

	public PrintProcessInfoNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void execute(MyThread thread) {
		System.out.println("pid: " + thread.getProcess().getID());
		System.out.println("threads: " + thread.getProcess().getThreadsSize());
	}

}
