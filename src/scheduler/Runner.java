package scheduler;
import static scheduler.AlgoTypes.valueOf;

import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/** @author Yeison Rodriguez */
public class Runner {
	//Arbitrary number of characters allowed in an input file.
	static final int MAX_FILE_CHARS = 100;
	public static boolean verbose = false;
	static int processOrder = 0;
    
	private static RandomFileReader randomReader;
		

	public static void main(String[] args) throws Error, Exception {
		randomReader = new RandomFileReader();
		
		PriorityQueue<Process> processQueue = new PriorityQueue<Process>();
		FileReader fileReader = null;
		int numberOfProcesses = 0;
		char inputBuffer[] = new char[MAX_FILE_CHARS];
		String delimeter = " \t\n\r\f()";
		//inputBuffer.allocate(MAX_FILE_CHARS);
		
		/* Remember to make the first argument the argument for the algorithm to
		 * use. 
		 */
		if(args.length != 2 && args.length != 3){
			System.out.println("  Usage: scheduler [--verbose] <input file> <scheduling algorithm>" +
					"\n\tPlease provide the name of an input file and the scheduling " +
					"algorithm as an argument.  The --verbose flag is for verbose output." +
					"\n\n\tThe algorithm may be one of the following:" +
					"\n\t  FCFS" +
					"\n\t  RR" +
					"\n\t  PSJF" +
					"\n\t  HPRN");
			System.exit(0);
		}
		
		try {
			fileReader = new FileReader(args[args.length - 2]);
			fileReader.read(inputBuffer);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error: The file name supplied was not found.");
			System.exit(1);
		}

		try{
			verbose = args[0].equals("--verbose");
		} catch (IndexOutOfBoundsException e){
			// Don't do anything
		}

		
		//st = new StreamTokenizer(fileReader);
		String input = String.valueOf(inputBuffer);
		StringTokenizer st = new StringTokenizer(input, delimeter);
		
		//Properly formatted input starts with the number of processes.
		numberOfProcesses = Integer.valueOf(st.nextToken());
		
		System.out.println(args[args.length - 2]);
		System.out.println("Scheduling Algorithm is: " + valueOf(args[args.length - 1]));
		System.out.println("Number of Processes: " + numberOfProcesses);
	
		/* Make processes from the input and place them into priority queues.  
		 * The processes are sorted based on their delays.  A process with a 
		 * high delay will be at the end of the queue.*/ 
		for(int i = 0; i < numberOfProcesses; i++){
			Process newProcess = makeProcess(st);
			processQueue.offer(newProcess);
		}		

		/** Here we retrieve the Algo Type based on the input **/
		SchedulingAlgo algo = valueOf(args[args.length - 1]).getSchedulingAlgo(numberOfProcesses);
		Process[] pArray = new Process[numberOfProcesses];
		for(int i = 0; i < pArray.length; i++){
			pArray[i] = processQueue.poll();
			algo.offer(pArray[i]);
		}
		
		for(Process p : pArray){
			System.out.println(p);
		}
		
		System.out.println("pRatio may be ignored for non-HPRN algos\n");		
		
		algo.capturePrintQueue();
		while(!algo.isFinished())
			algo.runCycle();
		
		System.out.println();
		/* Extract and reformat the contents of the processes for printing. 
		 * Print each process in priority order. */
		for(Process p : pArray){
			System.out.println("Process " + p.getInstance() + ":\n" + 
					"\t(A, B, C, IO) = " + "(" + p.getArrivalTime() + ", " + 
					p.getBurstNumber() + ", " + p.getTotalCPUNeeded() + 
					", " + p.getIONumber() + ")" + 
					"\n\tFinishing Time: " + p.finishingTime + 
					"\n\tTurnaround Time: " + (p.finishingTime - p.getArrivalTime()) +
					"\n\tI/O Time: " + p.IOTime +
					"\n\tWaiting Time: " + p.waitingTime);
		}
		
		System.out.println("\nFinishing Time: " + algo.getLastCycle());
		System.out.println("CPU Utilization: " + algo.getCPUUtilization());
		System.out.println("I/O Utilization: " + algo.getIOUtilization(pArray));
		System.out.println("Throughput : " + 100*algo.getProcessesPerCycle() + " processes per 100 cycles");
		System.out.println("Average turnaround time: " + algo.getAverageTurnaroundTime(pArray));
		System.out.println("Average waiting time: " + algo.getAverageWaitingTime(pArray));
	}
	
	/**The method below takes a StringTokenizer and reads the next four tokens 
	 * from that tokenizer.  The tokens will be interpreted as A B C and IO, 
	 * respectively.  Using this data, the method instantiates and returns a 
	 * Process object.
	 * 
	 * @param st - A string tokenizer whose next four tokens correspond to A B C and IO.
	 * @return A new process object created from the data in the string tokenizer.
	 */
	static Process makeProcess(StringTokenizer st){
		Process newProcess = new Process();
		int[] processData = new int[4];
		
		for(int i = 0; i < 4; i++){
			String token = st.nextToken();			
			processData[i] = Integer.parseInt(token); 
			
			switch(i){
				case 0: newProcess.setArrivalTime(processData[0]); break;
				case 1:	newProcess.setBurstNumber(processData[1]); break;
				case 2:	newProcess.setTotalCPUNeeded(processData[2]); break;
				case 3:	newProcess.setIONumber(processData[3]); break;
			}
		}
		newProcess.setOrder(processOrder);
		processOrder++;
		return newProcess;
	}
	
	public static int randomOS(int U){
		String nextRandomNumber = null;
		
		try {
			nextRandomNumber = randomReader.getNextRandomNumber();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		int randomOS = U == 0 ? 1 : 1 + (Integer.valueOf(nextRandomNumber) % U);
		
		if(verbose){
			System.out.println("The next random number is: " + nextRandomNumber);
			System.out.println("The value of 1 + ("+ nextRandomNumber +" % "+ U 
				+ ") is: "+ randomOS);
		}

		return randomOS;
	}
}