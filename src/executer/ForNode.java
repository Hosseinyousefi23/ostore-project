package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ForNode extends Node {

	public ForNode(String name, ParseTree tree) {
		super(name,tree);
	}

	@Override
	public void execute(MyThread t) {
		AssignmentNode firstAssign = (AssignmentNode) children.get(2);
		AssignmentNode secondAssign = (AssignmentNode) children.get(6);
		ExprNode Controller = (ExprNode) children.get(4);
		Node Block = children.get(8).getChildren().get(1);

		firstAssign.execute(t);
		Controller.execute(t);
		int control = (int) Controller.getResult();

		while (intToBoolean(control)) {
			Block.execute(t);
			secondAssign.execute(t);
			Controller.execute(t);
			control = (int) Controller.getResult();

		}

	}

}
