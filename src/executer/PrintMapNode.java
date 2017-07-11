package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class PrintMapNode extends Node {

	public PrintMapNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void execute(MyThread t) {
		t.getProcess().getScheduler().getOs().getFileManager().getBasicStorageManager().getStorage().printMap();
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
