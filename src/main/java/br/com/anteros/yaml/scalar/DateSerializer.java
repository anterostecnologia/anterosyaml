package br.com.anteros.yaml.scalar;

import java.text.ParseException;
import java.util.Date;

import br.com.anteros.yaml.AnterosYamlException;

public class DateSerializer implements ScalarSerializer<Date> {
	private DateTimeParser dateParser = new DateTimeParser();

	public Date read (String value) throws AnterosYamlException {
		try {
			return dateParser.parse(value);
		} catch (ParseException ex) {
			throw new AnterosYamlException("Invalid date: " + value, ex);
		}
	}

	public String write (Date object) throws AnterosYamlException {
		return dateParser.format(object);
	}
}
