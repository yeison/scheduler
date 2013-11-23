package scheduler;
import static scheduler.Runner.randomOS;

import java.util.Queue;


/**
 * A Comparable class of objects, that may be easily sorted by its time of
 * arrival.
 * @author Yeison Rodriguez */
public class Process implements Comparable<Process>{
	private int arrivalTime; //A
	private int burstNumber; //B
	private int totalCPUNeeded; //C
	private int IONumber; //IO
	private int remainingBurst;
	private int burstDuration;
	private int remainingCPU;
	private int order;
	private int priorityRatio = 0;
	private boolean tampered = false;  

	
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
		remainingBurst = randomOS(burst);
		burstDuration = 0;
	}
	
	void reduceBurst(){
		remainingBurst--;
		burstDuration++;
	}
	
	void reduceCPU(Queue<Process> readyQueue){
		reduceCPU();
		
		// Horribly inefficient
		if(readyQueue.contains(this)){
			readyQueue.remove(this);
			readyQueue.add(this);
		}
	}
	
	void reduceCPU(){
		remainingCPU--;
		updatePriorityRatio();
	}

	void setState(int state, Queue<Process> readyQueue){
		switch(state){
			case UNSTARTED: 
				break;
			
			case READY:
				//Set to ready and insert into ready queue.
				if(!readyQueue.contains(this))
					readyQueue.add(this);
				break;
			
			case RUNNING: 
				if(previousState == Process.BLOCKED || previousState == Process.UNSTARTED)
				    setRemainingBurst(burstNumber);
					reduceCPU(readyQueue);
			    
				runningProcess = this;
				break;
			
			case BLOCKED:
				readyQueue.remove(this);
				setRemainingBurst(IONumber);  
				break;
			
			case TERMINATED:
				if(readyQueue.contains(this))
					readyQueue.remove(this);
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
		updatePriorityRatio();
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
	
	double getPriorityRatio(){
		updatePriorityRatio();
		return priorityRatio;
	}
	
	private void updatePriorityRatio(){		
		priorityRatio = 			
			(cycle - arrivalTime)/(Math.max(1, totalCPUNeeded - remainingCPU));
	}
	
	int getInstance(){
		return this.processInstance;
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
	 * @param other - The process to be compared to this one. */
	public int compareTo(Process other){
		//If this process arrived earlier, return -1.
		if(this.getArrivalTime() < other.getArrivalTime())
			return -1;
		//If this processes arrived at the same time as the other process...
		else if(this.getArrivalTime() == other.getArrivalTime()){
			//then sort by the order of input.
			if(this.processInstance < other.processInstance)
				return -1;
			else if(this.processInstance > other.processInstance)
			    return 1;
			else
				return 0;
		}
		//If this process arrived later, return a 1.
		else
			return 1;
	}
	
	public int hashCode(){
		return this.processInstance;
	}
	
	
	@Override
	public boolean equals(Object obj){		
		return equals((Process)obj);
	}

	
	public boolean equals(Process p){
		if(p == null){
			return false;
		}
		
		return p.hashCode() == this.hashCode();
	}
	
	@Override
	public String toString(){
		return "(Instance:" + this.processInstance 
				+ " State:" + getStateString() 
				+ " Burst:" + remainingBurst 
				+ " CPU:" + remainingCPU
				+ " pRatio:" + getPriorityRatio() 
				+ ")";
	}

	public int getRemainingBurst() {
		return this.remainingBurst;
	}

	public int getRemainingCPU() {
		return this.remainingCPU;
	}

	public int getBurstNumber() {
		return this.burstNumber;
	}

	public int getTotalCPUNeeded() {
		return this.totalCPUNeeded;
	}

	public int getIONumber() {
		return this.IONumber;
	}

	public void setOrder(int processOrder) {
		this.order = processOrder;
	}
		
}
