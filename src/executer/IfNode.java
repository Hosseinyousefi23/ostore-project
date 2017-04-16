package executer;

import scheduler.MyThread;

public class IfNode extends Node {

	public IfNode(String name) {
		super(name);
	}

	@Override
	public void execute(MyThread t) {
		ExprNode expression = (ExprNode) children.get(2);
		Node ifBlock = children.get(4).children.get(1);
		expression.execute(t);
		if (intToBoolean((int) expression.getResult())) {
			ifBlock.execute(t);
		}
		if (hasElse()) {
			Node elseBlock = children.get(6).children.get(1);
			if (!intToBoolean((int) expression.getResult())) {
				elseBlock.execute(t);
			}
		}
	}

	private boolean hasElse() {
		return children.size() == 7;
	}

}
