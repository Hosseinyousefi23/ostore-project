package memory;

import java.util.ArrayList;

public class LRUMemoryManager extends MemoryManager {
	private ArrayList<int[]> arr;
	private int num;
	public LRUMemoryManager(int pageSize, int frameSize) {
		super(pageSize, frameSize);
		arr = new ArrayList<int[]>();
		num = 0;
	}

	@Override
	public void loadPage(int number) {
		int n = idxInArray(number);
		if (n != -1) {
			int[] oldVal = arr.get(n);
			int[] newVal = new int[] { oldVal[0], ++num };
			arr.set(n, newVal);

		} else {
			arr.add(new int[] { number, ++num });
		}
		if (inFrame(number)) {
			return;
		}
		if (isFull()) {
			int min = Integer.MAX_VALUE;
			int minIdx = 0;
			for (int i = 0; i < frame.length; i++) {
				int k = idxInArray(frame[i]);
				if (arr.get(k)[1] < min) {
					min = arr.get(k)[1];
					minIdx = i;
				}
			}
			frame[minIdx] = number;
		} else {
			for (int i = 0; i < frameSize; i++) {
				if (frame[i] == 0) {
					frame[i] = number;
					break;
				}
			}
		}
	}

	private boolean inFrame(int number) {
		for (int i = 0; i < frameSize; i++) {
			if (number == frame[i])
				return true;
		}
		return false;
	}

	private boolean isFull() {
		for (int i = 0; i < frame.length; i++) {
			if (frame[i] == 0) {
				return false;
			}
		}
		return true;
	}

	private int idxInArray(int number) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i)[0] == number) {
				return i;
			}
		}
		return -1;
	}

}
