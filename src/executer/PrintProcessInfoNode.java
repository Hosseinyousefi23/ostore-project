package executer;

import scheduler.MyThread;

public class PrintProcessInfoNode extends Node {

	public PrintProcessInfoNode(String name) {
		super(name);

	}

	@Override
	public void execute(MyThread thread) {
		System.out.println("pid: " + thread.getProcess().getID());
		System.out.println("threads: " + thread.getProcess().getThreadsSize());
	}

}
