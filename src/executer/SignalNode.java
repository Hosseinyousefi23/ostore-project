package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class SignalNode extends Node {

	private Node sname;

	public SignalNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void init() {
		sname = children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		String name = sname.getContent();
		t.getProcess().getScheduler().signal(name);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
