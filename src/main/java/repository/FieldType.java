package repository;

public enum FieldType {
    INT("INT", 16),
    INTEGER("INT", 16),
    FLOAT("FLOAT", 16),
    DOUBLE("DOUBLE", 16),
    BOOLEAN("BOOLEAN", 0),
    STRING("VARCHAR", 20);

    private final String text;
    private int size;

    FieldType(final String text, int size) {
        this.text = text;
        this.size = size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    @Override
    public String toString() {
        String result = (size == 0) ? text : text + "(" + size + ")";
        return result;
    }
}
