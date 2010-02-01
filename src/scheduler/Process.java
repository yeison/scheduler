package scheduler;
import java.lang.Comparable;


/**
 * A Comparable class of objects, that may be easily sorted by their time of
 * arrival.
 * @author yeison
 *
 */
public class Process implements Comparable<Process>{
	int arrivalTime; //A
	int burstNumber; //B
	int totalCPUNeeded; //C
	int IONumber; //IO
	int remainingBurst;
	int remainingCPU;
	int order;
	
	int finishingTime, turnAroundTime, IOTime, waitingTime;
	
	
	int state;

	final static int UNSTARTED = 0;
	final static int READY = 1;
	final static int RUNNING = 2;
	final static int BLOCKED = 3;
	final static int TERMINATED = 4;
	
	int timeWaiting;
	
	public Process(){
		this(0, 0, 0, 0);
	}
	
	public Process(int A, int B, int C, int IO){
		setArrivalTime(A);
		setBurstNumber(B);
		setTotalCPUNeeded(C);
		setIONumber(IO);
		timeWaiting = 0;
		remainingBurst = 0;
		
		finishingTime = turnAroundTime = IOTime = waitingTime = 0;
	}
	
	void setRemainingBurst(int burst){
		remainingBurst = burst; 
	}
	
	void reduceBurst(){
		remainingBurst--;
	}
	
	void reduceCPU(){
		remainingCPU--;
	}

	void setState(int state){
		switch(state){
			case UNSTARTED: break;
			case READY: timeWaiting++; break;
			case RUNNING: setRemainingBurst(burstNumber); break;
			case BLOCKED: setRemainingBurst(IONumber); break;
			case TERMINATED: break;
		}
		
		this.state = state;
	}
	
	public String getStateString() {
		switch(this.state){
			case UNSTARTED: return "unstarted";
			case READY: return "ready";
			case RUNNING: return "running";
			case BLOCKED: return "blocked";
			case TERMINATED: return "terminated";
			default: return null;
		}
	}
	
	void setIONumber(int IO) {
		this.IONumber = IO;
		
	}

	void setTotalCPUNeeded(int c) {
		this.totalCPUNeeded = c;
		this.remainingCPU = c;
	}

	void setBurstNumber(int b) {
		this.burstNumber =  b;
		
	}
	
	void resetBurst(){
		remainingBurst = this.burstNumber;
	}

	void setArrivalTime(int a) {
		this.arrivalTime = a;
		
	}
	
	int getArrivalTime(){
		return this.arrivalTime;
	}
	
	
	/**
	 * The method below needs to be implemented for comparables.
	 * We want to order the objects by their time of arrival.
	 * @param other - The process to be compared to this one.
	 */
	public int compareTo(Process other){
		//If this process arrived earlier, return -1.
		if(this.getArrivalTime() < other.getArrivalTime())
			return -1;
		//If this processes arrived at the same time as the other process...
		else if(this.getArrivalTime() == other.getArrivalTime()){
			//then sort by the order of input.
			if(this.order < other.order)
				return -1;
			return 1;
		}
		//If this process arrived later, return a 1.
		else
			return 1;
	}
		
}
