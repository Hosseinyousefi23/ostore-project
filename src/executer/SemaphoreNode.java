package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class SemaphoreNode extends Node {
	private Node sname;
	private ExprNode svalue;

	public SemaphoreNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void init() {
		sname = children.get(2);
		svalue = (ExprNode) children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		String name = sname.getContent();
		svalue.execute(t);
		Integer value = (Integer) svalue.getResult();
		t.getProcess().getScheduler().addSemaphore(name, value);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
