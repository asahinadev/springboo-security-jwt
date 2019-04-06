package com.example.spring.jwt.authentication.exception;

@SuppressWarnings("serial")
public class HeaderTokenIllegalException extends RuntimeException {

	public HeaderTokenIllegalException() {
		super();
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public HeaderTokenIllegalException(
			String message,
			Throwable cause,
			boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HeaderTokenIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

	public HeaderTokenIllegalException(String message) {
		super(message);
	}

	public HeaderTokenIllegalException(Throwable cause) {
		super(cause);
	}

}
