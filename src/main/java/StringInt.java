import edu.desu.utilities.UtilityMethodsString;

/**
 * A wrapper class that acts like a String but uses numeric comparison
 * (via UtilityMethodsString.compareNumericStrings) as its default ordering.
 */
public class StringInt implements Comparable<StringInt>, CharSequence {

    private final String value;

    public StringInt() {
        this.value = "";
    }

    public StringInt(String value) {
        this.value = (value != null) ? value : "";
    }

    public StringInt(int value) {
        this.value = String.valueOf(value);
    }

    public StringInt(Object value) {
        this.value = (value != null) ? String.valueOf(value) : "";
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(StringInt other) {
        if (other == null) {
            return 1;
        }
        return UtilityMethodsString.compareNumericStrings(this.value, other.value);
    }

    public int compareTo(String other) {
        if (other == null) {
            return 1;
        }
        return UtilityMethodsString.compareNumericStrings(this.value, other);
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (o instanceof StringInt) {
            return compareTo((StringInt) o) == 0;
        }
        if (o instanceof String) {
            return compareTo((String) o) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value.hashCode();
        }
    }
}
