package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class CreateThreadNode extends Node {

	private Node variable;

	public CreateThreadNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void init() {
		variable = children.get(2).getChildren().get(0).getChildren().get(0);
	}

	@Override
	public void execute(MyThread t) {
		int tid = MyThread.getNewTid();
		MyThread child = new MyThread(tid, t.getProgramTree(), t.getPc(), t.getProcess(), t);
		t.getProcess().addThread(child);
		t.getProcess().runThread(child);
		String varname = variable.getContent();
		t.addLocalVar(varname, tid);
		child.addLocalVar(varname, 0);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
