package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ReadOrgNode extends Node {

	private ExprNode fileNumber, indexInFile;
	private Node id1, id2;

	public ReadOrgNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileNumber = (ExprNode) children.get(2);
		indexInFile = (ExprNode) children.get(4);
		id1 = children.get(6);
		id2 = children.get(8);
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
		t.addLocalVar(id1.getContent(), data[0]);
		t.addLocalVar(id2.getContent(), data[1]);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
