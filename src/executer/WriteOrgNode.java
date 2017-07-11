package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WriteOrgNode extends Node {

	private ExprNode fileNumber, indexInFile;
	private ExprNode data1, data2;

	public WriteOrgNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileNumber = (ExprNode) children.get(2);
		indexInFile = (ExprNode) children.get(4);
		data1 = (ExprNode) children.get(6);
		data2 = (ExprNode) children.get(8);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileNumber.execute(t);
		indexInFile.execute(t);
		data1.execute(t);
		data2.execute(t);
		int fileNumberRes = (Integer) fileNumber.getResult();
		int indexInFileRes = (Integer) indexInFile.getResult();
		int data1Res = (Integer) data1.getResult();
		int data2Res = (Integer) data2.getResult();
		File f = fileManager.findFileByNumber(fileNumberRes);
		f.writeIndex(indexInFileRes, data1Res, data2Res);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
