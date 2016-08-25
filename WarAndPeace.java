package warAndPeace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WarAndPeace {
	
	public static void main(String[] args) {
		// Multiple threads:
		int numThreads=1;
		countCommas(numThreads);
		
		// One thread (main):
		int commas = 0;
		File book = new File("Voina_i_mir.txt");
		Scanner sc = null;
		try {
			sc = new Scanner(book);
		} catch (FileNotFoundException e) {
			System.out.println("No such file!");
			e.printStackTrace();
		}
		StringBuilder fullBook= new StringBuilder();
		while (sc.hasNextLine()) {
			fullBook.append(sc.nextLine());
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < fullBook.length(); i++) {
			if (fullBook.charAt(i)==',') {
				commas++;
			}
		}
		System.out.println("--------------- One thread counting ---------------");
		System.out.println("Time: "+(System.currentTimeMillis()-start));
		System.out.println("Commas: "+commas);
		System.out.println("---------------------------------------------");

	}

	public static void countCommas(int numThreads) {
		AtomicInteger commas=new AtomicInteger();
		commas.set(0);
		File book = new File("Voina_i_mir.txt");
		StringBuilder wholeBook= new StringBuilder();
		ArrayList<String> booklets = new ArrayList<String>();
		ArrayList<ReadingThread> threads = new ArrayList<ReadingThread>();
		Scanner sc = null;
		try {
			sc = new Scanner(book); // reads from the file
			while (sc.hasNextLine()) {
				wholeBook.append(sc.nextLine()); // adds all the text from the txt file to the Builder
			}
			int lengthOfSmallString = wholeBook.length()/numThreads; //divides the text into equal intervals
			
			for (int i = 0; i < numThreads; i++) { // fills the booklets collection
				int staringPoint = i*lengthOfSmallString;
				String booklet=wholeBook.substring(staringPoint, (staringPoint+lengthOfSmallString)); // adds the piece of text to the current small String
				booklets.add(booklet);
			}
			
			for (int i = 0; i < numThreads; i++) { // fills the threads collection
				threads.add(new ReadingThread(booklets.get(i), commas)); // creates the threads and assigns them booklets
			}
			
			// Start ----------------------------------------
			
			ExecutorService executor = Executors.newFixedThreadPool(numThreads); //Executor with N threads
			
			long start = System.currentTimeMillis(); // start time
			for(ReadingThread thread : threads){ // executes all the tasks
				executor.execute(thread);
			}
			executor.shutdown(); // shuts down the executor when all the tasks are finished
			while(!executor.isTerminated()){ // runs the tasks until completion
			}

			// Finish ----------------------------------------
			
			long duration=System.currentTimeMillis()-start; // stop time
			System.out.println("\n--------------- "+numThreads+"-thread counting ---------------");
			System.out.println("Time: "+duration);
			System.out.println("All commas: "+commas);
			System.out.println("---------------------------------------------");
		} 
		catch (FileNotFoundException e) {
			System.out.println("Error trying to read the file!");
			e.printStackTrace();
		}
		finally {
			sc.close();
		}
	}
}
