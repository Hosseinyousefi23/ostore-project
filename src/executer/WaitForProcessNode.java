package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import scheduler.Scheduler;

public class WaitForProcessNode extends Node {

	private ExprNode processId = (ExprNode) children.get(2);

	public WaitForProcessNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void execute(MyThread t) {
		processId.execute(t);
		Integer pid = (Integer) processId.getResult();
		Scheduler sc = t.getProcess().getScheduler();
		sc.getProcess(pid).addWaiter(t);
		t.getProcess().stopThread(t);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
