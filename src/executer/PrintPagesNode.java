<<<<<<< HEAD
package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class PrintPagesNode extends Node {

	public PrintPagesNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		String frame = t.getProcess().getScheduler().getOs().getMemManager().getFrame();
		System.out.println(frame);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
=======
package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class PrintPagesNode extends Node {

	public PrintPagesNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		String frame = t.getProcess().getScheduler().getOs().getMemManager().getFrame();
		System.out.println(frame);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
>>>>>>> refs/remotes/origin/master
