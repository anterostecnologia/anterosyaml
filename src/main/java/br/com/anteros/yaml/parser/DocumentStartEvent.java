package br.com.anteros.yaml.parser;

import java.util.Map;

import br.com.anteros.yaml.Version;

public class DocumentStartEvent extends Event {
	public final boolean isExplicit;
	public final Version version;
	public final Map<String, String> tags;

	public DocumentStartEvent (boolean explicit, Version version, Map<String, String> tags) {
		super(EventType.DOCUMENT_START);
		this.isExplicit = explicit;
		this.version = version;
		this.tags = tags;
	}

	public String toString () {
		return "<" + type + " explicit='" + isExplicit + "' version='" + version + "' tags='" + tags + "'>";
	}
}
