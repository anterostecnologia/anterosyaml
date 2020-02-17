package br.com.anteros.yaml.document;

import br.com.anteros.yaml.AnterosYamlException;

public interface YamlDocument {
	
	String getTag();
	int size();
	YamlEntry getEntry(String key) throws AnterosYamlException;
	YamlEntry getEntry(int index) throws AnterosYamlException;
	boolean deleteEntry(String key) throws AnterosYamlException;
	void setEntry(String key, boolean value) throws AnterosYamlException;
	void setEntry(String key, Number value) throws AnterosYamlException;
	void setEntry(String key, String value) throws AnterosYamlException;
	void setEntry(String key, YamlElement value) throws AnterosYamlException;
	YamlElement getElement(int item) throws AnterosYamlException;
	void deleteElement(int element) throws AnterosYamlException;
	void setElement(int item, boolean value) throws AnterosYamlException;
	void setElement(int item, Number value) throws AnterosYamlException;
	void setElement(int item, String value) throws AnterosYamlException;
	void setElement(int item, YamlElement element) throws AnterosYamlException;
	void addElement(boolean value) throws AnterosYamlException;
	void addElement(Number value) throws AnterosYamlException;
	void addElement(String value) throws AnterosYamlException;
	void addElement(YamlElement element) throws AnterosYamlException;
	
}
