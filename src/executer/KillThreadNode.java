package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import scheduler.Scheduler;

public class KillThreadNode extends Node {

	private ExprNode threadId;

	public KillThreadNode(String name, ParseTree tree) {
		super(name, tree);
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
		MyThread thread = sc.getThread(tid);
		t.getProcess().getScheduler().killThread(thread);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
