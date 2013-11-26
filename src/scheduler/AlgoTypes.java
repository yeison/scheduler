package scheduler;

/**
 * This Enum will help us to easily select our algo.
 * @author Yeison
 *
 */
public enum AlgoTypes {
	FCFS,
	PSJF,
	HPRN,
	RR;	
		
	public SchedulingAlgo getSchedulingAlgo(int numberOfProcesses){
		switch(this){
			case FCFS:
				return new FCFS(numberOfProcesses);
			case RR:
				return new RR(numberOfProcesses, 2);
			case PSJF:
				return new PSJF(numberOfProcesses);
			case HPRN:
				return new HPRN(numberOfProcesses);
			default:
				return null;
		}
	}
}
