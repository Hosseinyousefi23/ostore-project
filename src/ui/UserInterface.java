package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicTreeUI.TreeHomeAction;

import executer.Executer;
import scheduler.Scheduler;
import scheduler.Process;

public class UserInterface {

	public static boolean quited = false;
	private static int cores = 4;
	private static int sched = 100;

	public static void main(String[] args) {
		extractAndApplyOptions(args);
		Scheduler scheduler = new Scheduler(cores, sched);
		while (true) {
			System.out.println("Enter the file path to run or just type 'cmd' without quotes for interactive mode");
			Scanner sc = new Scanner(System.in);
			String command = sc.next();
			if (command.equals("cmd")) {
				quited = false;
				openInteractiveMode(scheduler);
			} else {
				executeFile(command, scheduler);
			}
		}

	}

	private static void extractAndApplyOptions(String[] args) {
		if (isOdd(args.length)) {
			throwInvalidInput();
		}
		for (int i = 0; i < args.length; i += 2) {
			String option = args[i];
			int value = Integer.parseInt(args[i + 1]);
			switch (option) {
			case "-cpu":
				setCores(value);
				break;
			case "-sched":
				setSched(value);
				break;
			default:
				throwInvalidInput();
			}
		}

	}

	private static boolean isOdd(int num) {
		return num % 2 == 1;
	}

	private static void setCores(int value) {
		cores = value;
	}

	private static void setSched(int value) {
		sched = value;
	}

	private static void throwInvalidInput() {
		throw new RuntimeException("Invalid input! Please try again");

	}

	private static void executeFile(String filePath, Scheduler scheduler) {
		try {
			Scanner scan = new Scanner(new File(filePath));
			String code = scan.useDelimiter("\\Z").next();
			scheduler.start(code);
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void openInteractiveMode(Scheduler scheduler) {
		System.out.println(">> OStore version 0.1 started");
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.print(">> ");
			String code = scan.nextLine();
			scheduler.getExe().execute(code, scheduler.getCmdThread());
			if (quited) {
				break;
			}
		}
		scan.close();
	}
}
