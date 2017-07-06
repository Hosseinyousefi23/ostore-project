package unittest;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import facade.Ostore;
import scheduler.Scheduler;

public class TestExecuter {
	@Test
	public void testAssignment() {
		try {
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn;
			System.setOut(new PrintStream(pipeOut));
			pipeIn = new PipedInputStream(pipeOut);
			Scanner scan = new Scanner(pipeIn);
			String code = "set(a,2) echo(a)";
			Ostore os = new Ostore(1, 100, 10, 3, "FIFO");
			os.start(code);
			String output = scan.next();
			Assert.assertTrue(output.equals("2"));
			scan.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testChannel() {
		String code = "set(ali,45) create_channel(ali) write(ali,24) read(ali,x) echo(x)";
		try {
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn;
			System.setOut(new PrintStream(pipeOut));
			pipeIn = new PipedInputStream(pipeOut);
			Scanner scan = new Scanner(pipeIn);
			Ostore os = new Ostore(1, 100, 10, 3, "FIFO");
			os.start(code);
			String output = scan.next();
			Assert.assertTrue(output.equals("24"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeadLock() {
		Scheduler sc = new Scheduler(3, 100, null);
		sc.start("semaphore(s,0) set(t1,0) set(t2,0) create_thread(t1) create_thread(t2) wait(s)");
		Assert.assertTrue(sc.getAllProcesses().size() == 0);
	}

	@Test
	public void testCreateProcess() {
		String code = "set(ali,45) create_process(ali) echo(2)";
		try {
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn;
			System.setOut(new PrintStream(pipeOut));
			pipeIn = new PipedInputStream(pipeOut);
			Scanner scan = new Scanner(pipeIn);
			Ostore os = new Ostore(1, 100, 10, 3, "FIFO");
			os.start(code);
			String output1 = scan.next();
			Assert.assertTrue(output1.equals("2"));
			String output2 = scan.next();
			Assert.assertTrue(output2.equals("2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateThread() {
		String code = "set(ali,45) create_thread(ali) echo(2)";
		try {
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn;
			System.setOut(new PrintStream(pipeOut));
			pipeIn = new PipedInputStream(pipeOut);
			Scanner scan = new Scanner(pipeIn);
			Ostore os = new Ostore(1, 100, 10, 3, "FIFO");
			os.start(code);
			String output1 = scan.next();
			Assert.assertTrue(output1.equals("2"));
			String output2 = scan.next();
			Assert.assertTrue(output2.equals("2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEcho() {
		String code = "echo(2)";
		try {
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn;
			System.setOut(new PrintStream(pipeOut));
			pipeIn = new PipedInputStream(pipeOut);
			Scanner scan = new Scanner(pipeIn);
			Ostore os = new Ostore(1, 100, 10, 3, "FIFO");
			os.start(code);
			String output = scan.next();
			Assert.assertTrue(output.equals("2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExec() {
		String code = "echo(2) exec(\"in2.txt\")";
		try {
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn;
			System.setOut(new PrintStream(pipeOut));
			pipeIn = new PipedInputStream(pipeOut);
			Scanner scan = new Scanner(pipeIn);
			Ostore os = new Ostore(1, 100, 10, 3, "FIFO");
			os.start(code);
			String output1 = scan.next();
			Assert.assertTrue(output1.equals("2"));
			String output2 = scan.next();
			Assert.assertTrue(output2.equals("hi"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGset() {
		String code = "gset(x,50) set(tid,2) create_thread(tid) echo(x)";
		try {
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn;
			System.setOut(new PrintStream(pipeOut));
			pipeIn = new PipedInputStream(pipeOut);
			Scanner scan = new Scanner(pipeIn);
			Ostore os = new Ostore(1, 100, 10, 3, "FIFO");
			os.start(code);
			String output1 = scan.next();
			Assert.assertTrue(output1.equals("50"));
			String output2 = scan.next();
			Assert.assertTrue(output2.equals("50"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
