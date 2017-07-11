package filesystem;

import java.util.ArrayList;

import javax.crypto.CipherInputStream;

public class FileOrganizationModule {

	private BasicStorageManager basicStorageManager;

	private ArrayList<File> files;

	public FileOrganizationModule() {
		basicStorageManager = new BasicStorageManager();
		files = new ArrayList<File>();
	}

	public BasicStorageManager getBasicStorageManager() {
		return basicStorageManager;
	}

	public void createFile(int name) {
		int freeSpace = findFileFreeSpace();
		if (freeSpace == -1) {
			return;
		}
		File file = new File(basicStorageManager.getStorage(), freeSpace, name);
		files.add(file);
		Block b = new Block(basicStorageManager.getStorage(), freeSpace);
		b.write(name, -1);
	}

	private int findFileFreeSpace() {
		for (int i = 0; i < 10; i++) {
			Block b = new Block(basicStorageManager.getStorage(), i);
			int[] data = b.read();
			if (data[0] == -1) {
				return i;
			}
		}
		return -1;
	}

	public File findFileByName(int fileName) {
		for (File f : files) {
			if (f.getName() == fileName) {
				return f;
			}
		}
		return null;
	}

	public File findFileByNumber(int fileNumber) {
		for (File f : files) {
			if (f.getLogicalNumber() == fileNumber) {
				return f;
			}
		}
		return null;

	}

	public void appendToFile(File f, int data) {
		Block b = getFreeBlock();
		if (b == null)
			return;
		f.append(data, b);
	}

	private Block getFreeBlock() {
		Block freeMetaBlock = new Block(basicStorageManager.getStorage(), 10);
		int firstFreeBlock = freeMetaBlock.read()[0];
		setNextFreeBlock(firstFreeBlock);
		return new Block(basicStorageManager.getStorage(), firstFreeBlock);

	}

	private void setNextFreeBlock(int firstFreeBlock) {
		Block freeMetaBlock = new Block(basicStorageManager.getStorage(), 10);
		for (int i = 11; i < 50; i++) {
			if (i == firstFreeBlock)
				continue;
			Block b = new Block(basicStorageManager.getStorage(), i);
			if (b.isFree()) {
				freeMetaBlock.write(i, -1);
				return;
			}
		}
		freeMetaBlock.write(-1, -1);
	}

	public void deleteFile(File f) {
		f.delete();
		files.remove(f);
	}

	public void printFileMap() {
		for (File f : files) {
			f.print();
		}
	}
}
