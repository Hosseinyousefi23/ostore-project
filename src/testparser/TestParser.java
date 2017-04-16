package testparser;

import executer.Node;
import parser.Parser;

public class TestParser {
	public static void main(String[] args) {
		Parser p = new Parser();
		Node root = p.parse("create_process(pid)\nkill_process(pid)\ncreate_thread(tid)\nkill_thread(tid)"
				+ "\nexec(\"ali.ostore\")\nwait_for_process(pid)\nwait_for_thread(tid)\nprint_process_info()"
				+ "\nprint_thread_info()\nsemaphore(s,2)\nwait(s)\nsignal(s)");
		System.out.println("");
	}
}
