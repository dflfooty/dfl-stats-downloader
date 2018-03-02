package net.dfl.statsdownloader.exception;

public class JobSubmittedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JobSubmittedException() {
		super();
	}
	
	public JobSubmittedException(String message) {
		super(message);
	}
}
