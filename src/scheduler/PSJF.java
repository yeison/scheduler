package scheduler;

import java.util.PriorityQueue;

public class PSJF extends SchedulingAlgo {
	
	public PSJF(int numberOfProcesses){
		unlocked = true;
		this.numberOfProcesses = numberOfProcesses;
		this.readyQ = new PriorityQueue<Process>(11, new ShortestJob());
	}
	
    @Override
	public boolean checkReadyToRun(Process currentProcess){
		
		//Simple method checks if a process is running before running the new process.
		if(Process.runningProcess != null && currentProcess != Process.runningProcess){
			//Is this process at the head of the readyQ?
			if(currentProcess.equals(readyQ.peek())){
				
				Process.runningProcess.reduceBurst();
				Process.runningProcess.reduceCPU();
				if(Process.runningProcess.remainingBurst == 0){
					Process.runningProcess.setState(Process.BLOCKED);
					Process.runningProcess.setTampered();
				}
				else{
				    Process.runningProcess.setState(Process.READY);
				    if(!readyQ.contains(Process.runningProcess))
				        readyQ.offer(Process.runningProcess);
				}
				
				//If it is, then set this process to RUNNING, and leave it on the readyQ.
				currentProcess.setState(Process.RUNNING);
				
				//Lock the semaphore
				unlocked = false;
				//Run one cycle
				//currentProcess.reduceBurst();
				currentProcess.reduceCPU();
				//unlock the semaphore for the next cycle if this processes' burst is 1.
				if(currentProcess.remainingBurst == 1)
					unlockNextCycle = true;
			}
			
			processQ.offer(currentProcess);
			return true;
		} else
			processQ.offer(currentProcess);
		
		return false;
	}


}
