package br.com.anteros.yaml.tokenizer;

public class Token {
	final static Token DOCUMENT_START = new Token(TokenType.DOCUMENT_START);
	final static Token DOCUMENT_END = new Token(TokenType.DOCUMENT_END);
	final static Token BLOCK_MAPPING_START = new Token(TokenType.BLOCK_MAPPING_START);
	final static Token BLOCK_SEQUENCE_START = new Token(TokenType.BLOCK_SEQUENCE_START);
	final static Token BLOCK_ENTRY = new Token(TokenType.BLOCK_ENTRY);
	final static Token BLOCK_END = new Token(TokenType.BLOCK_END);
	final static Token FLOW_ENTRY = new Token(TokenType.FLOW_ENTRY);
	final static Token FLOW_MAPPING_END = new Token(TokenType.FLOW_MAPPING_END);
	final static Token FLOW_MAPPING_START = new Token(TokenType.FLOW_MAPPING_START);
	final static Token FLOW_SEQUENCE_END = new Token(TokenType.FLOW_SEQUENCE_END);
	final static Token FLOW_SEQUENCE_START = new Token(TokenType.FLOW_SEQUENCE_START);
	final static Token KEY = new Token(TokenType.KEY);
	final static Token VALUE = new Token(TokenType.VALUE);
	final static Token STREAM_END = new Token(TokenType.STREAM_END);
	final static Token STREAM_START = new Token(TokenType.STREAM_START);

	public final TokenType type;

	public Token (TokenType type) {
		this.type = type;
	}

	public String toString () {
		return "<" + type + ">";
	}
}
