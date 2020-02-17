package br.com.anteros.yaml.tokenizer;

public class DirectiveToken extends Token {
	private final String directive;
	private final String value;

	public DirectiveToken (String directive, String value) {
		super(TokenType.DIRECTIVE);
		this.directive = directive;
		this.value = value;
	}

	public String getDirective () {
		return directive;
	}

	public String getValue () {
		return value;
	}

	public String toString () {
		return "<" + type + " directive='" + directive + "' value='" + value + "'>";
	}
}
