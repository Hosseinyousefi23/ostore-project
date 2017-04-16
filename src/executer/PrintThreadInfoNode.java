package executer;

import scheduler.MyThread;

public class PrintThreadInfoNode extends Node{

	public PrintThreadInfoNode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void execute(MyThread t){
		System.out.println("thread_id: " + t.getID()+ ", pid; " );
		System.out.println("");
	}
}
