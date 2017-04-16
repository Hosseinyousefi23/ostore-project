package executer;

public class WhileNode extends Node {

	public WhileNode(String name) {
		super(name);
	}

	@Override
	public void execute(Thread t) {
		ExprNode whilecontroller = (ExprNode) children.get(2);
		Node whileBlock = children.get(4).children.get(1);

		whilecontroller.execute(t);
		int control = (int) whilecontroller.getResult();

		while (intToBoolean(control)) {
			whileBlock.execute(t);
			whilecontroller.execute(t);
			control = (int) whilecontroller.getResult();

		}

	}

}
