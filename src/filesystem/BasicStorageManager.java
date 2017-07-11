package filesystem;

public class BasicStorageManager {
	PhysicalStorage storage;

	public BasicStorageManager() {
		storage = new PhysicalStorage();
	}

	public PhysicalStorage getStorage() {
		return storage;
	}

	public void printFreeMap() {
		String s = "";
		int counter = 0;
		for (int i = 11; i < 50; i++) {
			Block b = new Block(storage, i);
			if (b.isFree()) {
				s += i + " ";
				counter++;
			}
		}
		if (counter != 0) {
			s = s.substring(0, s.length() - 1);
		}
		s = "[" + counter + "]: " + s;
		System.out.println(s);
	}
}
