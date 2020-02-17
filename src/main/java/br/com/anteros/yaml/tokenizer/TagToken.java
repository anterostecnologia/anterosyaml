package br.com.anteros.yaml.tokenizer;

public class TagToken extends Token {
	private final String handle;
	private final String suffix;

	public TagToken (String handle, String suffix) {
		super(TokenType.TAG);
		this.handle = handle;
		this.suffix = suffix;
	}

	public String getHandle () {
		return handle;
	}

	public String getSuffix () {
		return suffix;
	}

	public String toString () {
		return "<" + type + " handle='" + handle + "' suffix='" + suffix + "'>";
	}
}
