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
		UserInterface.quited = true;
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
