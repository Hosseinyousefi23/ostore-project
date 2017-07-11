package executer;

import filesystem.BasicStorageManager;
import filesystem.FileOrganizationModule;
import filesystem.PhysicalStorage;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class PrintFreeMapNode extends Node {

	public PrintFreeMapNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void execute(MyThread t) {
		BasicStorageManager storageManager = t.getProcess().getScheduler().getOs().getFileManager()
				.getBasicStorageManager();
		storageManager.printFreeMap();
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
