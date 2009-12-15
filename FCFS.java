package scheduler;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class FCFS {
	LinkedList<Process> processQ = new LinkedList<Process>();
	LinkedBlockingQueue<Process> blockingQ = new LinkedBlockingQueue<Process>();
	LinkedList<Process> printQ = new LinkedList<Process>();
	LinkedList<Process> printQBackup = new LinkedList<Process>();
	int cycle;
	
	FCFS(){
		this.cycle = 0;
	}
	
	public void qProcess(Process p){
		printQ.offer(p);
		processQ = new LinkedList<Process>(printQ);
	}
	
	public void runCycle(){
		Process currentProcess;
		String cycleLine = "";
		//Make a copy of the process queue.
		blockingQ = new LinkedBlockingQueue<Process>(processQ);
		//Make a copy of the print queue.
		printQBackup = new LinkedList<Process>(printQ);
		
		//Print the print queue.
		while((currentProcess = printQ.poll()) != null){
			cycleLine += " \t" + currentProcess.getStateString() + " " + currentProcess.remainingBurst;
		}
		//Restore the print queue from its copy.
		printQ = new LinkedList<Process>(printQBackup);
		
		
		System.out.println("Before cycle " + this.cycle + ": " + cycleLine);
		
		//If the processes' time has arrived, set the process to ready.
		while((currentProcess = blockingQ.poll()) != null){
			if((currentProcess.arrivalTime - this.cycle) == 0)
				currentProcess.setState(Process.READY);
		}
		
		
		currentProcess = processQ.poll();
		blockingQ = new LinkedBlockingQueue<Process>(processQ);
		if(currentProcess.state == Process.READY){
			currentProcess.setState(Process.RUNNING);
		}
		processQ.offer(currentProcess);
		
		
		while((currentProcess = blockingQ.poll()) != null)
			if(currentProcess.state == Process.RUNNING){
				if(currentProcess.remainingBurst > 1)
					currentProcess.reduceBurst();
				else{
					currentProcess.setState(Process.BLOCKED);
				}
			}
		
		this.cycle++;
	}
			
}
