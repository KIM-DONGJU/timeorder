package kr.pe.timeorder.exception;

public class PermissionException extends RuntimeException {
	public PermissionException() {
		super("No permission");
	}
	
	public PermissionException(String s) {
		super("No permission : " + s);
	}
}
