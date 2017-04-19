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
		if (Executer.variables.containsKey(assignId.getContent())) {
			Executer.variables.replace(assignId.getContent(), resultassign);
		} else {
			Executer.variables.put(assignId.getContent(), resultassign);
		}
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}
}
