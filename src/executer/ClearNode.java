package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ClearNode extends Node {

	private ExprNode fileNumber;

	public ClearNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileNumber = (ExprNode) children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileNumber.execute(t);
		int fileNumberRes = (Integer) fileNumber.getResult();
		File f = fileManager.findFileByNumber(fileNumberRes);
		f.clear();
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
