package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WhileNode extends Node {
	private ExprNode whileController;
	private Node whileBlock;

	public WhileNode(String name, ParseTree tree) {
		super(name, tree);
		whileController = (ExprNode) children.get(2);
		whileBlock = children.get(4).getChildren().get(1);
		
	}

	@Override
	public void execute(MyThread t) {
		whileController = (ExprNode) children.get(2);
		whileBlock = children.get(4).getChildren().get(1);

		whileController.execute(t);
		int control = (int) whileController.getResult();

		while (intToBoolean(control)) {
			whileBlock.execute(t);
			whileController.execute(t);
			control = (int) whileController.getResult();
		}

	}

	@Override
	protected Node findNextInstruction(MyThread t) {
		if (nextCommands.get(t.getID()) == whileController) {
			if (intToBoolean((int) whileController.getResult())) {
				return whileBlock;
			} else {
				return null;
			}
		} else if (nextCommands.get(t.getID()) == whileBlock) {
			return whileController;
		}
		return null;
	}

}
