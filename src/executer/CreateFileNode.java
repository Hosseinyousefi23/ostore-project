package executer;

import filesystem.FileOrganizationModule;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class CreateFileNode extends Node {

	private ExprNode name;

	public CreateFileNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		name = (ExprNode) children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		FileOrganizationModule fileManager = t.getProcess().getScheduler().getOs().getFileManager();
		name.execute(t);
		int nameRes = (Integer) name.getResult();
		fileManager.createFile(nameRes);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
