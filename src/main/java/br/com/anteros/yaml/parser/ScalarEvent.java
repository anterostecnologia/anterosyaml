package br.com.anteros.yaml.parser;

import java.util.Arrays;

public class ScalarEvent extends NodeEvent {
	public final String tag;
	public final boolean[] implicit;
	public final String value;
	public final char style;

	public ScalarEvent (String anchor, String tag, boolean[] implicit, String value, char style) {
		super(EventType.SCALAR, anchor);
		this.tag = tag;
		this.implicit = implicit;
		this.value = value;
		this.style = style;
	}

	public String toString () {
		return "<" + type + " value='" + value + "' anchor='" + anchor + "' tag='" + tag + "' implicit='"
			+ Arrays.toString(implicit) + "' style='" + (style == 0 ? "" : style) + "'>";
	}
}
