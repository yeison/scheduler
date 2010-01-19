import java.lang.Comparable;

public class Process implements Comparable<Process>{
	int arrivalTime; //A
	int burstNumber; //B
	int totalCPUNeeded; //C
	int IONumber; //IO
	int remainingBurst;
	int remainingCPU;
	
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
		this.IONumber = IO;// TODO Auto-generated method stub
		
	}

	void setTotalCPUNeeded(int c) {
		this.totalCPUNeeded = c;// TODO Auto-generated method stub
		this.remainingCPU = c;
	}

	void setBurstNumber(int b) {
		this.burstNumber =  b;// TODO Auto-generated method stub
		
	}
	
	void resetBurst(){
		remainingBurst = this.burstNumber;
	}

	void setArrivalTime(int a) {
		this.arrivalTime = a;// TODO Auto-generated method stub
		
	}
	
	int getArrivalTime(){
		return this.arrivalTime;
	}
	
	public int compareTo(Process other){
		if(this.getArrivalTime() < other.getArrivalTime())
			return -1;
		else if(this.getArrivalTime() == other.getArrivalTime())
			return 0;
		else
			return 1;
	}
		
}
