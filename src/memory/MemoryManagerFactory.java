package memory;

public class MemoryManagerFactory {

	public MemoryManager create(int pageSize, int frameSize, String loadAlgorithm) {
		switch (loadAlgorithm) {
		case "FIFO":
			return new FIFOMemoryManager(pageSize, frameSize);
		case "LRU":
			return new LRUMemoryManager(pageSize, frameSize);
		case "LFU":
			return new LFUMemoryManager(pageSize, frameSize);
		case "MFU":
			return new MFOMemoryManager(pageSize, frameSize);
		default:
			throw new RuntimeException("loading algorithm \"" + loadAlgorithm + "\" is not known!");
		}
	}

}
