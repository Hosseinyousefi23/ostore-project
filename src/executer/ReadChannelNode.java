package executer;

import facade.Ostore;
import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ReadChannelNode extends Node {
	private ExprNode channelId;
	private Node readVar;

	public ReadChannelNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void init() {
		super.init();
		channelId = (ExprNode) children.get(2);
		readVar = children.get(4);
	}

	@Override
	public void execute(MyThread t) {
		channelId.execute(t);
		int id = (Integer) channelId.getResult();
		String varName = readVar.getContent();
		Ostore os = t.getProcess().getScheduler().getOs();
		int output = os.getChannel(id).read();
		t.addLocalVar(varName, output);
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}