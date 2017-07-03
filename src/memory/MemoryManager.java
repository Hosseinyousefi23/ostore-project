package memory;

public abstract class MemoryManager {
	protected int pageSize;
	protected int frameSize;

	protected int[] frame;

	public MemoryManager(int pageSize, int frameSize) {
		this.pageSize = pageSize;
		this.frameSize = frameSize;
		frame = new int[frameSize];
	}

	public abstract void loadPage(int number);

	public String getFrame() {
		String ret = "[";
		for (int i = 0; i < frame.length; i++) {
			ret += frame[i];
			if (i != frame.length - 1)
				ret += ",";
		}
		ret += "]";
		return ret;
	}
}
