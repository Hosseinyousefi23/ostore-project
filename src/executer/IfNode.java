package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class IfNode extends Node {

	public IfNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		ExprNode expression = (ExprNode) children.get(2);
		Node ifBlock = children.get(4).getChildren().get(1);
		expression.execute(t);
		if (intToBoolean((int) expression.getResult())) {
			ifBlock.execute(t);
		}
		if (hasElse()) {
			Node elseBlock = children.get(6).getChildren().get(1);
			if (!intToBoolean((int) expression.getResult())) {
				elseBlock.execute(t);
			}
		}
	}

	private boolean hasElse() {
		return children.size() == 7;
	}

}
