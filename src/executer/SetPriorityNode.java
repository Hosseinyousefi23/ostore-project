package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class SetPriorityNode extends Node {

	private ExprNode priority;

	public SetPriorityNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		priority = (ExprNode) children.get(2);
		super.init();
	}

	@Override
	public void execute(MyThread t) {
		priority.execute(t);
		t.setPriority((int) priority.getResult());
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
