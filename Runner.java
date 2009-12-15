package scheduler;
import java.util.PriorityQueue; 
import java.util.StringTokenizer;
import java.io.*;

/**
 * @author yeison
 *
 */
public class Runner {
	//Arbitrary number of characters allowed in an input file.
	static final int MAX_FILE_CHARS = 100;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PriorityQueue<Process> processQueue = new PriorityQueue<Process>();
		FileReader fileReader = null;
		int numberOfProcesses = 0;
		char inputBuffer[] = new char[MAX_FILE_CHARS];
		String delimeter = " \t\n\r\f()";
		//inputBuffer.allocate(MAX_FILE_CHARS);
		
		/* Remember to make the first argument the argument for the algorithm to use 
		 *
		 */
		if(args.length < 1){
			System.out.println("\tUsage: scheduler <input file> \n\tProvide the name of an input file as an argument");
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
	
		/*Make processes from the input and place them into priority queues.  The processes are
		 * sorted based on their delays.  A process with a high delay will be at the end of the queue.*/ 
		for(int i = 0; i < numberOfProcesses; i++){
			Process newProcess = makeProcess(st);
			processQueue.offer(newProcess);
		}

		FCFS algo = new FCFS();
		Process[] pArray = new Process[numberOfProcesses];
		for(int i = 0; i < pArray.length; i++){
			pArray[i] = processQueue.poll();
			algo.qProcess(pArray[i]);
		}
		
		algo.runCycle();
		algo.runCycle();
		algo.runCycle();
		algo.runCycle();
		
		
		System.out.println();
		for(int i = 0; i < pArray.length; i++){
			System.out.println("Process " + i + ":\n" + 
					"\t(A, B, C, IO) = " + "(" + pArray[i].arrivalTime + " " + pArray[i].burstNumber + " " + 
					pArray[i].totalCPUNeeded + " " + pArray[i].IONumber + ")");
		}
		
	
		
		// TODO Auto-generated method stub
	}
	
	/*The method below takes a StringTokenizer and reads the next four tokens from that tokenizer.  
	 * The tokens will be interpreted as A B C and IO, respectively.  Using this data, the method
	 * instantiates and returns a Process object.*/
	/**
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
		return newProcess;
	}
}
