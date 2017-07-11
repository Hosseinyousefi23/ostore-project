package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class CreateChannelNode extends Node {

	private ExprNode channelId;

	public CreateChannelNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		channelId = (ExprNode) children.get(2);
	}

	@Override
	public void execute(MyThread t) {
		channelId.execute(t);
		int result = (Integer) channelId.getResult();
		if (t.getProcess().getScheduler().getOs() == null) {
			System.out.println();
		}
		t.getProcess().getScheduler().getOs().addChannel(result);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}
}
