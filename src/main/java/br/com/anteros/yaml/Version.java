package br.com.anteros.yaml;

/** Represents the version of a YAML document.*/
public class Version {
	
	public static final Version DEFAULT_VERSION = new Version(1, 0);
	
	public final int major;
	public final int minor;

	public Version (String value) {
		if (value == null) throw new IllegalArgumentException("value cannot be null.");
		int dotIndex = value.indexOf('.');
		if (dotIndex == -1) throw new IllegalArgumentException("value must contain a period: " + value);
		major = Integer.parseInt(value.substring(0, dotIndex));
		minor = Integer.parseInt(value.substring(dotIndex + 1));
	}

	public Version (int major, int minor) {
		this.major = major;
		this.minor = minor;
	}

	public String toString () {
		return major + "." + minor;
	}

	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
		return result;
	}

	public boolean equals (int minor, int major) {
		return this.minor == minor && this.major == major;
	}

	public boolean equals (Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Version other = (Version)obj;
		if (major != other.major) return false;
		if (minor != other.minor) return false;
		return true;
	}
}
