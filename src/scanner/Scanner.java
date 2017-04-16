package scanner;

import java.util.ArrayList;

public class Scanner {
	private ArrayList<String> meaningful = new ArrayList<String>();
	java.util.Scanner sc;
	private String code;
	private ArrayList<String> token = new ArrayList<String>();
	private int counter = 0;

	private boolean devidable2(char c, char d) {
		if (d != 0) {
			String tmp = (c + "" + d);
			if (tmp.equals("==") || tmp.equals("!=") || tmp.equals("<=") || tmp.equals(">=")) {
				return true;
			}
		}
		return false;
	}

	public String getCode() {
		return code;
	}

	private boolean isOpenString(String word) {
		return (word.length() - word.replace("'", "").length() == 1)
				|| (word.length() - word.replace("\"", "").length() == 1);
	}

	private boolean isString(String word) {
		return (word.length() - word.replace("'", "").length() == 2)
				|| (word.length() - word.replace("\"", "").length() == 2);
	}

	private boolean isNumber(String word) {
		if (word.length() == 0)
			return false;
		for (int i = 0; i < word.length(); i++) {
			if ((word.charAt(i) <= 47 || word.charAt(i) >= 58)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean devidable(char c) {
		return ((c == ';') || (c == ' ') || (c == '\t') || (c == '\n') || (c == ',') || (c == '(') || (c == ')') || (c == '+')
				|| (c == '%') ||(c == '^') ||(c == '*') ||(c == '/') || (c == '-') || (c == '|') || (c == '&') ||(c == '!') || (c == '%') || (c == '!') || (c == '{') || (c == '}')
				|| (c == '<') || (c == '>'));
	}
	
	private void addToken(String meanedWord) {
		if (!(meanedWord.equals("\t") || meanedWord.equals(" ") || meanedWord.equals(""))) {
			token.add(meanedWord);
		}
	}
	
	public String nextToken() {
		String s = "";
		if (counter >= token.size()) {
			s = "EOF";
		} else {
			s = token.get(counter++);
		}
	
		return s;
	}

	public void scan(String code) {
		token = new ArrayList<String>();
		counter = 0;
		// code = code.toUpperCase();
		
		this.code = code;
//		code = code.replace(";", " ");
		initMeaningful();
		String word = "";
		String meanedWord = "";
		sc = new java.util.Scanner(code);
		while (sc.hasNext()) {
			String s = sc.nextLine();
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				char d = 0;
				if (i < s.length() - 1) {
					d = s.charAt(i + 1);
				}
				if (!devidable(c)&&!devidable2(c, d)) {
					word += s.charAt(i);
					if (isMeaningful(word)) {
						meanedWord = getMeaning(word);
					} else if (i == s.length() - 1) {
						addToken(meanedWord);
						word = "";
						meanedWord = "";
					}
					
				} else if (devidable2(c, d)) {
					addToken(c + "" + d);
					i++;
				} else {
					if (isNumber(word)) {
						addToken("INT_LITERAL " + word);
//						setMeaningful(word);
					} else if (isString(word)) {
						addToken("STRING_LITERAL " + word);
//						setMeaningful(word);
					} else {
						if (isMeaningful(word)) {
							addToken(meanedWord);
						} else if (!isOpenString(word)) {
							addIdToken("ID " + word);
//							setMeaningful(word);
						}
					}
					if (isOpenString(word)) {
						for (; i < s.length(); i++) {
							word += s.charAt(i);
							if (s.charAt(i) == '\"') {
								break;
							}
						}
					} else {
						addToken(c + "");
//						setMeaningful("" + c);
						word = "";
						meanedWord = "";

					}
					// else if(!isNothing(word)){
					// word = "";
					// meanedWord = "";
					// }

				}
			}
		}
	}

	private void addIdToken(String meanedWord) {
		if (!(meanedWord.equals("ID \n") || meanedWord.equals("ID \t") || meanedWord.equals(" ")
				|| meanedWord.equals("ID ") || meanedWord.equals("ID") || meanedWord.equals(""))) {
			token.add(meanedWord);
		}
	}

//	private void setMeaningful(String s) {
//		if (!(s.equals("") || s.equals(" ")))
//			meaningful.add(s);
//	}

	private void initMeaningful() {
		meaningful.add("SET");
		meaningful.add("ECHO");
		meaningful.add("CONCAT");
		meaningful.add("IF");
		meaningful.add("WHILE");
		meaningful.add("FOR");
		meaningful.add("INDEX");
		meaningful.add("ELSE");
		meaningful.add("CREATE_PROCESS");
		meaningful.add("KILL_PROCESS");
		meaningful.add("CREATE_THREAD");
		meaningful.add("KILL_THREAD");
		meaningful.add("EXEC");
		meaningful.add("WAIT_FOR_PROCESS");
		meaningful.add("WAIT_FOR_THREAD");
		meaningful.add("PRINT_PROCESS_INFO");
		meaningful.add("PRINT_THREAD_INFO");
		meaningful.add("SEMAPHORE");
		meaningful.add("WAIT");
		meaningful.add("SIGNAL");
	}

	private boolean isMeaningful(String word) {
		for (int i = 0; i < meaningful.size(); i++) {
			if (meaningful.get(i).equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

	private String getMeaning(String word) {
		for (int i = 0; i < meaningful.size(); i++) {
			if (meaningful.get(i).equalsIgnoreCase(word)) {
				return meaningful.get(i);
			}
		}
		return null;
	}
}
