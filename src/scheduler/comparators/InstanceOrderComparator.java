package scheduler.comparators;

import java.util.Comparator;
import scheduler.Process;

public class InstanceOrderComparator implements Comparator<Process> {

	@Override
	public int compare(Process p1, Process p2) {
		if(p1.getProcessInstance() < p2.getProcessInstance() ){
			return -1;
		} else if (p1.getProcessInstance() > p2.getProcessInstance()){
			return 1;
		} else {
			return 0;
		}		
		
	}

}
