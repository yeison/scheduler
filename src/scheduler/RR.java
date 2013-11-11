package scheduler;

import java.util.LinkedList;
//import java.util.concurrent.LinkedBlockingQueue;

public class RR extends SchedulingAlgo{
	private int quantum;
	
	public RR(int numberOfProcesses, int quantum){
		setLock(false);
		this.quantum = quantum;
		this.readyQ = new LinkedList<Process>();
		this.tempQ = new LinkedList<Process>();
		this.numberOfProcesses = numberOfProcesses;
	}
	
	public void offer(Process newProcess){
		if(newProcess.getBurstNumber() > quantum)
			newProcess.setBurstNumber(quantum);
		processQ.offer(newProcess);
	}

}