package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import scheduler.Process;
import scheduler.Scheduler;

public class CreateProcessNode extends Node {

	public CreateProcessNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void execute(MyThread t) {
		int pid = Process.getNewPid();
		Scheduler sc = t.getProcess().getScheduler();
		Process child = new Process(pid, t.getProgramTree(), t.getProcess(),
				sc, t.getID());
		sc.addToReadyQueue(child);
		t.getProcess().addChildProcess(child);

	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
