package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WaitNode extends Node {

	private Node sname = children.get(2);

	public WaitNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		String name = sname.getContent();
		t.getProcess().getScheduler().waitSemaphore(t, name);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}
}
