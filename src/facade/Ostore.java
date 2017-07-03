package facade;

import java.util.HashMap;

import memory.MemoryManager;
import memory.MemoryManagerFactory;
import scheduler.Scheduler;

public class Ostore {
	private Scheduler scheduler;
	private HashMap<Integer, Channel> channels;
	private MemoryManager memManager;

	public Ostore(int cores, int scheduleTime, int pageSize, int frameSize, String loadAlgorithm) {
		channels = new HashMap<Integer, Channel>();
		scheduler = new Scheduler(cores, scheduleTime, this);
		MemoryManagerFactory factory = new MemoryManagerFactory();
		memManager = factory.create(pageSize, frameSize, loadAlgorithm);
	}

	public void start(String code) {
		scheduler.start(code);
	}

	public void addChannel(int result) {
		channels.put(result, new Channel());
	}

	public Channel getChannel(int id) {
		return channels.get(id);
	}

	public MemoryManager getMemManager() {
		return memManager;
	}
}
