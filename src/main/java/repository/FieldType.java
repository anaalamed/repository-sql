package repository;

public enum FieldType {
    INT("INT"),
    FLOAT("FLOAT"),
    BOOLEAN("BOOLEAN"),
    STRING("VARCHAR");

    private final String text;
    private int size;

    FieldType(final String text) {
        this.text = text;
        this.size = 16;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return text + "(" + size + ")";
    }
}
