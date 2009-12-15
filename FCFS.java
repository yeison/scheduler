package scheduler;
import java.util.PriorityQueue;

public class FCFS {
	PriorityQueue<Process> processQueue = new PriorityQueue<Process>();
	int cycle;
	
	FCFS(){
		this.cycle = 0;
	}
	
	public void qProcess(Process p){
		processQueue.offer(p);
	}
	
	public void runCycle(){
		Process currentProcess;
		String cycleLine = "";
		
		while((currentProcess = processQueue.poll()) != null){
			cycleLine += "\t" + currentProcess.getStateString() + " " + currentProcess.remainingBurst;
		}
		
		System.out.println("Before cycle " + this.cycle + ": " + cycleLine);
		
		
		this.cycle++;
	}
			
}
