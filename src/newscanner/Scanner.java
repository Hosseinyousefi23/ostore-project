package newscanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Scanner {
	private String code;
	private int currentState;
	private int startState = 0;
	private String currentword = "";
	private int currentIndex = 0;

	private String[] stateValue = { "", "=", "<", ">", "(", ")", "{", "}", ",", "+", "-", "*", "/", "%", "|", "!", "&",
			"==", "<=", ">=", "ID", "ID", "ID", "ID", "ID", "ID", "ID", "ID", "ID", "ID", "ID", "CONCAT", "ID", "ID",
			"ECHO", "ID", "ID", "ELSE", "ID", "FOR", "IF", "ID", "SET", "ID", "ID", "ID", "WHILE", "ERROR", "ERROR",
			"STRING_LITERAL", "INT_LITERAL", "ID", "ID", "ID", "INDEX", ";", "^", "!=", "ID", "ID", "ID", "QUIT" };

	private String startStateTransitionCode = "=@1;<@2;>@3;(@4;)@5;{@6;"
			+ "}@7;,@8;+@9;-@10;*@11;/@12;%@13;|@14;!@15;&@16;c@20;e@21;f@22;i@23;s@24;w@25;word@26;\"@47;num@50";

	private String transitionsCode = "26@wordnum@26;1@=@17;2@=@18;3@=@19;20@o@27;27@n@28;"
			+ "28@c@29;29@a@30;30@t@31;21@c@32;32@h@33;33@o@34;21@l@35;"
			+ "35@s@36;36@e@37;22@o@38;38@r@39;23@f@40;24@e@41;41@t@42;"
			+ "25@h@43;43@i@44;44@l@45;45@e@46;47@all@48;48@all@48;48@\"@49;50@num@50;23@n@51;51@d@52;52@e@53;53@x@54";

	private ArrayList<HashMap<String, Integer>> transitions;

	public Scanner() {
		initTransitions();
		decodeTransitions();
	}

	private void initTransitions() {
		transitions = new ArrayList<HashMap<String, Integer>>();
		for (int i = 0; i < 62; i++) {
			transitions.add(new HashMap<String, Integer>());
		}
	}

	public void scan(String code) {
		currentState = 0;
		currentword = "";
		currentIndex = 0;
		this.code = code;
	}

	private void decodeTransitions() {
		String[] transes = startStateTransitionCode.split(";");
		for (String trans : transes) {
			String[] transData = trans.split("@");
			String withChar = transData[0];
			int toState = Integer.parseInt(transData[1]);
			putTransition(startState, withChar, toState);
		}

		String[] otherTranses = transitionsCode.split(";");
		for (String trans : otherTranses) {
			String[] transData = trans.split("@");
			int fromState = Integer.parseInt(transData[0]);
			String withChar = transData[1];
			int toState = Integer.parseInt(transData[2]);
			putTransition(fromState, withChar, toState);
		}
		// State 20 to 46 and 51 to 54 with wordnum to 26
		for (int i = 20; i <= 46; i++) {
			putTransition(i, "wordnum", 26);
		}
		for (int i = 51; i <= 54; i++) {
			putTransition(i, "wordnum", 26);
		}
		for (int i = 58; i <= 61; i++) {
			putTransition(i, "wordnum", 26);
		}

		putTransition(startState, ";", 55);
		putTransition(startState, "^", 56);
		putTransition(15, "=", 57);
		putTransition(startState, "q", 58);
		putTransition(58, "u", 59);
		putTransition(59, "i", 60);
		putTransition(60, "t", 61);
	}

	private void putTransition(int fromState, String withChar, int toState) {
		transitions.get(fromState).put(withChar, toState);
	}

	public String nextToken() {
		if (currentIndex >= code.length())
			return "EOF";
		while (currentIndex < code.length() && isWhiteSpace(code.charAt(currentIndex))) {
			currentIndex++;
		}
		while (currentIndex < code.length()) {
			char input = code.charAt(currentIndex);
			if (!tryTransition(currentState, input)) {
				break;
			}
		}
		return createToken();
	}

	private boolean isWhiteSpace(char input) {
		return input == '\n' || input == ' ' || input == '\t' || input == '\r';
	}

	private String createToken() {
		String token = stateValue[currentState];
		if (token == "ID" || token == "STRING_LITERAL" || token == "INT_LITERAL") {
			token = token + " " + currentword;
		}
		currentState = startState;
		currentword = "";
		return token;
	}

	private boolean tryTransition(int state, char input) {
		if (tryNormalTransition(state, input)) {
			return true;
		}
		if (trySpecialTransition(state, input)) {
			return true;
		}
		return false;
	}

	private boolean tryNormalTransition(int state, char input) {
		Integer toState = transitions.get(state).get(input + "");
		if (toState != null) {
			goToState(toState, input);
			return true;
		}
		return false;
	}

	private void goToState(Integer toState, char input) {
		currentState = toState;
		currentIndex++;
		currentword += input + "";
	}

	private boolean trySpecialTransition(int state, char input) {
		Iterator<String> iterator = transitions.get(state).keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			int toState = 0;
			switch (key) {
			case "all":
				if (isAll(input)) {
					toState = transitions.get(state).get(key);
				}
				break;
			case "word":
				if (isWord(input)) {
					toState = transitions.get(state).get(key);
				}
				break;
			case "wordnum":
				if (isWordNum(input)) {
					toState = transitions.get(state).get(key);
				}
				break;
			case "num":
				if (isNum(input)) {
					toState = transitions.get(state).get(key);
				}
				break;
			default:
				break;
			}
			if (toState != 0) {
				goToState(toState, input);
				return true;
			}
		}
		return false;

	}

	private boolean isNum(char input) {
		return Character.isDigit(input);
	}

	private boolean isWordNum(char input) {
		return Character.isAlphabetic(input) || Character.isDigit(input) || input == '_';
	}

	private boolean isWord(char input) {
		return Character.isAlphabetic(input);
	}

	private boolean isAll(char input) {
		return input != '\"';
	}

}
