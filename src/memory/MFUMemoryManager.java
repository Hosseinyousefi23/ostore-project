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
		// TODO load page
		int n = idxInArray(number);
		if (n != -1) {
			int[] oldVal = arr.get(n);
			int[] newVal = new int[] { oldVal[0], oldVal[1] + 1 };
			arr.set(n, newVal);

		} else {
			arr.add(new int[] { number, 1 });
		}
		int max = arr.get(idxInArray(frame[0]))[1];
		int minIdx = 0;
		for (int i = 1; i < frame.length; i++) {
			int k = idxInArray(frame[i]);
			if (arr.get(k)[1] > max) {
				max = arr.get(k)[1];
				minIdx = i;
			}
		}
		frame[minIdx] = number;
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
