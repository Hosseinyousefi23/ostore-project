package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import executer.AppendNode;
import executer.AssignmentNode;
import executer.ClearNode;
import executer.CreateChannelNode;
import executer.CreateFileNode;
import executer.CreateProcessNode;
import executer.CreateThreadNode;
import executer.DeleteFileNode;
import executer.EchoNode;
import executer.ExecNode;
import executer.ExprNode;
import executer.ForNode;
import executer.GlobalAssignmentNode;
import executer.IfNode;
import executer.KillProcessNode;
import executer.KillThreadNode;
import executer.LoadPageNode;
import executer.OpenNode;
import executer.PrintFileMapNode;
import executer.PrintFreeMapNode;
import executer.PrintMapNode;
import executer.PrintPagesNode;
import executer.PrintProcessInfoNode;
import executer.PrintThreadInfoNode;
import executer.QuitNode;
import executer.ReadBasicNode;
import executer.ReadChannelNode;
import executer.ReadFileNode;
import executer.ReadOrgNode;
import executer.ReadPhysicalNode;
import executer.SemaphoreNode;
import executer.SetPriorityNode;
import executer.SignalNode;
import executer.SizeNode;
import executer.WaitForProcessNode;
import executer.WaitForThreadNode;
import executer.WaitNode;
import executer.WhileNode;
import executer.WriteBasicNode;
import executer.WriteChannelNode;
import executer.WriteFileNode;
import executer.WriteOrgNode;
import executer.WritePhysicalNode;
import scanner.Scanner;

public class Parser {

	private Scanner scanner;

	private String token;

	private ArrayList<String> followInprocess;

	private ParseTree tree;

	private HashMap<String, ArrayList<ArrayList<String>>> rules;

	public Parser() {
		scanner = new Scanner();
		followInprocess = new ArrayList<String>();
		rules = new HashMap<String, ArrayList<ArrayList<String>>>();
		tree = new ParseTree();
		extractRules();
	}

	public ParseTree parse(String code) {
		scanner.scan(code);
		token = scanner.nextToken();
		Node root = new Node("<program>", tree);
		buildTree("<program>", root);
		tree.setRoot(root);
		tree.init();
		return tree;
	}

	private boolean buildTree(String element, Node root) {
		if (token == "EOF") {
			return true;
		}
		boolean virgin = true;
		if (root.isNonTerminal()) {
			ArrayList<ArrayList<String>> elementRules = rules.get(element);
			outerloop: for (ArrayList<String> rule : elementRules) {
				if (isToEpsilon(rule)) {
					if (follow(element).contains(token)) {
						return true;
					}
				} else {
					for (String childElement : rule) {
						if (isFakeElement(childElement)) {
							String childElement1 = childElement.substring(1, childElement.length() - 1);
							if (buildTree(childElement1, root)) {
								// System.out.println(childElement1);
								virgin = false;
								continue;
							} else {
								if (!virgin) {
									throw new RuntimeException("UnexpectedToken at line  = \"" + token + "\"");
								}
								continue outerloop;
							}
						} else {
							Node node = createNode(childElement);
							if (buildTree(childElement, node)) {
								// System.out.println(childElement);
								virgin = false;
								buildRelation(root, node);
								continue;
							} else {
								if (!virgin) {
									throw new RuntimeException("UnexpectedToken at line " + " = \"" + token + "\"");
								}
								continue outerloop;
							}
						}
					}
					return true;
				}
			}
			return false;

		} else {
			if (token.contains(" ")) {
				String tokenValue = token.substring(0, token.indexOf(" "));
				String content = token.substring(token.indexOf(" ") + 1, token.length());
				if (tokenValue.equals(element)) {
					token = scanner.nextToken();
					root.setContent(content);
					return true;
				}
				return false;
			}
			if (token.equals(element)) {
				token = scanner.nextToken();
				return true;
			}
			return false;
		}

	}

	private Node createNode(String element) {
		switch (element) {
		case "<assignment>":
			return new AssignmentNode(element, tree);
		case "<echo>":
			return new EchoNode(element, tree);
		case "<expr>":
		case "<simple_expr>":
			return new ExprNode(element, tree);
		case "<quit>":
			return new QuitNode(element, tree);
		case "<statement>":
			switch (token) {
			case "IF":
				return new IfNode(element, tree);
			case "WHILE":
				return new WhileNode(element, tree);
			case "FOR":
				return new ForNode(element, tree);
			}
		case "<create_process>":
			return new CreateProcessNode(element, tree);
		case "<kill_process>":
			return new KillProcessNode(element, tree);
		case "<create_thread>":
			return new CreateThreadNode(element, tree);
		case "<kill_thread>":
			return new KillThreadNode(element, tree);
		case "<exec>":
			return new ExecNode(element, tree);
		case "<wait_for_process>":
			return new WaitForProcessNode(element, tree);
		case "<wait_for_thread>":
			return new WaitForThreadNode(element, tree);
		case "<print_process_info>":
			return new PrintProcessInfoNode(element, tree);
		case "<print_thread_info>":
			return new PrintThreadInfoNode(element, tree);
		case "<semaphore>":
			return new SemaphoreNode(element, tree);
		case "<wait>":
			return new WaitNode(element, tree);
		case "<signal>":
			return new SignalNode(element, tree);
		case "<global_assignment>":
			return new GlobalAssignmentNode(element, tree);
		case "<create_channel>":
			return new CreateChannelNode(element, tree);
		case "<read>":
			return new ReadChannelNode(element, tree);
		case "<write>":
			return new WriteChannelNode(element, tree);
		case "<set_priority>":
			return new SetPriorityNode(element, tree);
		case "<load_page>":
			return new LoadPageNode(element, tree);
		case "<print_pages>":
			return new PrintPagesNode(element, tree);
		case "<read_physical>":
			return new ReadPhysicalNode(element, tree);
		case "<write_physical>":
			return new WritePhysicalNode(element, tree);
		case "<read_basic>":
			return new ReadBasicNode(element, tree);
		case "<write_basic>":
			return new WriteBasicNode(element, tree);
		case "<read_org>":
			return new ReadOrgNode(element, tree);
		case "<write_org>":
			return new WriteOrgNode(element, tree);
		case "<create_file>":
			return new CreateFileNode(element, tree);
		case "<open>":
			return new OpenNode(element, tree);
		case "<read_file>":
			return new ReadFileNode(element, tree);
		case "<append>":
			return new AppendNode(element, tree);
		case "<write_file>":
			return new WriteFileNode(element, tree);
		case "<clear>":
			return new ClearNode(element, tree);
		case "<delete_file>":
			return new DeleteFileNode(element, tree);
		case "<size>":
			return new SizeNode(element, tree);
		case "<print_map>":
			return new PrintMapNode(element, tree);
		case "<print_file_map>":
			return new PrintFileMapNode(element, tree);
		case "<print_free_map>":
			return new PrintFreeMapNode(element, tree);
		default:
			return new Node(element, tree);

		}
	}

	private void buildRelation(Node root, Node node) {
		node.setParrent(root);
		root.addChild(node);

	}

	private boolean isFakeElement(String childElement) {
		return childElement.startsWith("<<") && childElement.endsWith(">>");
	}

	private ArrayList<String> follow(String element) {
		followInprocess.add(element);
		ArrayList<String> ret = new ArrayList<String>();
		for (String key : rules.keySet()) {
			ArrayList<ArrayList<String>> elementRules = rules.get(key);
			for (ArrayList<String> rule : elementRules) {
				for (int i = 0; i < rule.size(); i++) {
					if (rule.get(i).equals(element) || rule.get(i).equals("<" + element + ">")) {
						if (i == rule.size() - 1 && !followInprocess.contains(key)) {
							ret = union(ret, follow(key));
						} else if (!followInprocess.contains(key)) {
							ret = union(ret, first(rule.get(i + 1)));
							if (isEpsilon(rule.get(i + 1))) {
								ret = union(ret, follow(rule.get(i + 1)));
							}
						}
					}
				}
			}
		}
		followInprocess.remove(element);
		return ret;
	}

	private ArrayList<String> first(String element) {
		ArrayList<String> ret = new ArrayList<String>();
		if (!(element.startsWith("<") && element.endsWith(">"))) {
			ret.add(element);
			return ret;
		}
		if (element.startsWith("<<") && element.endsWith(">>"))
			element = element.substring(1, element.length() - 1);
		ArrayList<ArrayList<String>> elementRules = rules.get(element);
		for (ArrayList<String> rule : elementRules) {
			for (String childElement : rule) {
				ret = union(ret, first(childElement));
				if (!isEpsilon(childElement)) {
					break;
				}
			}
		}
		return ret;
	}

	private boolean isEpsilon(String element) {
		if (!(element.startsWith("<") && element.endsWith(">")))
			return false;
		if (element.startsWith("<<") && element.endsWith(">>"))
			element = element.substring(1, element.length() - 1);
		ArrayList<ArrayList<String>> elementRules = rules.get(element);
		for (ArrayList<String> rule : elementRules) {
			if (rule.size() == 1 && rule.get(0).equals("\\epsilon"))
				return true;
		}
		return false;
	}

	private ArrayList<String> union(ArrayList<String> array1, ArrayList<String> array2) {
		ArrayList<String> ret = new ArrayList<String>();
		for (String element : array1) {
			if (!ret.contains(element)) {
				ret.add(element);
			}
		}
		for (String element : array2) {
			if (!ret.contains(element)) {
				ret.add(element);
			}
		}
		return ret;
	}

	private boolean isToEpsilon(ArrayList<String> rule) {
		return rule.size() == 1 && rule.get(0).equals("\\epsilon");
	}

	private void extractRules() {
		try {
			java.util.Scanner read = new java.util.Scanner(new File("grammar.txt"));
			String grammar = read.useDelimiter("\\Z").next();
			String[] rulesString = grammar.split("\n");
			for (String s : rulesString) {
				addRule(s);
			}
			read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void addRule(String s) {
		String[] data = s.split("==>");
		String left = data[0].substring(0, data[0].length() - 1);
		String[] right = data[1].substring(1, data[1].length()).split(" ");
		ArrayList<String> array = new ArrayList<String>();
		for (String str : right) {
			if (str.endsWith("\r")) {
				str = str.substring(0, str.length() - 1);
			}
			array.add(str);
		}
		if (rules.containsKey(left)) {
			rules.get(left).add(array);
		} else {
			rules.put(left, new ArrayList<ArrayList<String>>());
			rules.get(left).add(array);
		}

	}
}
