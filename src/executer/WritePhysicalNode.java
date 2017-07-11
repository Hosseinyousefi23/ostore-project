package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WritePhysicalNode extends Node {

	private ExprNode index;
	private ExprNode data;

	public WritePhysicalNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		index = (ExprNode) children.get(2);
		data = (ExprNode) children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		data.execute(t);
		index.execute(t);
		int dataRes = (Integer) data.getResult();
		int indexRes = (Integer) index.getResult();
		t.getProcess().getScheduler().getOs().getFileManager().getBasicStorageManager().getStorage().write(indexRes,
				dataRes);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
