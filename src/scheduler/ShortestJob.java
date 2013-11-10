package scheduler;

import java.util.Comparator;

public class ShortestJob implements Comparator<Process>{

	public int compare(Process p1, Process p2) {
		if(p1.getRemainingCPU() > p2.getRemainingCPU())
			return 1;
		else if(p1.getRemainingCPU() < p2.getRemainingCPU())
			return -1;
		else	
			return p1.compareTo(p2);
	}
	
}