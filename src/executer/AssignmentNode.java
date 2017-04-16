package executer;

public class AssignmentNode extends Node {

	public AssignmentNode(String name) {
		super(name);

	}

	@Override
	public void execute(Thread t) {
		ExprNode assign= (ExprNode) children.get(4);
		Node assignId = children.get(2);
		assign.execute(t);
		Object resultassign= assign.getResult();
		if(Executer.variables.containsKey(assignId.getContent())){
			Executer.variables.replace(assignId.getContent(), resultassign);
		}else{
			Executer.variables.put(assignId.getContent(), resultassign);
		}
			
		
		
	}
}
