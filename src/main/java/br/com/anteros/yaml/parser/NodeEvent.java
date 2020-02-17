package br.com.anteros.yaml.parser;

public abstract class NodeEvent extends Event {
	public final String anchor;

	public NodeEvent (EventType eventType, String anchor) {
		super(eventType);
		this.anchor = anchor;
	}
}
