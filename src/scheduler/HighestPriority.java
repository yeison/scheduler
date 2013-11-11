package scheduler;

import java.util.Comparator;

public class HighestPriority implements Comparator<Process> {
	
	public int compare(Process p1, Process p2) {
		if(p1.getPriorityRatio() < p2.getPriorityRatio())
			return 1;
		else if(p1.getPriorityRatio() > p2.getPriorityRatio())
			return -1;
		
		return p1.compareTo(p2);
	}

}