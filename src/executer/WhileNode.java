package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WhileNode extends Node {

	public WhileNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		ExprNode whilecontroller = (ExprNode) children.get(2);
		Node whileBlock = children.get(4).getChildren().get(1);

		whilecontroller.execute(t);
		int control = (int) whilecontroller.getResult();

		while (intToBoolean(control)) {
			whileBlock.execute(t);
			whilecontroller.execute(t);
			control = (int) whilecontroller.getResult();

		}

	}

}
