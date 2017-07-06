package scheduler;

public class Variable {
	private Object value;
	private boolean isPrivate; // private vars wont copy by thread creation

	public Variable(Object value) {
		this.value = value;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate() {
		this.isPrivate = true;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
