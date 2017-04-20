package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class SemaphoreNode extends Node {
	private Node sname = children.get(2);
	private ExprNode svalue = (ExprNode) children.get(3);

	public SemaphoreNode(String name, ParseTree tree) {
		super(name, tree);

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
		done();
	}
}
