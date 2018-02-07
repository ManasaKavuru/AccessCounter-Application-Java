// Runnable class

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


public class RequestHandler implements Runnable {
	private static AccessCounter accessCounter = new AccessCounter();
	volatile boolean done = false;
	private int count = 0;
	private ReentrantLock lock = new ReentrantLock();

	public Path select() {
		File curDir = new File(".");
		File[] filesList = curDir.listFiles();
		String fileType = ".html";
		List<java.nio.file.Path> results = new ArrayList<java.nio.file.Path>();

		for (File file1 : filesList) {
			if (file1.isFile() && file1.getName().toLowerCase().contains(fileType.toLowerCase())) {
				count++;

				// System.out.println(file1.getPath());
				results.add(file1.toPath());
			}
		}
		
		
		 System.out.println("No of files are:" + count);
		Random rand = new Random();
		int n = rand.nextInt(count) + 0; // Random selection of a file 
		System.out.println("Selected file is:" + results.get(n));

		System.out.println(results);
		return results.get(n);
	}

	public void setDone() {

		try {
			lock.lock();
			done = true;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void run() {
		if (done == true) {
			return;
		}
		// Creating Files
		
		File file1 = new File("a.html");
		File file2 = new File("b.html");
		File file3 = new File("c.html");
		File file4 = new File("d.html");
		try {
			file1.createNewFile();
			file2.createNewFile();
			file3.createNewFile();
			file4.createNewFile();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Path filePath;
		filePath = select(); // Random access of file
		int count;

		accessCounter.increment(filePath);
		count = accessCounter.getCount(filePath);
		System.out.println("Access Count  for selected file is:" + count);

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// 
	public static void main(String[] args) {
		accessCounter.setMap();
		// Multiple Threads creation
		for (int i = 0; i < 15; i++) {
			new Thread(new RequestHandler()).start();  
		}

	}
}