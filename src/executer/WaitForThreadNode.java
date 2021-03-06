package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import scheduler.Scheduler;

public class WaitForThreadNode extends Node {

	private ExprNode threadId;

	public WaitForThreadNode(String name, ParseTree tree) {
		super(name, tree);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		threadId = (ExprNode) children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		threadId.execute(t);
		Integer tid = (Integer) threadId.getResult();
		Scheduler sc = t.getProcess().getScheduler();
		sc.getThread(tid).addWaiter(t);
		t.getProcess().stopThread(t);
		t.setStatus("waiting");
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
