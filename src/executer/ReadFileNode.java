package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ReadFileNode extends Node {

	private ExprNode fileNumber, indexInFile;
	private Node dataId, eof;

	public ReadFileNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileNumber = (ExprNode) children.get(2);
		indexInFile = (ExprNode) children.get(4);
		dataId = children.get(6);
		eof = children.get(8);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileNumber.execute(t);
		indexInFile.execute(t);
		int fileNumberRes = (Integer) fileNumber.getResult();
		int indexInFileRes = (Integer) indexInFile.getResult();
		File f = fileManager.findFileByNumber(fileNumberRes);
		int[] data = f.readIndex(indexInFileRes);
		if (data[0] == -1 && data[1] == -1) {
			t.addLocalVar(dataId.getContent(), 0);
			t.addLocalVar(eof.getContent(), 1);
		} else {
			t.addLocalVar(dataId.getContent(), data[0]);
			t.addLocalVar(eof.getContent(), 0);
		}
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
