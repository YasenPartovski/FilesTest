package warAndPeace;

import java.util.concurrent.atomic.AtomicInteger;

public class ReadingThread implements Runnable {

	private String booklet;
	private int numberOfCommas=0;
	private AtomicInteger commas;

	public ReadingThread(String booklet, AtomicInteger commas) {
		this.booklet=booklet;
		this.commas=commas;
	}

	@Override
	public void run() {
		for (int i = 0; i < booklet.length(); i++) {
			if (booklet.charAt(i)==',') { // if the char is a comma
				numberOfCommas++;
			}
		}
		int allCommas=commas.get()+numberOfCommas;
		commas.set(allCommas);
	}
	
}
