package executer;

import filesystem.Block;
import filesystem.PhysicalStorage;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ReadBasicNode extends Node {

	private ExprNode index;
	private Node id1, id2;

	public ReadBasicNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		index = (ExprNode) children.get(2);
		id1 = children.get(4);
		id2 = children.get(6);
	}

	@Override
	public void execute(MyThread t) {
		index.execute(t);
		PhysicalStorage storage = t.getProcess().getScheduler().getOs().getFileManager().getBasicStorageManager()
				.getStorage();
		int indexRes = (Integer) index.getResult();
		Block b = new Block(storage, indexRes);
		int[] data = b.read();
		t.addLocalVar(id1.getContent(), data[0]);
		t.addLocalVar(id2.getContent(), data[1]);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
