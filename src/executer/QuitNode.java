package executer;

import ui.UserInterface;

public class QuitNode extends Node {

	public QuitNode(String name) {
		super(name);
	}

	@Override
	public void execute(Thread t) {
		UserInterface.quited = true;
	}
}
