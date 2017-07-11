package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class DeleteFileNode extends Node {

	private ExprNode fileName;

	public DeleteFileNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileName = (ExprNode) children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileName.execute(t);
		int fileNameRes = (Integer) fileName.getResult();
		File f = fileManager.findFileByName(fileNameRes);
		fileManager.deleteFile(f);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
