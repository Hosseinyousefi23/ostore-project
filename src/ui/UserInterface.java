package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.cert.PKIXRevocationChecker.Option;
import java.util.HashMap;
import java.util.Scanner;

import executer.AppendNode;
import executer.ClearNode;
import executer.CreateFileNode;
import executer.DeleteFileNode;
import executer.Executer;
import executer.OpenNode;
import executer.PrintFileMapNode;
import executer.PrintMapNode;
import executer.ReadBasicNode;
import executer.ReadFileNode;
import executer.ReadOrgNode;
import executer.ReadPhysicalNode;
import executer.SizeNode;
import executer.WriteBasicNode;
import executer.WriteFileNode;
import executer.WriteOrgNode;
import executer.WritePhysicalNode;
import facade.Ostore;
import parser.ParseTree;
import parser.Parser;

public class UserInterface {

	public static boolean quited = false;
	private static int cores = 4;
	private static int sched = 100;
	private static int pageSize = 10;
	private static int frameSize = 3;
	private static String loadAlgorithm = "FIFO";
	public static HashMap<String, Object> interVars = new HashMap<String, Object>();
	private static String filePath = "";

	public static void main(String[] args) {
		extractAndApplyOptions(args);
		
		while (true) {
			Ostore os = new Ostore(cores, sched, pageSize, frameSize, loadAlgorithm);
			String command;
			if (isFileGiven()) {
				command = filePath;
				System.out.println("file path is " + filePath);
			} else {
				System.out.println("Enter the file path to run or just type 'cmd' without quotes for interactive mode");
				Scanner sc = new Scanner(System.in);
				command = sc.next();
			}
			if (command.equals("cmd")) {
				quited = false;
				openInteractiveMode();
			} else {
				executeFile(command, os);
				filePath = "";
			}
		}

	}

	private static void extractAndApplyOptions(String[] args) {
		int i = 0;
		while (i < args.length) {
			if (args[i].startsWith("-")) {
				if (i + 1 < args.length) {
					applyoption(args[i], args[i + 1]);
					i += 2;
				} else {
					throwInvalidInput(args[i]);
				}
			} else {
				setFilePath(args[i]);
				i++;
			}
		}

	}

	private static void setFilePath(String string) {
		filePath = string;
	}

	private static boolean isFileGiven() {
		return !filePath.equals("");
	}

	private static void applyoption(String option, String value) {
		switch (option) {
		case "-cpu":
			setCores(Integer.parseInt(value));
			break;
		case "-sched":
			setSched(Integer.parseInt(value));
			break;
		case "-mem":
			String[] data = value.split(":");
			setPageSize(Integer.parseInt(data[0]));
			setFrameSize(Integer.parseInt(data[1]));
			setLoadAlgorithm(data[2]);
			break;

		default:
			throwInvalidInput(option);
		}

	}

	private static void setLoadAlgorithm(String algorithm) {
		loadAlgorithm = algorithm;
	}

	private static void setFrameSize(int size) {
		frameSize = size;
	}

	private static void setPageSize(int size) {
		pageSize = size;
	}

	private static void setCores(int value) {
		cores = value;
	}

	private static void setSched(int value) {
		sched = value;
	}

	private static void throwInvalidInput(String input) {
		throw new RuntimeException("Invalid input \"" + input + "\" ! Please try again");

	}

	private static void executeFile(String filePath, Ostore os) {
		try {
			Scanner scan = new Scanner(new File(filePath));
			String code = scan.useDelimiter("\\Z").next();
			scan.close();
			os.start(code);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void openInteractiveMode() {
		System.out.println(">> OStore version 0.1 started");
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.print(">> ");
			String code = scan.nextLine();
			Parser p = new Parser();
			ParseTree tree = p.parse(code);
			String commandName = tree.getRoot().getChildren().get(0).getName();
			if (notSupported(commandName)) {
				System.out.println("This command is not supported in interactive mode!");
				continue;
			}
			Executer exe = new Executer();
			exe.execute(code, null);
			if (quited) {
				break;
			}
		}
	}

	private static boolean notSupported(String commandName) {
		switch (commandName) {
		case "<create_channel>":
		case "<create_process>":
		case "<create_thread>":
		case "<exec>":
		case "<kill_process>":
		case "<kill_thread>":
		case "<print_process_info>":
		case "<print_thread_info>":
		case "<read>":
		case "<semaphore>":
		case "<set_priority>":
		case "<signal>":
		case "<statement>":
		case "<wait_for_process>":
		case "<wait_for_thread>":
		case "<wait>":
		case "<write>":
		case "<load_page>":
		case "<print_pages>":
		case "<read_physical>":
		case "<write_physical>":
		case "<read_basic>":
		case "<write_basic>":
		case "<read_org>":
		case "<write_org>":
		case "<create_file>":
		case "<open>":
		case "<read_file>":
		case "<append>":
		case "<write_file>":
		case "<clear>":
		case "<delete_file>":
		case "<size>":
		case "<print_map>":
		case "<print_file_map>":
		case "<print_free_map>":
			return true;
		default:
			return false;
		}
	}
}
