package com.javaeasy.security.exception;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -2143442695181255052L;

	public BaseException() {
		super();
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

}
