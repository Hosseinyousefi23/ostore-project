package executer;

import filesystem.File;
import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class SizeNode extends Node {

	private ExprNode fileNumber;
	private Node id;

	public SizeNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		fileNumber = (ExprNode) children.get(2);
		id = children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		fileNumber.execute(t);
		int fileNumberRes = (Integer) fileNumber.getResult();
		File f = fileManager.findFileByNumber(fileNumberRes);
		int size = f.getSize();
		t.addLocalVar(id.getContent(), size);

	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
