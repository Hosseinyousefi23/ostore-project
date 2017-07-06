package executer;

import facade.Ostore;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class WriteChannelNode extends Node {

	private ExprNode channelId;
	private ExprNode writeValue;

	public WriteChannelNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		channelId = (ExprNode) children.get(2);
		writeValue = (ExprNode) children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		channelId.execute(t);
		int id = (Integer) channelId.getResult();
		writeValue.execute(t);
		int value = (Integer) writeValue.getResult();
		Ostore os = t.getProcess().getScheduler().getOs();
		os.getChannel(id).write(value);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
