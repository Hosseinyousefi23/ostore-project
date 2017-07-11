package memory;

public class FIFOMemoryManager extends MemoryManager {
	private int currentPos = 0;

	public FIFOMemoryManager(int pageSize, int frameSize) {
		super(pageSize, frameSize);
	}

	@Override
	public void loadPage(int number) {
		frame[(++currentPos) % frameSize] = number;
	}

}
