package scheduler;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import scheduler.comparators.InstanceOrderComparator;

public class Scheduler {
	
	Collection<Process> processCollection;
	TreeSet<Process> processCollectionCopy = new TreeSet<Process>();
	TreeSet<Process> terminatedProcessTreeSet = new TreeSet<Process>();
	TreeSet<Process> printQueue = new TreeSet<Process>(new InstanceOrderComparator());
	 
	
	public Scheduler(Collection<Process> c){
		this.processCollection = c;
		this.processCollectionCopy.addAll(processCollection);
		this.printQueue.addAll(c);
	}
	
	// This function needs to be renamed really badly.
	public void runScheduler(){
	
		while( processCollection.size() != terminatedProcessTreeSet.size() ){
			
			System.out.println(Process.cycle);
			
			processCollectionCopy.clear();
			processCollectionCopy.addAll(processCollection);
			processCollection.clear();
			
			for(Process process : processCollectionCopy){
				
				process.runCycle();
				processCollection.add(process);

				
				try {
					if(process.getState() == Process.TERMINATED){
						terminatedProcessTreeSet.add(process);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(process);
				
				
			}
			
			System.out.println();
			Process.cycle++;
		}
	}

}
