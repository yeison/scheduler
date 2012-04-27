package scheduler;

import java.util.Comparator;

public class ShortestJob implements Comparator<Process>{

	public int compare(Process p1, Process p2) {
		if(p1.remainingCPU < p2.remainingCPU)
			return -1;
		else if(p1.remainingCPU > p2.remainingCPU)
			return 1;
		
		return p2.compareTo(p1);
	}

}
