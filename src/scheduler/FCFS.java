package scheduler;

import java.util.LinkedList;
//import java.util.concurrent.LinkedBlockingQueue;

public class FCFS extends SchedulingAlgo{
	
	public FCFS(int numberOfProcesses){
		unlocked = true;
		this.readyQ = new LinkedList<Process>();
		this.numberOfProcesses = numberOfProcesses;
	}
	
}