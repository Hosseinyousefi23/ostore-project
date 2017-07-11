package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ReadPhysicalNode extends Node {

	private ExprNode index;
	private Node saveTo;

	public ReadPhysicalNode(String name, ParseTree tree) {
		super(name, tree);

	}

	@Override
	public void init() {
		super.init();
		index = (ExprNode) children.get(2);
		saveTo = children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		index.execute(t);
		int indexRes = (Integer) index.getResult();
		int data = t.getProcess().getScheduler().getOs().getFileManager().getBasicStorageManager().getStorage()
				.read(indexRes);
		t.addLocalVar(saveTo.getContent(), data);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
