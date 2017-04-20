package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import scheduler.Scheduler;

public class ExecNode extends Node {

	private ExprNode filePath = (ExprNode) children.get(2);

	public ExecNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		int pid = t.getProcess().getID();
		Scheduler sc = t.getProcess().getScheduler();
		filePath.execute(t);
		sc.exec(t.getProcess(), (String) filePath.getResult());
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
