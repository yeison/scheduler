package scheduler;
import java.lang.Comparable;


/**
 * A Comparable class of objects, that may be easily sorted by its time of
 * arrival.
 * @author Yeison Rodriguez
 *
 */
public class Process implements Comparable<Process>{
	int arrivalTime; //A
	int burstNumber; //B
	int totalCPUNeeded; //C
	int IONumber; //IO
	int remainingBurst;
	int burstDuration;
	int remainingCPU;
	int order;
	int priorityRatio = 0;
	boolean tampered = false;
	
	
	int finishingTime, turnAroundTime, IOTime, waitingTime;
	
	
	int state;
	int previousState;
	static int cycle;
	static int processInstances = 0;
	int processInstance;
	static Process runningProcess;

	final static int UNSTARTED = 0;
	final static int READY = 1;
	final static int RUNNING = 2;
	final static int BLOCKED = 3;
	final static int TERMINATED = 4;
	
	
	public Process(){
		this(0, 0, 0, 0);
	}
	
	public Process(int A, int B, int C, int IO){
		setArrivalTime(A);
		setBurstNumber(B);
		setTotalCPUNeeded(C);
		setIONumber(IO);
		setRemainingBurst(burstNumber);
		burstDuration = 0;
		
		finishingTime = turnAroundTime = IOTime = waitingTime = 0;
		processInstance = ++processInstances;
	}
	
	void setRemainingBurst(int burst){
		remainingBurst = burst;
		burstDuration = 0;
	}
	
	void reduceBurst(){
		remainingBurst--;
		burstDuration++;
	}
	
	void reduceCPU(){
		remainingCPU--;
	}

	void setState(int state){
		switch(state){
			case UNSTARTED: 
				break;
			
			case READY:   
				break;
			
			case RUNNING: 
				if(previousState == Process.BLOCKED || previousState == Process.UNSTARTED)
				    setRemainingBurst(burstNumber); 
			    
				runningProcess = this;
				break;
			
			case BLOCKED: 
				setRemainingBurst(IONumber);  
				break;
			
			case TERMINATED:
				setRemainingBurst(0);
				finishingTime = cycle; 
				break;
		}
		
		previousState = this.state;
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
	
	int getRatio(){
		return
			(cycle - arrivalTime)/(Math.max(1, totalCPUNeeded - remainingCPU));
	}
	
	boolean checkTampered(){
		boolean tempTampered = this.tampered;
		this.tampered = false;
		return tempTampered;
	}
	
	void setTampered(){
		this.tampered = true;
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
			if(this.processInstance < other.processInstance)
				return -1;
			return 1;
		}
		//If this process arrived later, return a 1.
		else
			return 1;
	}
	
	public int hashCode(){
		return this.processInstance;
	}
	
	
	public boolean equals(Object obj){
		return equals((Process)obj);
	}
	
	public boolean equals(Process p){
		return p.hashCode() == this.hashCode();
	}
	
	@Override
	public String toString(){
		return "(Instance:" + this.processInstance + " State:" + getStateString() + " Burst:" + remainingBurst + ")";
	}
		
}
