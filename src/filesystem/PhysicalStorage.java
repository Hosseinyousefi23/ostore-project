package filesystem;

public class PhysicalStorage {

	private int[] storage;

	public PhysicalStorage() {
		storage = new int[100];
	}

	public int read(int index) {
		return storage[index];
	}

	public void write(int index, int value) {
		storage[index] = value;
	}
}
