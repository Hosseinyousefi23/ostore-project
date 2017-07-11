package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WriteFileNode extends Node {
	private ExprNode fileNumber, indexInFile;
	private ExprNode data;

	public WriteFileNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileNumber = (ExprNode) children.get(2);
		indexInFile = (ExprNode) children.get(4);
		data = (ExprNode) children.get(6);

	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileNumber.execute(t);
		indexInFile.execute(t);
		data.execute(t);
		int fileNumberRes = (Integer) fileNumber.getResult();
		int indexInFileRes = (Integer) indexInFile.getResult();
		int dataRes = (Integer) data.getResult();
		File f = fileManager.findFileByNumber(fileNumberRes);
		int[] data = f.readIndex(indexInFileRes);
		if (data[0] != -1 || data[1] != -1) {
			f.writeIndex(indexInFileRes, dataRes, data[1]);
		}
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
