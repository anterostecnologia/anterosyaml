package br.com.anteros.yaml.emitter;

import br.com.anteros.yaml.Version;

public class EmitterConfig {
	Version version = new Version(1, 1);
	boolean canonical;
	boolean useVerbatimTags = true;
	int indentSize = 3;
	int wrapColumn = 100;
	boolean escapeUnicode = true;

	/** Sets the YAML version to output. Default is 1.1. */
	public void setVersion (Version version) {
		if (version == null) throw new IllegalArgumentException("version cannot be null.");
		this.version = version;
	}

	/** If true, the YAML output will be canonical. Default is false. */
	public void setCanonical (boolean canonical) {
		this.canonical = canonical;
	}

	/** Sets the number of spaces to indent. Default is 3. */
	public void setIndentSize (int indentSize) {
		if (indentSize < 2) throw new IllegalArgumentException("indentSize cannot be less than 2.");
		this.indentSize = indentSize;
	}

	/** Sets the column at which values will attempt to wrap. Default is 100. */
	public void setWrapColumn (int wrapColumn) {
		if (wrapColumn <= 4) throw new IllegalArgumentException("wrapColumn must be greater than 4.");
		this.wrapColumn = wrapColumn;
	}

	/** If false, tags will never be surrounded by angle brackets (eg, "!<java.util.LinkedList>"). Default is true. */
	public void setUseVerbatimTags (boolean useVerbatimTags) {
		this.useVerbatimTags = useVerbatimTags;
	}

	/** If false, UTF-8 unicode characters will be output instead of the escaped unicode character code. */
	public void setEscapeUnicode (boolean escapeUnicode) {
		this.escapeUnicode = escapeUnicode;
	}
}
