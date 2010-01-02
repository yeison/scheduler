package scheduler;
import java.util.LinkedList;
//import java.util.concurrent.LinkedBlockingQueue;

public class FCFS {
	/**@param processQ - Contains the processes in the order of their delays, and the order they were encountered.
	 * @param cycle - The current cycle of this set of process executions.**/
	LinkedList<Process> processQ = new LinkedList<Process>();
	private int cycle;
	//A semaphore for the processor.
	private boolean unlocked;
	
	public FCFS(){
		
	}
	
	public void offer(Process newProcess){
		processQ.push(newProcess);
	}
	
	public void runCycle(){
		Process currentProcess = processQ.pollLast();
		int state = currentProcess.state;
		
		switch(state){
			case Process.UNSTARTED: checkArrivalOf(currentProcess); break;
			case Process.READY: checkReadyToRun(currentProcess); break;
			case Process.RUNNING: checkRunningToBlock(currentProcess); break;
			case Process.BLOCKED: ;
			//case Process.TERMINATED: ;
		}
		
	}
	
	public void checkArrivalOf(Process currentProcess){
		//If the processes' time has arrived, set the process to ready.  If not, place back into the queue.
		if((currentProcess.arrivalTime - this.cycle) == 0){
			currentProcess.setState(Process.READY);
			processQ.offer(currentProcess);
		}
		else
			processQ.offer(currentProcess);
	}
	
	public void checkReadyToRun(Process currentProcess){
		//Simple method checks if a process is running before running the new process.
		if(unlocked){
			currentProcess.setState(Process.RUNNING);
			processQ.offer(currentProcess);
			//Lock the semaphore.
			unlocked = false;
		}
	}
	
	public void checkRunningToBlock(Process currentProcess){
		if(currentProcess.remainingBurst > 1){ // >1 or >0?  Come back and check this later
			currentProcess.reduceBurst();
			currentProcess.reduceCPU();
			processQ.offer(currentProcess);
		}
		else if(currentProcess.remainingCPU < 1){
			//No more CPU time needed.  Process finished.  Don't place back on the queue.
			currentProcess.setState(Process.TERMINATED);
			//Unlock the semaphore
			unlocked = true;
		}
		else{//Burst time has run out, block this process.
			currentProcess.setState(Process.BLOCKED);
			processQ.offer(currentProcess);
			//Unlock the semaphore
			unlocked = true;
		}
			
	}
	
	public void checkBlockedToReady(){
		
	}

}
