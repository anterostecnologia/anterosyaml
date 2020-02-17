package br.com.anteros.yaml.document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import br.com.anteros.yaml.AnterosYamlException;
import br.com.anteros.yaml.AnterosYamlConfig.WriteConfig;
import br.com.anteros.yaml.emitter.Emitter;
import br.com.anteros.yaml.emitter.EmitterException;
import br.com.anteros.yaml.parser.Event;
import br.com.anteros.yaml.parser.MappingStartEvent;

public class YamlMapping extends YamlElement implements YamlDocument {

	// use a list to keep the sequence
	List<YamlEntry> entries = new LinkedList<YamlEntry>();

	public int size() {
		return entries.size();
	}
	
	public void addEntry(YamlEntry entry) {
		entries.add(entry);
	}
	
	public boolean deleteEntry(String key) {
		for(int index = 0; index < entries.size(); index++) {
			if(key.equals(entries.get(index).getKey().getValue())) {
				entries.remove(index);
				return true;
			}
		}
		return false;
	}

	public YamlEntry getEntry(String key) throws AnterosYamlException {
		for(YamlEntry entry : entries) {
			if(key.equals(entry.getKey().getValue()))
				return entry;
		}
		return null;
	}
	
	public YamlEntry getEntry(int index) throws AnterosYamlException {
		return entries.get(index);
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(anchor!=null) {
			sb.append('&');
			sb.append(anchor);
			sb.append(' ');
		}
		if(tag!=null) {
			sb.append(" !");
			sb.append(tag);
		}
		if(!entries.isEmpty()) {
			sb.append('{');
			for(YamlEntry entry : entries) {
				sb.append(entry.toString());
				sb.append(',');
			}
			sb.setLength(sb.length() - 1);
			sb.append('}');
		}
		return sb.toString();
	}
	
	@Override
	public void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException {
		emitter.emit(new MappingStartEvent(anchor, tag, tag==null, false));
		for(YamlEntry entry : entries)
			entry.emitEvent(emitter, config);
		emitter.emit(Event.MAPPING_END);
	}
	
	public void setEntry(String key, boolean value) throws AnterosYamlException {
		setEntry(key, new YamlScalar(value));
	}

	public void setEntry(String key, Number value) throws AnterosYamlException {
		setEntry(key, new YamlScalar(value));
	}

	public void setEntry(String key, String value) throws AnterosYamlException {
		setEntry(key, new YamlScalar(value));
	}

	public void setEntry(String key, YamlElement value) throws AnterosYamlException {
		YamlEntry entry = getEntry(key);
		if(entry!=null)
			entry.setValue(value);
		else {
			entry = new YamlEntry(new YamlScalar(key), value);
			addEntry(entry);
		}
		
	}

	public YamlElement getElement(int item) throws AnterosYamlException {
		throw new AnterosYamlException("Can only get element on sequence!");
	}

	public void deleteElement(int element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only delete element on sequence!");
	}
	
	public void setElement(int item, boolean element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only set element on sequence!");
	}

	public void setElement(int item, Number element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only set element on sequence!");
	}

	public void setElement(int item, String element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only set element on sequence!");
	}

	public void setElement(int item, YamlElement element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only set element on sequence!");
	}

	public void addElement(boolean element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only add element on sequence!");
	}

	public void addElement(Number element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only add element on sequence!");
	}

	public void addElement(String element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only add element on sequence!");
	}

	public void addElement(YamlElement element) throws AnterosYamlException {
		throw new AnterosYamlException("Can only add element on sequence!");
	}
}
