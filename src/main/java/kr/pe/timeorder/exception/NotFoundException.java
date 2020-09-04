package kr.pe.timeorder.exception;

public class NotFoundException extends RuntimeException {
	public NotFoundException(Long id) {
		super("Could not find employee " + id);
	}
}
