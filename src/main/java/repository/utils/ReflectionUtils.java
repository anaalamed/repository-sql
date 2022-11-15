package repository.utils;

import com.google.gson.Gson;

import repository.FieldType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static repository.FieldType.isBoxedPrimitive;

public class ReflectionUtils {
    public static <T> List<Field> getClassFields(Class<T> clz) {
        List<Field> classFields = new ArrayList<>();

        try {
            Constructor<T> constructor = (Constructor<T>) clz.getConstructor();
            T item = constructor.newInstance();
            Field[] declaredFields = clz.getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                classFields.add(field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classFields;
    }

    public static <T> String parseTableName(Class<T> clz) {
        return clz.getSimpleName().toLowerCase();
    }

    public static String getFieldSQLType(Field field) {
        String fieldTypeValue = field.getType().getSimpleName().toUpperCase();

        boolean isSQLField = Arrays.stream(FieldType.values()).anyMatch((t) -> t.name().equals(fieldTypeValue));
        String result = isSQLField ? FieldType.valueOf(fieldTypeValue).toString() : FieldType.OBJECT.toString();

        return result;
    }

    public static <T> String[] getKeysValuesOfObject(T object) {
        List<Field> fields = ReflectionUtils.getClassFields(object.getClass());
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        for (Field field : fields) {
            keys.add(field.getName());
            values.add(extractFieldValue(object, field));
        }

        String keysStr = String.join(", ", keys);
        String valuesStr = String.join(", ", values);
        return new String[]{keysStr, valuesStr};
    }

    private static <T> String extractFieldValue(T object, Field field) {
        String value = null;

        try {
            if (field.getType().isPrimitive() || isBoxedPrimitive(field.getType())) {
                value = field.get(object).toString();
            } else {
                Gson gson = new Gson();
                value = gson.toJson(field.get(object));

                if (!field.getType().getSimpleName().equals("String")) {
                    value = value.replace("\"", "\\\"").replace("\'", "\\\'");
                    value = String.format("\"%s\"", value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static boolean isComplexObject(Class<?> clz) {
        return (!clz.isPrimitive() && !isBoxedPrimitive(clz) && !clz.getSimpleName().equals("String"));
    }
}