package memory;

import java.util.ArrayList;

public class MFUMemoryManager extends MemoryManager {
	private ArrayList<int[]> arr;

	public MFUMemoryManager(int pageSize, int frameSize) {
		super(pageSize, frameSize);
		arr = new ArrayList<int[]>();
	}

	@Override
	public void loadPage(int number) {
		int n = idxInArray(number);
		if (n != -1) {
			int[] oldVal = arr.get(n);
			int[] newVal = new int[] { oldVal[0], oldVal[1] + 1 };
			arr.set(n, newVal);

		} else {
			arr.add(new int[] { number, 1 });
		}
		if (inFrame(number)) {
			return;
		}
		if (isFull()) {
			int max = Integer.MAX_VALUE;
			int minIdx = 0;
			for (int i = 0; i < frame.length; i++) {
				int k = idxInArray(frame[i]);
				if (arr.get(k)[1] > max) {
					max = arr.get(k)[1];
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
