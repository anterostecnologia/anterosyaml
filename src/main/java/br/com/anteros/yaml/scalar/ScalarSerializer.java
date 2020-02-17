package br.com.anteros.yaml.scalar;

import br.com.anteros.yaml.AnterosYamlException;

public interface ScalarSerializer<T> {
	abstract public String write (T object) throws AnterosYamlException;

	abstract public T read (String value) throws AnterosYamlException;
}
