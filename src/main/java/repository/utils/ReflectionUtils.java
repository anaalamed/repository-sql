package repository.utils;

import com.google.gson.Gson;

import repository.FieldType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

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


    public static <T> Map<String, String> getMapKeysValuesOfObject(T object) {
        List<Field> fields = ReflectionUtils.getClassFields(object.getClass());
        HashMap<String, String> map = new HashMap<>();

        for (Field field : fields) {
            map.put(field.getName(), extractFieldValue(object, field));
        }

        return map;
    }

    public static <T> String createKeysStringForQuery(T object) {
        Map<String, String> mapKeysValues = ReflectionUtils.getMapKeysValuesOfObject(object);

        String keysStr = String.join(", ", mapKeysValues.keySet());
        return " (" + keysStr + ") ";
    }

    public static <T> String createValuesStringForQuery(T object) {
        Map<String, String> mapKeysValues = ReflectionUtils.getMapKeysValuesOfObject(object);

        String valuesStr = String.join(", ", mapKeysValues.values());
        return " (" + valuesStr + ") ";
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