package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ForNode extends Node {

	private AssignmentNode firstAssign;
	private ExprNode controller;
	private AssignmentNode secondAssign;
	private Node block;

	public ForNode(String name, ParseTree tree) {
		super(name, tree);
		firstAssign = (AssignmentNode) children.get(2);
		secondAssign = (AssignmentNode) children.get(6);
		controller = (ExprNode) children.get(4);
		block = children.get(8).getChildren().get(1);
		nextCommand = firstAssign;
	}

	@Override
	public void execute(MyThread t) {

		firstAssign.execute(t);
		controller.execute(t);
		int control = (int) controller.getResult();

		while (intToBoolean(control)) {
			block.execute(t);
			secondAssign.execute(t);
			controller.execute(t);
			control = (int) controller.getResult();

		}

	}

	@Override
	protected Node findNextInstruction() {
		if (nextCommand == firstAssign) {
			return controller;
		} else if (nextCommand == secondAssign) {
			return controller;
		} else if (nextCommand == controller) {
			if (intToBoolean((int) controller.getResult())) {
				return block;
			} else {
				return null;
			}
		} else if (nextCommand == block) {
			return secondAssign;
		}
		return null;
	}

}
