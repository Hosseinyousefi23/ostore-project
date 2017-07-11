package filesystem;

import java.util.ArrayList;
import java.util.Random;

public class File {
	private static ArrayList<Integer> logicalNumbers = new ArrayList<Integer>();
	private static Random r = new Random();
	private int logicalNumber;
	private PhysicalStorage storage;
	private int name;
	private int metaIndex;

	public File(PhysicalStorage storage, int metaIndex, int name) {
		logicalNumber = r.nextInt(100);
		while (logicalNumbers.contains(logicalNumber)) {
			logicalNumber = r.nextInt(100);
		}
		logicalNumbers.add(logicalNumber);

		this.metaIndex = metaIndex;
		this.name = name;
		this.storage = storage;
	}

	public int[] readIndex(int index) {
		if (isEmpty()) {
			int[] ret = new int[2];
			ret[0] = -1;
			ret[1] = -1;
			return ret;
		}
		int i = 0;
		Block nextBlock = findFirstBlock();
		while (i < index) {
			if (nextBlock.read()[1] == -1) {
				int[] ret = new int[2];
				ret[0] = -1;
				ret[1] = -1;
				return ret;
			}
			nextBlock = new Block(storage, nextBlock.read()[1]);
			i++;
		}
		return nextBlock.read();

	}

	private boolean isEmpty() {
		Block b = new Block(storage, metaIndex);
		return b.read()[1] == -1;
	}

	public void writeIndex(int index, int... data) {
		int i = 0;
		Block nextBlock = findFirstBlock();
		while (i < index) {
			if (nextBlock.read()[1] == -1) {
				return;
			}
			nextBlock = new Block(storage, nextBlock.read()[1]);
			i++;
		}
		nextBlock.write(data);
	}

	private Block findFirstBlock() {
		Block block = new Block(storage, metaIndex);
		if (block.read()[1] == -1) {
			return block;
		} else {
			return new Block(storage, block.read()[1]);
		}
	}

	public int getLogicalNumber() {
		return logicalNumber;
	}

	public int getName() {
		return name;
	}

	public void append(int data, Block b) {
		Block lastBlock = findLastBlock();
		b.write(data, -1);
		lastBlock.write(lastBlock.read()[0], b.getIndex());
	}

	private Block findLastBlock() {
		Block nextBlock = findFirstBlock();
		while (nextBlock.read()[1] != -1) {
			nextBlock = new Block(storage, nextBlock.read()[1]);
		}
		return nextBlock;
	}

	public void clear() {
		Block b = new Block(storage, metaIndex);
		if (b.read()[1] != -1) {
			Block nextBlock = new Block(storage, b.read()[1]);
			while (nextBlock.read()[1] != -1) {
				int nextIndex = nextBlock.read()[1];
				nextBlock.write(-1, -1);
				nextBlock = new Block(storage, nextIndex);
			}
			nextBlock.write(-1, -1);
			b.write(b.read()[0], -1);
		}
	}

	public void delete() {
		Block b = new Block(storage, metaIndex);
		if (b.read()[1] != -1) {
			Block nextBlock = new Block(storage, b.read()[1]);
			while (nextBlock.read()[1] != -1) {
				int nextIndex = nextBlock.read()[1];
				nextBlock.write(-1, -1);
				nextBlock = new Block(storage, nextIndex);
			}
			nextBlock.write(-1, -1);
			b.write(-1, -1);
		}

	}

	public int getSize() {
		if (isEmpty()) {
			return 0;
		}
		int counter = 0;
		Block nextBlock = findFirstBlock();
		while (nextBlock.read()[1] != -1) {
			counter++;
			nextBlock = new Block(storage, nextBlock.read()[1]);
		}
		counter++;
		return counter;

	}

	public void print() {
		String s = name + "[" + getSize() + "]: ";
		if (!isEmpty()) {
			Block nextBlock = findFirstBlock();
			while (nextBlock.read()[1] != -1) {
				s += nextBlock.getIndex() + " ";
				nextBlock = new Block(storage, nextBlock.read()[1]);
			}
			s += nextBlock.getIndex();
		}
		System.out.println(s);
	}
}
