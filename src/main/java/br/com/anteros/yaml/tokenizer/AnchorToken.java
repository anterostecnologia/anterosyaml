package br.com.anteros.yaml.tokenizer;

public class AnchorToken extends Token {
	private String instanceName;

	public AnchorToken () {
		super(TokenType.ANCHOR);
	}

	public String getInstanceName () {
		return instanceName;
	}

	public void setInstanceName (String instanceName) {
		this.instanceName = instanceName;
	}

	public String toString () {
		return "<" + type + " aliasName='" + instanceName + "'>";
	}
}
