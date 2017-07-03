package filesystem;

public class BasicStorageManager {
	PhysicalStorage storage;

	public BasicStorageManager() {
		storage = new PhysicalStorage();
	}

	public int[] readBlock(int index) {
		int[] block = new int[2];
		block[0] = storage.read(2 * index);
		block[1] = storage.read(2 * index + 1);
		return block;
	}

	public void writeBlock(int index, int[] value) {
		storage.write(2 * index, value[0]);
		storage.write(2 * index + 1, value[1]);
	}
}
