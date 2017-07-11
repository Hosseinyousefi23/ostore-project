package memory;

public class FIFOMemoryManager extends MemoryManager {
	private int currentPos = 0;

	public FIFOMemoryManager(int pageSize, int frameSize) {
		super(pageSize, frameSize);
	}

	@Override
	public void loadPage(int number) {
		if (inFrame(number)) {
			return;
		}
		frame[(++currentPos) % frameSize] = number;
	}

	private boolean inFrame(int number) {
		for (int i = 0; i < frameSize; i++) {
			if (number == frame[i])
				return true;
		}
		return false;
	}

}
