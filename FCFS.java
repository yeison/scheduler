package scheduler;
import java.util.LinkedList;
//import java.util.concurrent.LinkedBlockingQueue;

public class FCFS {
	LinkedList<Process> processQ = new LinkedList<Process>();
	LinkedList<Process> readyQ = new LinkedList<Process>();
	LinkedList<Process> blockingQ = new LinkedList<Process>();
	LinkedList<Process> printQ = new LinkedList<Process>();
	LinkedList<Process> nextProcessQ = new LinkedList<Process>();
	LinkedList<Process> nextReadyQ = new LinkedList<Process>();
	LinkedList<Process> nextBlockingQ = new LinkedList<Process>();
	Process runningProcess = null;
	int cycle;
	
	
	FCFS(){
		this.cycle = 0;
	}
	
	public void qProcess(Process p){
		printQ.offer(p);
		processQ = new LinkedList<Process>(printQ);
	}
	
	public void runCycle(){	
		printCycle();
		
		checkArrival();
		
		checkReadyToRun();
		
		checkRunToBlock();
		
		nextBlockingQ = checkBlockToReady();
				
		this.cycle++;
		this.blockingQ = nextBlockingQ;
		this.readyQ = nextReadyQ;
		this.processQ = nextProcessQ;
	}
	
	public void printCycle(){
		Process currentProcess;
		String cycleLine = "";
		LinkedList<Process> printQCopy = new LinkedList<Process>(printQ);
		
		//Print the print queue.
		while((currentProcess = printQ.poll()) != null){
			cycleLine += " \t" + currentProcess.getStateString() + " " + currentProcess.remainingBurst;
		}
		//Restore the print queue from its copy.
		printQ = new LinkedList<Process>(printQCopy);
		
		System.out.println("Before cycle " + this.cycle + ": " + cycleLine);
	}
	
	public void checkArrival(){
		Process currentProcess;
		//If the processes' time has arrived, set the process to ready.
		while((currentProcess = this.processQ.poll()) != null){
			if((currentProcess.arrivalTime - this.cycle) == 0){
				currentProcess.setState(Process.READY);
				nextReadyQ.offer(currentProcess);
			}
			else
				nextProcessQ.offer(currentProcess);
		}
	}
	
	public void checkReadyToRun(){
		Process currentProcess = this.readyQ.peek(); 
		if(currentProcess != null && currentProcess.state == Process.READY && runningProcess == null){
			runningProcess = this.readyQ.poll();
			runningProcess.setState(Process.RUNNING);
		}
	}
	
	public void checkRunToBlock(){
		if(runningProcess != null){
			if(runningProcess.remainingBurst > 1)
				runningProcess.reduceBurst();
			else{
				runningProcess.setState(Process.BLOCKED);
				nextBlockingQ.offer(runningProcess);
				nextProcessQ.offer(runningProcess);
				runningProcess = null;
			}
		}
	}
	
	public LinkedList<Process> checkBlockToReady(){
		Process currentProcess;
		LinkedList<Process> blockingQ = new LinkedList<Process>();
		while(this.blockingQ.peek() != null){
			currentProcess = this.blockingQ.poll();
			if(currentProcess.state == Process.BLOCKED){
				if(currentProcess.remainingBurst > 1){
					currentProcess.reduceBurst();
					blockingQ.offer(currentProcess);
				}
				else{
					currentProcess.reduceBurst();
					currentProcess.setState(Process.READY);
					nextReadyQ.offer(currentProcess);
				}
			}
		}
		return blockingQ;
	}
}
