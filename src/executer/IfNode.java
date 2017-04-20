package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class IfNode extends Node {
	private ExprNode expression;
	private Node ifBlock;
	private Node elseBlock;

	public IfNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		expression = (ExprNode) children.get(2);
		ifBlock = children.get(4).getChildren().get(1);
		if (hasElse()) {
			elseBlock = children.get(6).getChildren().get(1);
		}
	}

	@Override
	public void execute(MyThread t) {
		expression.execute(t);
		if (intToBoolean((int) expression.getResult())) {
			ifBlock.execute(t);
		}
		if (hasElse()) {
			elseBlock = children.get(6).getChildren().get(1);
			if (!intToBoolean((int) expression.getResult())) {
				elseBlock.execute(t);
			}
		}
	}

	@Override
	protected Node findNextInstruction(MyThread t) {
		if (nextCommands.get(t.getID()) == expression) {
			if (intToBoolean((int) expression.getResult())) {
				return ifBlock;
			} else {
				return elseBlock;
			}
		} else if (nextCommands.get(t.getID()) == ifBlock || nextCommands.get(t.getID()) == elseBlock) {
			return null;
		}
		return null;
	}

	private boolean hasElse() {
		return children.size() == 7;
	}

	@Override
	public void initializeThread(int tid) {
		super.initializeThread(tid);
		nextCommands.put(tid, expression);
	}

}
