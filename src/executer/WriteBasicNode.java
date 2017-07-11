package executer;

import filesystem.Block;
import filesystem.PhysicalStorage;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WriteBasicNode extends Node {

	private ExprNode index;
	private ExprNode data1, data2;

	public WriteBasicNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		index = (ExprNode) children.get(2);
		data1 = (ExprNode) children.get(4);
		data2 = (ExprNode) children.get(6);
	}

	@Override
	public void execute(MyThread t) {
		PhysicalStorage storage = t.getProcess().getScheduler().getOs().getFileManager().getBasicStorageManager()
				.getStorage();
		index.execute(t);
		data1.execute(t);
		data2.execute(t);
		int indexRes = (Integer) index.getResult();
		int data1Res = (Integer) data1.getResult();
		int data2Res = (Integer) data2.getResult();
		Block b = new Block(storage, indexRes);
		b.write(data1Res, data2Res);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
