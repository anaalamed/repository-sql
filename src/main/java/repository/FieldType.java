package repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public enum FieldType {
    INT("INT", 16, 0),
    INTEGER("INT", 16, 0),
    FLOAT("FLOAT", 16, 2),
    DOUBLE("DOUBLE", 16, 9),
    BOOLEAN("BOOLEAN", 0, 0),
    STRING("VARCHAR", 50, 0),
    LIST("VARCHAR", 1000, 0),
    MAP("VARCHAR", 1000, 0),
    OBJECT("VARCHAR", 1000, 0);

    private final String text;
    private final int size;
    private final int d;

    FieldType(final String text, int size, int d) {
        this.text = text;
        this.size = size;
        this.d = d;
    }
    @Override
    public String toString() {
        String result = text;
        if (size > 0 && d > 0) {
            result += String.format("(%d, %d)", size, d);
        } else if (size > 0) {
            result += String.format("(%d)", size);
        }

        return result;
    }

    public static boolean isBoxedPrimitive(Type type) {
        List<Type> boxedPrimitives = new ArrayList<>();
        boxedPrimitives.add(Integer.TYPE);
        boxedPrimitives.add(Double.TYPE);
        boxedPrimitives.add(Boolean.TYPE);
        boxedPrimitives.add(Float.TYPE);

        return boxedPrimitives.contains(type);
    }
}
