package executer;

public class EchoNode extends Node {

	public EchoNode(String name) {
		super(name);
	}
	
	@Override
	public void execute(Thread t) {
		ExprNode echoNode=(ExprNode) children.get(2);
		echoNode.execute(t);
		Object resultecho= echoNode.getResult();
		System.out.println(resultecho);
		
	}

}
