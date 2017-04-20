package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import scheduler.Process;
import scheduler.Scheduler;

public class CreateProcessNode extends Node {

	private Node variable;

	public CreateProcessNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void init() {
		variable = children.get(2).getChildren().get(0).getChildren().get(0);
	}

	@Override
	public void execute(MyThread t) {
		int pid = Process.getNewPid();
		Scheduler sc = t.getProcess().getScheduler();
		Process child = new Process(pid, t.getProgramTree(), t.getProcess(), sc, t);
		sc.addToReadyQueue(child);
		sc.addProcess(child);
		t.getProcess().addChildProcess(child);
		String varname = variable.getContent();
		t.addLocalVar(varname, child.getMainThread().getID());
		child.getMainThread().addLocalVar(varname, 0);

	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
