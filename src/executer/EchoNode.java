package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class EchoNode extends Node {

	public EchoNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		ExprNode echoNode = (ExprNode) children.get(2);
		echoNode.execute(t);
		Object resultecho = echoNode.getResult();
		System.out.println(resultecho);

	}

}
