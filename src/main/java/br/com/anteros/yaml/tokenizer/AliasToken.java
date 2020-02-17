package br.com.anteros.yaml.tokenizer;

public class AliasToken extends Token {
	private String instanceName;

	public AliasToken () {
		super(TokenType.ALIAS);
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
