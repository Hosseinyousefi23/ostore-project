package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import ui.UserInterface;

public class GlobalAssignmentNode extends Node {
	private ExprNode assign;
	private Node assignId;

	public GlobalAssignmentNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		assign = (ExprNode) children.get(4);
		assignId = children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		assign.execute(t);
		Object resultassign = assign.getResult();
		if (t == null) {
			UserInterface.interVars.put(assignId.getContent(), resultassign);
		} else {
			t.getProcess().addGlobalVar(assignId.getContent(), resultassign);
		}
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
