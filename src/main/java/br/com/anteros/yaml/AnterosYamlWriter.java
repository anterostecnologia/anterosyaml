package br.com.anteros.yaml;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.anteros.yaml.Beans.Property;
import br.com.anteros.yaml.AnterosYamlConfig.WriteClassName;
import br.com.anteros.yaml.AnterosYamlConfig.WriteConfig;
import br.com.anteros.yaml.document.YamlElement;
import br.com.anteros.yaml.emitter.Emitter;
import br.com.anteros.yaml.emitter.EmitterException;
import br.com.anteros.yaml.parser.AliasEvent;
import br.com.anteros.yaml.parser.DocumentEndEvent;
import br.com.anteros.yaml.parser.DocumentStartEvent;
import br.com.anteros.yaml.parser.Event;
import br.com.anteros.yaml.parser.MappingStartEvent;
import br.com.anteros.yaml.parser.ScalarEvent;
import br.com.anteros.yaml.parser.SequenceStartEvent;
import br.com.anteros.yaml.scalar.ScalarSerializer;

import java.util.Set;

/** Serializes Java objects as YAML.
  */
public class AnterosYamlWriter {
	private final AnterosYamlConfig config;
	private final Emitter emitter;
	private boolean started;

	private Map<Class, Object> defaultValuePrototypes = new IdentityHashMap();

	private final List queuedObjects = new ArrayList();
	private final Map<Object, Integer> referenceCount = new IdentityHashMap();
	private final Map<Object, String> anchoredObjects = new HashMap();
	private int nextAnchor = 1;
	private boolean isRoot;

	public AnterosYamlWriter (Writer writer) {
		this(writer, new AnterosYamlConfig());
	}

	public AnterosYamlWriter (Writer writer, AnterosYamlConfig config) {
		this.config = config;
		emitter = new Emitter(writer, config.writeConfig.emitterConfig);
	}

	public void setAlias (Object object, String alias) {
		anchoredObjects.put(object, alias);
	}

	public void write (Object object) throws AnterosYamlException {
		if (config.writeConfig.autoAnchor) {
			countObjectReferences(object);
			queuedObjects.add(object);
			return;
		}
		writeInternal(object);
	}

	public AnterosYamlConfig getConfig () {
		return config;
	}

	private void writeInternal (Object object) throws AnterosYamlException {
		try {
			if (!started) {
				emitter.emit(Event.STREAM_START);
				started = true;
			}
			emitter.emit(new DocumentStartEvent(config.writeConfig.explicitFirstDocument, null, null));
			isRoot = true;
			writeValue(object, config.writeConfig.writeRootTags ? null : object.getClass(), null, null);
			emitter.emit(new DocumentEndEvent(config.writeConfig.explicitEndDocument));
		} catch (EmitterException ex) {
			throw new AnterosYamlException("Error writing YAML.", ex);
		} catch (IOException ex) {
			throw new AnterosYamlException("Error writing YAML.", ex);
		}
	}

	/** Returns the YAML emitter, which allows the YAML output to be configured. */
	public Emitter getEmitter () {
		return emitter;
	}

	/** Writes any buffered objects, then resets the list of anchored objects.
	 * @see WriteConfig#setAutoAnchor(boolean) */
	public void clearAnchors () throws AnterosYamlException {
		for (Object object : queuedObjects)
			writeInternal(object);
		queuedObjects.clear();
		referenceCount.clear();
		nextAnchor = 1;
	}

	/** Finishes writing any buffered output and releases all resources.
	 * @throws AnterosYamlException If the buffered output could not be written or the writer could not be closed. */
	public void close () throws AnterosYamlException {
		clearAnchors();
		defaultValuePrototypes.clear();
		try {
			emitter.emit(Event.STREAM_END);
			emitter.close();
		} catch (EmitterException ex) {
			throw new AnterosYamlException(ex);
		} catch (IOException ex) {
			throw new AnterosYamlException(ex);
		}
	}

	private void writeValue (Object object, Class fieldClass, Class elementType, Class defaultType)
		throws EmitterException, IOException, AnterosYamlException {
		boolean isRoot = this.isRoot;
		this.isRoot = false;

		if (object instanceof YamlElement) {
			((YamlElement)object).emitEvent(emitter, config.writeConfig);
			return;
		} else if (object == null) {
			emitter.emit(new ScalarEvent(null, null, new boolean[] {true, true}, null, (char)0));
			return;
		}

		Class valueClass = object.getClass();
		boolean unknownType = fieldClass == null;
		if (unknownType) fieldClass = valueClass;

		if (object instanceof Enum) {
			emitter.emit(
				new ScalarEvent(null, null, new boolean[] {true, true}, ((Enum)object).name(), this.config.writeConfig.quote.c));
			return;
		}

		String anchor = null;
		if (!Beans.isScalar(valueClass)) {
			anchor = anchoredObjects.get(object);
			if (config.writeConfig.autoAnchor) {
				Integer count = referenceCount.get(object);
				if (count == null) {
					emitter.emit(new AliasEvent(anchoredObjects.get(object)));
					return;
				}
				if (count > 1) {
					referenceCount.remove(object);
					if (anchor == null) {
						anchor = String.valueOf(nextAnchor++);
						anchoredObjects.put(object, anchor);
					}
				}
			}
		}

		String tag = null;
		boolean showTag = false;
		if ((unknownType || valueClass != fieldClass || config.writeConfig.writeClassName == WriteClassName.ALWAYS)
			&& config.writeConfig.writeClassName != WriteClassName.NEVER) {
			showTag = true;
			if ((unknownType || fieldClass == List.class) && valueClass == ArrayList.class) showTag = false;
			if ((unknownType || fieldClass == Map.class) && valueClass == HashMap.class) showTag = false;
			if (fieldClass == Set.class && valueClass == HashSet.class) showTag = false;
			if (valueClass == defaultType) showTag = false;
			if (showTag) {
				tag = config.classNameToTag.get(valueClass.getName());
				if (tag == null) tag = valueClass.getName();
			}
		}

		for (Entry<Class, ScalarSerializer> entry : config.scalarSerializers.entrySet()) {
			if (entry.getKey().isAssignableFrom(valueClass)) {
				ScalarSerializer serializer = entry.getValue();
				emitter.emit(new ScalarEvent(null, tag, new boolean[] {tag == null, tag == null}, serializer.write(object), (char)0));
				return;
			}
		}

		if (Beans.isScalar(valueClass)) {
			char style = 0;
			String string = String.valueOf(object);
			if (valueClass == String.class) {
				try {
					Float.parseFloat(string);
					style = this.config.writeConfig.quote.c;
				} catch (NumberFormatException ignored) {
				}
			}
			emitter.emit(new ScalarEvent(null, tag, new boolean[] {true, true}, string, style));
			return;
		}

		if (object instanceof Collection) {
			emitter.emit(new SequenceStartEvent(anchor, tag, !showTag, false));
			for (Object item : (Collection)object) {
				if (isRoot && !config.writeConfig.writeRootElementTags) elementType = item.getClass();
				writeValue(item, elementType, null, null);
			}
			emitter.emit(Event.SEQUENCE_END);
			return;
		}

		if (object instanceof Map) {
			emitter.emit(new MappingStartEvent(anchor, tag, !showTag, false));
			Map map = (Map)object;
			for (Object item : map.entrySet()) {
				Entry entry = (Entry)item;
				Object key = entry.getKey(), value = entry.getValue();
				if (isRoot && !config.writeConfig.writeRootElementTags) elementType = value.getClass();
				if (config.tagSuffix != null && key instanceof String) {
					// Skip tag keys.
					if (((String)key).endsWith(config.tagSuffix)) continue;

					// Write value with tag, if found.
					if (value instanceof String) {
						Object valueTag = map.get(key + config.tagSuffix);
						if (valueTag instanceof String) {
							String string = (String)value;
							char style = 0;
							try {
								Float.parseFloat(string);
								style = this.config.writeConfig.quote.c;
							} catch (NumberFormatException ignored) {
							}
							writeValue(key, null, null, null);
							emitter.emit(new ScalarEvent(null, (String)valueTag, new boolean[] {false, false}, string, style));
							continue;
						}
					}
				}
				writeValue(key, null, null, null);
				writeValue(value, elementType, null, null);
			}
			emitter.emit(Event.MAPPING_END);
			return;
		}

		if (fieldClass.isArray()) {
			elementType = fieldClass.getComponentType();
			emitter.emit(new SequenceStartEvent(anchor, null, true, false));
			for (int i = 0, n = Array.getLength(object); i < n; i++)
				writeValue(Array.get(object, i), elementType, null, null);
			emitter.emit(Event.SEQUENCE_END);
			return;
		}

		// Value must be a bean.

		Object prototype = null;
		if (!config.writeConfig.writeDefaultValues && valueClass != Class.class) {
			prototype = defaultValuePrototypes.get(valueClass);
			if (prototype == null && Beans.getDeferredConstruction(valueClass, config) == null) {
				try {
					prototype = Beans.createObject(valueClass, config.privateConstructors);
				} catch (InvocationTargetException ex) {
					throw new AnterosYamlException("Error creating object prototype to determine default values.", ex);
				}
				defaultValuePrototypes.put(valueClass, prototype);
			}
		}

		Set<Property> properties = Beans.getProperties(valueClass, config.beanProperties, config.privateFields, config);
		emitter.emit(new MappingStartEvent(anchor, tag, !showTag, false));
		for (Property property : properties) {
			try {
				Object propertyValue = property.get(object);
				if (prototype != null) {
					// Don't output properties that have the default value for the prototype.
					Object prototypeValue = property.get(prototype);
					if (propertyValue == null && prototypeValue == null) continue;
					if (propertyValue != null && prototypeValue != null && prototypeValue.equals(propertyValue)) continue;
				}
				emitter.emit(
					new ScalarEvent(null, null, new boolean[] {true, true}, property.getName(), this.config.writeConfig.quote.c));
				Class propertyElementType = config.propertyToElementType.get(property);
				Class propertyDefaultType = config.propertyToDefaultType.get(property);
				writeValue(propertyValue, property.getType(), propertyElementType, propertyDefaultType);
			} catch (Exception ex) {
				throw new AnterosYamlException("Error getting property '" + property + "' on class: " + valueClass.getName(), ex);
			}
		}
		emitter.emit(Event.MAPPING_END);
	}

	private void countObjectReferences (Object object) throws AnterosYamlException {
		if (object == null || Beans.isScalar(object.getClass())) return;

		// Count every reference to the object, but follow its own references the first time it is encountered.
		Integer count = referenceCount.get(object);
		if (count != null) {
			referenceCount.put(object, count + 1);
			return;
		}
		referenceCount.put(object, 1);

		if (object instanceof Collection) {
			for (Object item : (Collection)object)
				countObjectReferences(item);
			return;
		}

		if (object instanceof Map) {
			for (Object value : ((Map)object).values())
				countObjectReferences(value);
			return;
		}

		if (object.getClass().isArray()) {
			for (int i = 0, n = Array.getLength(object); i < n; i++)
				countObjectReferences(Array.get(object, i));
			return;
		}

		// Value must be an object.

		Set<Property> properties = Beans.getProperties(object.getClass(), config.beanProperties, config.privateFields, config);
		for (Property property : properties) {
			if (Beans.isScalar(property.getType())) continue;
			Object propertyValue;
			try {
				propertyValue = property.get(object);
			} catch (Exception ex) {
				throw new AnterosYamlException("Error getting property '" + property + "' on class: " + object.getClass().getName(), ex);
			}
			countObjectReferences(propertyValue);
		}
	}
}
