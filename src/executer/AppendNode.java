package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class AppendNode extends Node {

	private ExprNode fileNumber, data;

	public AppendNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileNumber = (ExprNode) children.get(2);
		data = (ExprNode) children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileNumber.execute(t);
		data.execute(t);
		int fileNumberRes = (Integer) fileNumber.getResult();
		int dataRes = (Integer) data.getResult();
		File f = fileManager.findFileByNumber(fileNumberRes);
		fileManager.appendToFile(f, dataRes);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
