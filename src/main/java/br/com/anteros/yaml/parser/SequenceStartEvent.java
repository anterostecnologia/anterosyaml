package br.com.anteros.yaml.parser;

public class SequenceStartEvent extends CollectionStartEvent {
	public SequenceStartEvent (String anchor, String tag, boolean implicit, boolean flowStyle) {
		super(EventType.SEQUENCE_START, anchor, tag, implicit, flowStyle);
	}
}
