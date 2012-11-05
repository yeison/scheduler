package scheduler;
import java.lang.Comparable;
import java.util.StringTokenizer;


/**
 * A Comparable class of objects that may easily be sorted by its time of
 * arrival.
 * @author Yeison Rodriguez
 *
 */
public class Process implements Comparable<Process>{
	int arrivalTime; //A
	int burstNumber; //B
	int totalCPUNeeded; //C
	int IONumber; //IO
	
	private int remainingCpuBurst;
	private int remainingIoBurst;
	private int currentBurstDuration;
	private int remainingCPU;
	private boolean tampered = false;
	
	int state;
	int previousState;
	static int cycle;
	
	// Keep track of other existing processes
	static int processInstances = 0;
	private int processInstance;

	static Process runningProcess;
	
	int finishingTime, turnAroundTime, IOTime, waitingTime;	

	final static int UNSTARTED = 0;
	final static int READY = 1;
	final static int RUNNING = 2;
	final static int BLOCKED = 3;
	final static int TERMINATED = 4;
	
	public Process(){
		this(0, 0, 0, 0);
	}
	
	public Process(int arrivalTime, int burstNumber, int cpu, int IO){
		
		setArrivalTime(arrivalTime);
		setBurstNumber(burstNumber);
		setTotalCPUNeeded(cpu);
		setIONumber(IO);
		
		currentBurstDuration = 0;
		
		finishingTime = turnAroundTime = IOTime = waitingTime = 0;
		processInstance = ++processInstances;
	}
	
	private void reduceCpuBurst(){
		remainingCpuBurst--;
		currentBurstDuration++;
	}
	
	private void reduceIoBurst(){
		remainingCpuBurst--;
		currentBurstDuration++;
	}
	
	private void reduceCPU(){
		remainingCPU--;
	}
	
	void runCycle(){
		
		try {
			
			switch(this.getState()){
			
				case UNSTARTED: 
					break;
				
				case READY:   
					break;
				
				case RUNNING: 
					this.reduceCPU();
					this.reduceCpuBurst();
					break;
				
				case BLOCKED: 
					this.reduceIoBurst();
					break;
				
				case TERMINATED:
					break;
			
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}

	/** Setters **/
	void setState(int state){
		
		switch(state){
		
			case UNSTARTED: 
				break;
			
			case READY:   
				setRemainingCpuBurst(burstNumber);
				break;
			
			case RUNNING: 
				if(previousState == Process.BLOCKED 
				|| previousState == Process.UNSTARTED){
				    setRemainingCpuBurst(burstNumber); 
				}
			    
				Process.runningProcess = this;
				break;
			
			case BLOCKED: 
				setRemainingIoBurst(IONumber);  
				break;
			
			case TERMINATED:
				setRemainingCpuBurst(0);
				finishingTime = cycle; 
				break;
				
		}
		
		previousState = this.state;
		this.state = state;
		
		if(this.remainingCPU == 0){
			this.state = Process.TERMINATED;
		}
		
	}
	
	void setRemainingCpuBurst(int burst){
		remainingCpuBurst = burst;
		currentBurstDuration = 0;
	}
	
	void setRemainingIoBurst(int burst){
		remainingIoBurst = burst;
	}
	
	private void setIONumber(int IO) {
		this.IONumber = IO;		
	}

	private void setTotalCPUNeeded(int c) {
		this.totalCPUNeeded = c;
		this.remainingCPU = c;
	}

	private void setBurstNumber(int b) {
		this.burstNumber =  b;		
	}
	
	private void setArrivalTime(int a) {
		this.arrivalTime = a;
	}
	
	private void setTampered(){
		this.tampered = true;
	}
	
	private void resetBurst(){
		remainingCpuBurst = this.burstNumber;
	}
	
	/** Getters **/
	public int getProcessInstance() {
		return processInstance;
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
	
	public int getState() throws Exception {
		
		switch(this.state){
		
			case UNSTARTED: 
				if( cycle < this.arrivalTime ){
					return Process.UNSTARTED;
				} else {
					this.setState(Process.READY);
					return this.getState();
				}
				
			case READY:
				if( Process.runningProcess == null){
					this.setState(Process.RUNNING);
					return this.getState();
				} else {
					return (this.state == Process.RUNNING) ? RUNNING:READY;
				}
				
			case RUNNING:
				if( this.remainingCpuBurst > 0 ){
					return Process.RUNNING;
				} else {
					this.setState(Process.BLOCKED);
					return this.getState();
				}
				
			case BLOCKED:
				if( this.remainingIoBurst > 0){
					return Process.BLOCKED;
				} else {
					this.setState(Process.RUNNING);
					return this.getState();
				}
				
				
			case TERMINATED: 
				return Process.TERMINATED;
			
			default: throw new Exception();
		}
		
	}
	
	public int getArrivalTime(){
		return this.arrivalTime;
	}
	
	public int getRatio(){
		return
			(cycle - arrivalTime)/(Math.max(1, totalCPUNeeded - remainingCPU));
	}
	
	public boolean checkTampered(){
		boolean tempTampered = this.tampered;
		this.tampered = false;
		return tempTampered;
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
			else
			    return 1;
		}

		//If this process arrived later, return a 1.
		else
			return 1;
	}
	
	@Override
	public int hashCode(){
		return this.processInstance;
	}	
	
	public boolean equals(Process p){
		return p.hashCode() == this.hashCode();
	}
	
	@Override
	public String toString(){
		return "(Instance:" + this.processInstance + " State:" 
				+ getStateString() + " Burst:" + remainingCpuBurst + " CPU:" 
				+ remainingCPU + ")";
	}
	
	/**
	 * The method below takes a StringTokenizer and reads the next four tokens 
	 * from that tokenizer.  The tokens will be interpreted as A B C and IO, 
	 * respectively.  Using this data, the method instantiates and returns a 
	 * Process object.
	 * 
	 * @param st - A string tokenizer whose next four tokens correspond to A B C and IO.
	 * @return A new process object created from the data in the string tokenizer.
	 */
	static Process makeProcess(StringTokenizer st){
		
		Process newProcess = new Process();
		int[] processData = new int[4];
		
		for(int i = 0; i < 4; i++){
			String token = st.nextToken();			
			processData[i] = Integer.parseInt(token); 
			
			switch(i){
				case 0: newProcess.setArrivalTime(processData[0]); break;
				case 1:	newProcess.setBurstNumber(processData[1]); break;
				case 2:	newProcess.setTotalCPUNeeded(processData[2]); break;
				case 3:	newProcess.setIONumber(processData[3]); break;
			}
		}
		
		return newProcess;
	}
	
//	public boolean equals(Object obj){
//	    return equals((Process)obj);
//  }
		
}
