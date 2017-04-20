package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class KillProcessNode extends Node {

	private ExprNode processId = (ExprNode) children.get(2);

	public KillProcessNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		processId.execute(t);
		Integer id = (Integer) processId.getResult();
		t.getProcess().getScheduler().killProcess(id);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
