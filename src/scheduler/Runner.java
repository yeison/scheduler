package scheduler;

 
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.io.*;

/**
 * @author Yeison Rodriguez
 *
 */
public class Runner {
	//Arbitrary number of characters allowed in an input file.
	static final int MAX_FILE_CHARS = 100;
	
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		TreeSet<Process> processTreeSet = new TreeSet<Process>();
		FileReader fileReader = null;
		int numberOfProcesses = 0;
		char inputBuffer[] = new char[MAX_FILE_CHARS];
		String delimeter = " \t\n\r\f()";

		
		/* TODO: Remember to make the first argument the argument for the
		 *  algorithm to use. 
		 */
		if(args.length < 1){
			System.out.println("\tUsage: scheduler <input file> \n\tPlease " +
					"provide the name of an input file as an argument.");
			System.exit(0);
		}
		
		try {
			fileReader = new FileReader(args[0]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error: The file name supplied was not found.");
			System.exit(1);
		}
		
		try {
			fileReader.read(inputBuffer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//st = new StreamTokenizer(fileReader);
		String input = String.valueOf(inputBuffer);
		StringTokenizer st = new StringTokenizer(input, delimeter);
		
		//Properly formatted input starts with the number of processes.
		numberOfProcesses = Integer.valueOf(st.nextToken());
		System.out.println("Number of Processes: " + numberOfProcesses);
	
		/* Make processes from the input and place them into priority queues.  
		 * The processes are sorted based on their delays.  A process with a 
		 * high delay will be at the end of the queue.*/ 
		for(int i = 0; i < numberOfProcesses; i++){
			Process newProcess = Process.makeProcess(st);
			processTreeSet.add(newProcess);
		}
		
		Scheduler scheduler = new Scheduler(processTreeSet);			
		
		System.out.println(args[0]);
		
		scheduler.runCycle();	
		
		System.out.println();
		/* Extract and reformat the contents of the processes for printing. 
		 * Print each process in priority order.
		 */
		int i = 0;
		Process[] processArray = processTreeSet.toArray(new Process[0]);
		for(Process process : processArray ){
			System.out.println("Process " + i + ":\n" + 
					"\t(A, B, C, IO) = " + "(" + process.arrivalTime + ", " + 
					process.burstNumber + ", " + process.totalCPUNeeded + 
					", " + process.IONumber + ")" + 
					"\n\tFinishing Time: " + process.finishingTime + 
					"\n\tTurnaround Time: " + (process.finishingTime - process.arrivalTime) +
					"\n\tI/O Time: " + process.IOTime +
					"\n\tWaiting Time: " + process.waitingTime);
			i++;
		}
		
	
	}
	

}
