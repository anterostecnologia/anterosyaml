package br.com.anteros.yaml;

import java.io.IOException;

public class AnterosYamlException extends IOException {
	public AnterosYamlException () {
		super();
	}

	public AnterosYamlException (String message, Throwable cause) {
		super(message);
		initCause(cause);
	}

	public AnterosYamlException (String message) {
		super(message);
	}

	public AnterosYamlException (Throwable cause) {
		initCause(cause);
	}
}
