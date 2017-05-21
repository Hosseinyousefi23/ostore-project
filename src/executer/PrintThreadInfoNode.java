package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class PrintThreadInfoNode extends Node {

	public PrintThreadInfoNode(String name, ParseTree tree) {
		super(name, tree);
		// TODO Auto-generated constructor stub
	}

	public void execute(MyThread t) {
		System.out.println("thread_id: " + t.getID() + ", pid: " + t.getProcess().getID());
		System.out.println("clock: " + t.getPc() + ", wait: " + t.waitTime() + ", status: " + t.getStatus());
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
