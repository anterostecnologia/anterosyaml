package br.com.anteros.yaml.document;

import java.io.IOException;

import br.com.anteros.yaml.YamlConfig.WriteConfig;
import br.com.anteros.yaml.emitter.Emitter;
import br.com.anteros.yaml.emitter.EmitterException;
import br.com.anteros.yaml.parser.ScalarEvent;

public class YamlScalar extends YamlElement {

	String value;
	
	public YamlScalar() {
	}
	
	public YamlScalar(Object value) {
		this.value = String.valueOf(value);
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(anchor!=null) {
			sb.append('&');
			sb.append(anchor);
			sb.append(' ');
		}
		sb.append(value);
		if(tag!=null) {
			sb.append(" !");
			sb.append(tag);
		}
		return sb.toString();
	}
	
	@Override
	public void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException {
		char style = 0; // TODO determine style
		emitter.emit(new ScalarEvent(anchor, tag, new boolean[] {true, true}, value, style));
	}
}
