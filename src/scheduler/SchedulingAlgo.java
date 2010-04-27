package scheduler;

import java.util.LinkedList;

public class SchedulingAlgo {
	/**@param processQ - Contains the processes in the order of their delays, and the order they were encountered.
	 * @param cycle - The current cycle of this set of process executions.**/
	LinkedList<Process> processQ = new LinkedList<Process>();
	LinkedList<Process> readyQ = new LinkedList<Process>();
	LinkedList<Process> tempQ;
	protected int cycle;
	//A semaphore for the processor.
	protected boolean unlocked;
	protected boolean unlockNextCycle;
	int numberTerminated = 0;
	int numberOfProcesses;
	
	
	public void printCycle(){
		Process currentProcess;
		String cycleLine = "";
		LinkedList<Process> printQ = new LinkedList<Process>(processQ);
 
		//Print the print queue.
		while((currentProcess = printQ.poll()) != null){
			cycleLine += " \t" + currentProcess.getStateString() + " " 
			+ currentProcess.remainingBurst;
		}
		//Restore the print queue from its copy.
 
		System.out.println("Before cycle " + cycle + ": " + cycleLine);
	}
	
	//Indicates that all processes have finished running.
	public boolean isFinished(){
		if(numberTerminated == numberOfProcesses)
			return true;
		else
			return false;
	}
}
