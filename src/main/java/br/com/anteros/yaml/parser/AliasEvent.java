package br.com.anteros.yaml.parser;

public class AliasEvent extends NodeEvent {
	public AliasEvent (String anchor) {
		super(EventType.ALIAS, anchor);
	}

	public String toString () {
		return "<" + type + " anchor='" + anchor + "'>";
	}
}
