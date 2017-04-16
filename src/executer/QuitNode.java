package executer;

import scheduler.MyThread;
import ui.UserInterface;

public class QuitNode extends Node {

	public QuitNode(String name) {
		super(name);
	}

	@Override
	public void execute(MyThread t) {
		UserInterface.quited = true;
	}
}
