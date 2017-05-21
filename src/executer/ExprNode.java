package executer;

import parser.Node;
import parser.ParseTree;
import scheduler.MyThread;

public class ExprNode extends Node {
	private Object result;

	public ExprNode(String name, ParseTree tree) {
		super(name, tree);
	}

	@Override
	public void execute(MyThread t) {
		Node firstChild = children.get(0);
		switch (firstChild.getName()) {

		case "<simple_expr>":
			if (children.size() == 1) {
				ExprNode simpleExp = (ExprNode) children.get(0);
				simpleExp.execute(t);
				result = simpleExp.getResult();
			} else {
				ExprNode First = (ExprNode) children.get(0);
				ExprNode Second = (ExprNode) children.get(2);
				Node operation = children.get(1).getChildren().get(0);

				First.execute(t);
				Second.execute(t);
				Object firstResult = First.getResult();
				Object secondResult = Second.getResult();

				switch (operation.getName()) {

				case "+":
					result = (int) firstResult + (int) secondResult;
					break;

				case "-":
					result = (int) firstResult - (int) secondResult;
					break;

				case "*":
					result = (int) firstResult * (int) secondResult;
					break;

				case "/":
					result = (int) firstResult / (int) secondResult;
					break;

				case "%":
					result = (int) firstResult % (int) secondResult;
					break;

				case "|":
					result = (int) firstResult + (int) secondResult - (int) firstResult * (int) secondResult;
					break;

				case "&":
					result = (int) firstResult * (int) secondResult;
					break;

				case "^":
					result = (int) Math.pow((int) firstResult, (int) secondResult);
					break;

				case "<":
					result = booleanToInt((int) firstResult < (int) secondResult);
					break;

				case ">":
					result = booleanToInt((int) firstResult > (int) secondResult);
					break;

				case "<=":
					result = booleanToInt((int) firstResult <= (int) secondResult);
					break;

				case ">=":
					result = booleanToInt((int) firstResult >= (int) secondResult);
					break;

				case "==":
					result = booleanToInt((int) firstResult == (int) secondResult);
					break;

				case "!=":
					result = booleanToInt((int) firstResult != (int) secondResult);
					break;
				}
			}
			break;

		case "!":
			ExprNode exp = (ExprNode) children.get(1);
			exp.execute(t);
			int resultexp = (int) exp.getResult();
			result = 1 - resultexp;

			break;

		case "-":
			ExprNode exp1 = (ExprNode) children.get(1);
			exp1.execute(t);
			int resultexp1 = (int) exp1.getResult();
			result = -resultexp1;
			break;

		case "(":

			ExprNode exp2 = (ExprNode) children.get(1);
			exp2.execute(t);
			Object resultexp2 = exp2.getResult();
			result = resultexp2;

			break;

		case "CONCAT":

			ExprNode firstConcat = (ExprNode) children.get(2);
			ExprNode secondConcat = (ExprNode) children.get(4);
			firstConcat.execute(t);
			secondConcat.execute(t);
			String firstresult = firstConcat.getResult().toString();
			String secondresult = secondConcat.getResult().toString();
			result = firstresult + secondresult;
			break;

		case "INDEX":
			ExprNode firstIndex = (ExprNode) children.get(2);
			ExprNode secondIndex = (ExprNode) children.get(4);
			ExprNode StringIndex = (ExprNode) children.get(6);
			firstIndex.execute(t);
			secondIndex.execute(t);
			StringIndex.execute(t);

			int firstindexResult = (int) firstIndex.getResult();
			int secondindexResult = (int) secondIndex.getResult();
			String StringIndexResult = (String) StringIndex.getResult();
			result = StringIndexResult.substring(firstindexResult, secondindexResult);

			break;

		case "ID":
			String varName = children.get(0).getContent();
			if (t.getLocalVar(varName) != null) {
				result = t.getLocalVar(varName);
			} else if (t.getProcess().getGlobalVar(varName) != null) {
				result = t.getProcess().getGlobalVar(varName);
			} else {
				throw new RuntimeException("variable \"" + varName + "\" is not defined");
			}
			break;

		case "<literal>":
			Object literal = children.get(0).getChildren().get(0).getContent();
			if (isString((String) literal)) {
				literal = removequotes((String) literal);
			} else {
				literal = Integer.parseInt((String) literal);
			}
			result = literal;
			break;

		}
	}

	private int booleanToInt(boolean b) {
		if (b)
			return 1;
		return 0;
	}

	private String removequotes(String literal) {
		return literal.substring(1, literal.length() - 1);
	}

	private boolean isString(String literal) {
		return literal.startsWith("\"") && literal.endsWith("\"");
	}

	public Object getResult() {
		return result;
	}

	@Override
	public void executeInstruction(MyThread t) {
		execute(t);
		done(t.getID());
	}

}
