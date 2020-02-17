package br.com.anteros.yaml.parser;

public abstract class CollectionStartEvent extends NodeEvent {
	public final String tag;
	public final boolean isImplicit;
	public final boolean isFlowStyle;

	protected CollectionStartEvent (EventType eventType, String anchor, String tag, boolean isImplicit, boolean isFlowStyle) {
		super(eventType, anchor);
		this.tag = tag;
		this.isImplicit = isImplicit;
		this.isFlowStyle = isFlowStyle;
	}

	public String toString () {
		return "<" + type + " anchor='" + anchor + "' tag='" + tag + "' implicit='" + isImplicit + "' flowStyle='" + isFlowStyle
			+ "'>";
	}
}
