package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class AssignmentNode extends Node {

	public AssignmentNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void execute(MyThread t) {
		ExprNode assign = (ExprNode) children.get(4);
		Node assignId = children.get(2);
		assign.execute(t);
		Object resultassign = assign.getResult();
		t.getProcess().addGlobalVar(assignId.getContent(), resultassign);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
