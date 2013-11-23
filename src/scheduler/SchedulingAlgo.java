package scheduler;

import java.util.LinkedList;
import java.util.Queue;


abstract public class SchedulingAlgo {
	
	/**@param processQ - Contains the processes in the order of their delays, 
	 * and the order they were encountered.
	 * @param cycle - The current cycle of this set of process executions.**/
	protected Queue<Process> processQ = new LinkedList<Process>();
    // LinkedList and PriorityQueue both implement Queue
	Queue<Process> readyQ;
	LinkedList<Process> printQ;
	Queue<Process> tempQ;
	protected int cycle = 0;
	private int lastLockCycle = -1;
	//A semaphore for the processor.
	protected boolean unlocked;
	protected boolean unlockNextCycle;
	int numberTerminated = 0;
	int numberOfProcesses;
	private int cyclesRunning = 0;
	
	
	public void capturePrintQueue(){
		printQ = new LinkedList<Process>(processQ);
	}
	
	public void offer(Process newProcess){
		processQ.offer(newProcess);
	}
	
	protected void setLock(boolean lock){
		if(lastLockCycle == this.cycle)
			// Don't do anything, we locked this cycle
			return;
		if(!lock){
		    unlocked = true;
		} else{
			unlocked = false;
			lastLockCycle = this.cycle;
			cyclesRunning++;
		}
	}
	
	
	public void runCycle(){

		Process currentProcess;
		int state;
		tempQ.clear();
		tempQ.addAll(processQ);

		printCycle();
		processQ.clear();
		
		
		while(!tempQ.isEmpty()){
			currentProcess = tempQ.poll();
			state = currentProcess.state;
		
			if(currentProcess.checkTampered()){
				processQ.offer(currentProcess);
				continue;
			}
			
			switch(state){
				case Process.UNSTARTED: 
					checkArrivalOf(currentProcess); 
					break;
				case Process.READY: 
					checkReadyToRun(currentProcess); 
					currentProcess.waitingTime++;                 
					break;
				case Process.RUNNING: 
					checkRunningToBlock(currentProcess); 
					break;
				case Process.BLOCKED:                 
					checkBlockedToReady(currentProcess);
					currentProcess.IOTime++;                
					break;
				case Process.TERMINATED: 
					processQ.offer(currentProcess); 
					break;
			}
		}
		
		
		/* The function call below will only set the process to running if the
		 * semaphore is still unlocked after going through all processes.*/
		checkReadyQueue();
		Process.cycle = ++cycle;
		if(unlockNextCycle)
			setLock(false);		
	}

	public void checkArrivalOf(Process currentProcess){
		/* If the processes' time has arrived, set the process to ready.  If not, 
		 * place back into the queue.*/
		if((currentProcess.getArrivalTime() - this.cycle) == 0){
			currentProcess.setState(Process.READY, readyQ);

			/* If this process is ready, and the semaphore is unlocked, then go 
			 * ahead and run it.  After setting to running return from this function.*/
			if(unlocked){
				checkReadyToRun(currentProcess);
				return;
			}
			processQ.offer(currentProcess);
		}
		else
			//If the process is not ready yet, place back into the queue.
			processQ.offer(currentProcess);
	}
	
	public boolean checkReadyToRun(Process currentProcess){
		
		//Simple check to see if a process is running before running the new process.
		if(unlocked){
			//Is this process at the head of the readyQ?
			if(currentProcess.equals(readyQ.peek())){
				//If it is, then set this process to RUNNING, and leave it on the readyQ.
				currentProcess.setState(Process.RUNNING, readyQ);
				
				//Lock the semaphore
				setLock(true);

				//unlock the semaphore for the next cycle if this processes' burst is 1.
				if(currentProcess.getRemainingBurst() == 1)
					unlockNextCycle = true;
			}
			
			processQ.offer(currentProcess);
			return true;
		} else
			processQ.offer(currentProcess);
		
		return false;
	}
	
	public boolean checkReadyQueue(){
		if(unlocked && !readyQ.isEmpty()){
			Process readyProcess = readyQ.peek();
			//Is this process at the head of the readyQ?
			//If it is, then set this process to RUNNING, and leave it on the readyQ.
			readyProcess.setState(Process.RUNNING, readyQ);
			
			//Lock the semaphore
			setLock(true);

			//unlock the semaphore for the next cycle if this processes' burst is 1.
			if(readyProcess.getRemainingBurst() == 1)
				unlockNextCycle = true;
			return true;
		}
		return false;
	}
	

	public void checkRunningToBlock(Process currentProcess){
		if(currentProcess.getRemainingCPU() < 1){
			//No more CPU time needed.  Process finished.
			currentProcess.setState(Process.TERMINATED, readyQ);
			
			//Place back in the processQ for printing of state.
			processQ.offer(currentProcess);
			numberTerminated++;
			//Unlock the semaphore
			setLock(false);
			Process.runningProcess = null;
		}
		else if(currentProcess.getRemainingBurst() > 1){
			currentProcess.reduceBurst();
			currentProcess.reduceCPU();
			processQ.offer(currentProcess);
			setLock(true); //Assure that the processor is locked.
		}
		else{
			//Burst time has run out, block this process.
			currentProcess.setState(Process.BLOCKED, readyQ);
			//currentProcess.reduceBurst();

			processQ.offer(currentProcess);
			//Unlock the semaphore
			setLock(false);
			Process.runningProcess = null;
		}
			
	}
	
	public void checkBlockedToReady(Process currentProcess){
		
		if(currentProcess.getRemainingBurst() > 1){
			currentProcess.reduceBurst();
			processQ.offer(currentProcess);
		}
		else{
			currentProcess.reduceBurst();
			currentProcess.setState(Process.READY, readyQ);
			checkReadyToRun(currentProcess);
		}		

	}
	
	public void printCycle(){
		Process currentProcess;
		String cycleLine = "";
		LinkedList<Process> pqCopy = new LinkedList<Process>(printQ);
 
		//Print the print queue.
		while((currentProcess = pqCopy.poll()) != null){
			cycleLine += " \t" + currentProcess.getStateString() + " " 
			+ currentProcess.getRemainingBurst();
		}
 
		System.out.println("Before cycle " + cycle + ": " + cycleLine);
	}
	
	//Indicates that all processes have finished running.
	public boolean isFinished(){
		if(numberTerminated == numberOfProcesses)
			return true;
		else
			return false;
	}
	
	public int getCyclesRunning(){
		return cyclesRunning;
	}
	
	public int getLastCycle(){
		return cycle-1;		
	}
	
	public double getCPUUtilization(){
		return (double)getCyclesRunning()/getLastCycle();
	}
	
	public double getIOUtilization(Process[] pArray){
		int totalIO = 0;
		for(Process p : pArray){
			totalIO += p.IOTime;			
		}
		return (double)totalIO/getLastCycle();		
	}
	
	public double getProcessesPerCycle(){
		return (double)numberOfProcesses/getLastCycle();
	}
	
	public double getAverageTurnaroundTime(Process[] pArray){
		int totalTurnaroundTime = 0;
		for(Process p : pArray){
			totalTurnaroundTime += (p.finishingTime - p.getArrivalTime());
		}
		
		return (double)totalTurnaroundTime/numberOfProcesses;
	}
	
	public double getAverageWaitingTime(Process[] pArray){
		int totalWaitingTime = 0;
		for(Process p : pArray){
			totalWaitingTime += p.waitingTime;
		}
		
		return (double)totalWaitingTime/numberOfProcesses;
	}
}
