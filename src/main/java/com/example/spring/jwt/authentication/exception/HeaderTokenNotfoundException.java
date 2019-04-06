package com.example.spring.jwt.authentication.exception;

@SuppressWarnings("serial")
public class HeaderTokenNotfoundException extends RuntimeException {

	public HeaderTokenNotfoundException() {
		super();
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public HeaderTokenNotfoundException(
			String message,
			Throwable cause,
			boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HeaderTokenNotfoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public HeaderTokenNotfoundException(String message) {
		super(message);
	}

	public HeaderTokenNotfoundException(Throwable cause) {
		super(cause);
	}

}
