package scheduler;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import scheduler.comparators.InstanceOrderComparator;

public class Scheduler {
	
	Collection<Process> processCollection;
	TreeSet<Process> terminatedProcessTreeSet = new TreeSet<Process>();
	TreeSet<Process> printQueue = new TreeSet<Process>(new InstanceOrderComparator());
	 
	
	public Scheduler(Collection<Process> c){
		this.processCollection = c;
		this.terminatedProcessTreeSet.addAll(c);
		this.printQueue.addAll(c);
	}
	
	public void runCycle(){
		
		
		for(Process process : processCollection){
			
			process.runCycle();		

			
			System.out.println(process);
		}
	}
	
	

}
