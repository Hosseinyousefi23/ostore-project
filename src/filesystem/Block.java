package filesystem;

public class Block {
	private PhysicalStorage storage;
	private int index;

	public Block(PhysicalStorage storage, int index) {
		this.storage = storage;
		this.index = index;
	}

	public void write(int... data) {
		storage.write(2 * index, data[0]);
		storage.write(2 * index + 1, data[1]);
	}

	public int[] read() {
		int[] data = new int[2];
		data[0] = storage.read(2 * index);
		data[1] = storage.read(2 * index + 1);
		return data;
	}

	public boolean isFree() {
		return read()[0] == -1;
	}

	public int getIndex() {
		return index;
	}

}
