package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WhileNode extends Node {
	private ExprNode whileController;
	private Node whileBlock;

	public WhileNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		whileController = (ExprNode) children.get(2);
		whileBlock = children.get(4).getChildren().get(1);
	}

	@Override
	public void execute(MyThread t) {

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
			whileBlock.getNextCommands().replace(t.getID(), whileBlock.getChildren().get(0));
			whileBlock.setDone(false);
			return whileController;
		}
		return null;
	}

	@Override
	public void initializeThread(int tid) {
		super.initializeThread(tid);
		nextCommands.put(tid, whileController);
	}

}
