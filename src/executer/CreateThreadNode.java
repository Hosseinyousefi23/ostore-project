package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class CreateThreadNode extends Node {

	public CreateThreadNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		int tid = MyThread.getNewTid();
		MyThread child = new MyThread(tid, t.getProgramTree(), t.getPc(),
				t.getProcess(), t.getID());
		t.getProcess().runThread(child);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done();
	}

}
