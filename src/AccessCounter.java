
// THREAD SAFE ACCESS COUNTER

////
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AccessCounter {
	private HashMap<java.nio.file.Path, Integer> counter = new HashMap<java.nio.file.Path, Integer>();
	private ReentrantLock lock = new ReentrantLock();
	private Path path;
	private RequestHandler requestHandler = new RequestHandler();

	public void setMap() {
		
		counter.remove(null);
		// Storing the file path as Key and initial access count as 0
		counter.put(Paths.get("a.html"), 0);
		counter.put(Paths.get("b.html"), 0);
		counter.put(Paths.get("c.html"), 0);
		counter.put(Paths.get("d.html"), 0);
	}

	// To accept a file path and increment the access count
	public void increment(java.nio.file.Path filePath) {
		Path k = filePath.getFileName();
		lock.lock();
		System.out.println("Lock is Obtained");

		try {
			if (counter.keySet().contains(k)) {

				for (Path key : counter.keySet()) {
					if (key.equals(k)) {
						Integer count = counter.get(key);
						count++;
						counter.put(key, count);
					}
				}
			} else {
				counter.put(path, 1);
			}
		} finally {
			lock.unlock();
			System.out.println("Lock is released");
		}
		requestHandler.setDone();
		System.out.println("Counter HashMap:" + counter);
	}
	// Accept a file path and returns the access count.

	public int getCount(java.nio.file.Path filePath) {
		Path k = filePath.getFileName();
		lock.lock();
		System.out.println("Lock is Obtained");
		try {
			if (counter.keySet().contains(k)) {

				Integer count = counter.get(k);
				return count;
			} else {
				return 0;
			}

		} finally {
			lock.unlock();
			System.out.println("Lock is released");
		}
	}
}