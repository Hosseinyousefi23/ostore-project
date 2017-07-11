package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class OpenNode extends Node {

	private ExprNode fileName;
	private Node logicalNumberId;

	public OpenNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileName = (ExprNode) children.get(2);
		logicalNumberId = children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileName.execute(t);
		int fileNameRes = (Integer) fileName.getResult();
		File f = fileManager.findFileByName(fileNameRes);
		String varName = logicalNumberId.getContent();
		t.addLocalVar(varName, f.getLogicalNumber());
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
