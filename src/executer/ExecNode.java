package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import scheduler.Scheduler;

public class ExecNode extends Node {

	public ExecNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		int pid = t.getProcess().getID();
		Scheduler sc = t.getProcess().getScheduler();
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
