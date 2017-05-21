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
	}

	@Override
	public void init() {
		super.init();
		firstAssign = (AssignmentNode) children.get(2);
		secondAssign = (AssignmentNode) children.get(6);
		controller = (ExprNode) children.get(4);
		block = children.get(8).getChildren().get(1);
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
	protected Node findNextInstruction(MyThread t) {
		if (nextCommands.get(t.getID()) == firstAssign) {
			return controller;
		} else if (nextCommands.get(t.getID()) == secondAssign) {
			return controller;
		} else if (nextCommands.get(t.getID()) == controller) {
			if (intToBoolean((int) controller.getResult())) {
				return block;
			} else {
				return null;
			}
		} else if (nextCommands.get(t.getID()) == block) {
			block.getNextCommands().replace(t.getID(), block.getChildren().get(0));
			block.setDone(false, t.getID());
			return secondAssign;
		}
		return null;
	}

	@Override
	public void initializeThread(int tid) {
		super.initializeThread(tid);
		nextCommands.put(tid, firstAssign);
	}

}
