package scheduler.exceptions;

public class InvalidProcessInstanceException extends Error {

	private static final long serialVersionUID = 4405966174797343091L;
	
	public InvalidProcessInstanceException(String message) {
		super(message);
		printStackTrace();
	}

}