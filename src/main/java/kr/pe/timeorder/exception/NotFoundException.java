package kr.pe.timeorder.exception;

public class NotFoundException extends RuntimeException {
	public NotFoundException() {
		super("Could not find entity");
	}
	
	public NotFoundException(String s) {
		super("Could not find entity : " + s);
	}
}
