package facade;

import java.util.ArrayList;

public class Channel {
	private ArrayList<Integer> buffer;

	public Channel() {
		buffer = new ArrayList<Integer>();
	}

	public void write(int num) {
		buffer.add(num);
	}

	public int read() {
		if (isEmpty()) {
			return -1;
		}
		return buffer.remove(buffer.size() - 1);
	}

	private boolean isEmpty() {
		return buffer.size() == 0;
	}
}
