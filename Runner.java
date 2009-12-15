package scheduler;
import java.util.PriorityQueue; 
import java.util.StringTokenizer;
import java.io.*;
import java.nio.CharBuffer;

/**
 * @author yeison
 *
 */
public class Runner {
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
		
		
		numberOfProcesses = Integer.valueOf(st.nextToken());
		String c;
		c = st.nextToken();
		System.out.println("Number of Processes: " + numberOfProcesses);
		Process[] processArray = new Process[numberOfProcesses];
		
		for(int i = 0; i < numberOfProcesses; i++){
			c = st.nextToken();
			if(c.compareTo("(") == 0)
				processQueue.offer(makeProcess(st));
			else
				continue;
		}
		
		for(int i = 0; i < processArray.length; i++){
			processArray[i] = processQueue.poll();
			System.out.println("Process " + i + " arrival time: " + processArray[i].arrivalTime);
		}
		
		// TODO Auto-generated method stub
	}
	
	static Process makeProcess(StringTokenizer st){
		Process newProcess = new Process();
		int[] processArray = new int[4];
		
		for(int i = 0; i < 4; i++){
			String c = st.toString();
			st.nextToken();
			
			//Check the character c, if it is a blank space ignore it, if it is ')' we're finished with this process
			//return it, otherwise add it to the process array.  The array temporarily contains process information.
			switch(c.toCharArray()[0]){
				case ' ': continue;
				case ')': return newProcess;
				default: processArray[i] = Integer.parseInt(c); 
			}
			
			switch(i){
				case 0: newProcess.setArrivalTime(processArray[0]); break;
				case 1:	newProcess.setBurstNumber(processArray[1]); break;
				case 2:	newProcess.setTotalCPUNeeded(processArray[2]); break;
				case 3:	newProcess.setIONumber(processArray[3]); break;
			}
			
			i++;
		}
		return null;
	}
}
