package filesystem;

public class PhysicalStorage {

	private int[] storage;

	public PhysicalStorage() {
		storage = new int[100];
		for (int i = 0; i < storage.length; i++) {
			storage[i] = -1;
		}
		storage[20] = 11;
	}

	public int read(int index) {
		return storage[index];
	}

	public void write(int index, int value) {
		storage[index] = value;
	}

	public void printMap() {
		String s = "[";
		for (int i = 0; i < storage.length; i++) {
			if (i == storage.length - 1) {
				s += storage[i];
			} else {
				s += storage[i] + ", ";
			}
		}
		s += "]";
		System.out.println(s);
	}
}
