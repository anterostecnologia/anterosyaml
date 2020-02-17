package br.com.anteros.yaml.document;

import java.io.IOException;

import br.com.anteros.yaml.AnterosYamlConfig.WriteConfig;
import br.com.anteros.yaml.emitter.Emitter;
import br.com.anteros.yaml.emitter.EmitterException;
import br.com.anteros.yaml.parser.ScalarEvent;

public class YamlEntry {
	
	YamlScalar key;
	YamlElement value;
	
	public YamlEntry(YamlScalar key, YamlElement value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(key.toString());
		sb.append(':');
		sb.append(value.toString());
		return sb.toString();
	}
	
	public YamlScalar getKey() {
		return key;
	}
	
	public YamlElement getValue() {
		return value;
	}
	
	public void setKey(YamlScalar key) {
		this.key = key;
	}
	
	public void setValue(YamlElement value) {
		this.value = value;
	}

	public void setValue(boolean value) {
		this.value = new YamlScalar(value);
	}
	
	public void setValue(Number value) {
		this.value = new YamlScalar(value);
	}
	
	public void setValue(String value) {
		this.value = new YamlScalar(value);
	}

	public void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException {
		key.emitEvent(emitter, config);
		if(value==null)
			emitter.emit(new ScalarEvent(null, null, new boolean[] {true, true}, null, (char)0));
		else
			value.emitEvent(emitter, config);
	}
	
}
