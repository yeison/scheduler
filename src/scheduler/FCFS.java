package scheduler;

import java.util.LinkedList;
//import java.util.concurrent.LinkedBlockingQueue;

public class FCFS extends SchedulingAlgo{
	
	public FCFS(int numberOfProcesses){
		setLock(false);
		this.readyQ = new LinkedList<Process>();
		this.processQ = new LinkedList<Process>();
		this.numberOfProcesses = numberOfProcesses;
	}
	
}