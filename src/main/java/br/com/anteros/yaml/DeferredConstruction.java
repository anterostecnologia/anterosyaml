package br.com.anteros.yaml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import br.com.anteros.yaml.Beans.Property;

/** Stores a constructor, parameters names, and property values so construction can be deferred until all property values are
 * known.*/
class DeferredConstruction {
	private final Constructor constructor;
	private final String[] parameterNames;
	private final ParameterValue[] parameterValues;
	private final List<PropertyValue> propertyValues = new ArrayList(16);

	public DeferredConstruction (Constructor constructor, String[] parameterNames) {
		this.constructor = constructor;
		this.parameterNames = parameterNames;
		parameterValues = new ParameterValue[parameterNames.length];
	}

	public Object construct () throws InvocationTargetException {
		try {
			Object[] parameters = new Object[parameterValues.length];
			int i = 0;
			boolean missingParameter = false;
			for (ParameterValue parameter : parameterValues) {
				if (parameter == null)
					missingParameter = true;
				else
					parameters[i++] = parameter.value;
			}
			Object object;
			if (missingParameter) {
				try {
					object = constructor.getDeclaringClass().getConstructor().newInstance();
				} catch (Exception ex) {
					throw new InvocationTargetException(new AnterosYamlException("Missing constructor property: " + parameterNames[i]));
				}
			} else object = constructor.newInstance(parameters);
			for (PropertyValue propertyValue : propertyValues) {
				if (propertyValue.value != null)
					propertyValue.property.set(object, propertyValue.value);
			}
			return object;
		} catch (Exception ex) {
			throw new InvocationTargetException(ex, "Error constructing instance of class: "
				+ constructor.getDeclaringClass().getName());
		}
	}

	public void storeProperty (Property property, Object value) {
		int index = 0;
		for (String name : parameterNames) {
			if (property.getName().equals(name)) {
				ParameterValue parameterValue = new ParameterValue();
				parameterValue.value = value;
				parameterValues[index] = parameterValue;
				return;
			}
			index++;
		}

		PropertyValue propertyValue = new PropertyValue();
		propertyValue.property = property;
		propertyValue.value = value;
		propertyValues.add(propertyValue);
	}

	public boolean hasParameter (String name) {
		for (String s : parameterNames)
			if (s.equals(name)) return true;
		return false;
	}

	static class PropertyValue {
		Property property;
		Object value;
	}

	static class ParameterValue {
		Object value;
	}
}
