package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;
import ui.UserInterface;

public class QuitNode extends Node {

	public QuitNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		if (t == null) {
			UserInterface.quited = true;
		} else {
			t.getProcess().getScheduler().killThread(t);
		}
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
