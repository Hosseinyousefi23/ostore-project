package testscanner;

import scanner.Scanner;

public class Test {
	public static void main(String[] args) {
		Scanner sc = new Scanner();
		sc.scan("create_process(pid)\nkill_process(pid)\ncreate_thread(tid)\nkill_thread(tid)"
				+ "\nexec(\"ali.ostore\")\nwait_for_process(pid)\nwait_for_thread(tid)\nprint_process_info()"
				+ "\nprint_thread_info()\nsemaphore(s,2)\nwait(s)\nsignal(s)");
		String s = "";
		while (!s.equals("EOF")) {
			s = sc.nextToken();
			System.out.println(s);
		}

	}
}
