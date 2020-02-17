package br.com.anteros.yaml.tokenizer;

public class ScalarToken extends Token {
	private String value;
	private boolean plain;
	private char style;

	public ScalarToken (final String value, final boolean plain) {
		this(value, plain, (char)0);
	}

	public ScalarToken (final String value, final boolean plain, final char style) {
		super(TokenType.SCALAR);
		this.value = value;
		this.plain = plain;
		this.style = style;
	}

	public boolean getPlain () {
		return this.plain;
	}

	public String getValue () {
		return this.value;
	}

	public char getStyle () {
		return this.style;
	}

	public String toString () {
		return "<" + type + " value='" + value + "' plain='" + plain + "' style='" + (style == 0 ? "" : style) + "'>";
	}
}
