package br.com.anteros.yaml.parser;

public class MappingStartEvent extends CollectionStartEvent {
	public MappingStartEvent (String anchor, String tag, boolean implicit, boolean flowStyle) {
		super(EventType.MAPPING_START, anchor, tag, implicit, flowStyle);
	}
}
