package scheduler;
import java.util.Queue;

public class FCFS {
	Queue<Process> processQueue;
	
	FCFS(Process p){
		processQueue.add(p);
	}
	
	public void addProcess(Process p){
		processQueue.add(p);
	}
}
