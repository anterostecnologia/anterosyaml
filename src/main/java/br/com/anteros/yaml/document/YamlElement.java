package br.com.anteros.yaml.document;

import java.io.IOException;

import br.com.anteros.yaml.AnterosYamlConfig.WriteConfig;
import br.com.anteros.yaml.emitter.Emitter;
import br.com.anteros.yaml.emitter.EmitterException;

public abstract class YamlElement {

	String tag;
	String anchor;
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	
	public String getTag() {
		return tag;
	}
	
	public String getAnchor() {
		return anchor;
	}

	public abstract void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException;
}
