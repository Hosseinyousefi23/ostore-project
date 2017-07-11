package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class LoadPageNode extends Node {

	private ExprNode page;

	public LoadPageNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		page = (ExprNode) children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		page.execute(t);
		int index = (int) page.getResult();
		t.getProcess().getScheduler().getOs().getMemManager().loadPage(index);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
