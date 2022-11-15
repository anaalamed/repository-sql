package repository.utils;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.FieldType;
import repository.annotations.Constraints;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static repository.FieldType.isBoxedPrimitive;

public class ReflectionUtils {

    private final static Logger logger = LogManager.getLogger(ReflectionUtils.class.getName());

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

            try {
                if (field.getType().isPrimitive() || isBoxedPrimitive(field.getType())) {
                    values.add(field.get(object).toString());
                } else {
                    Gson gson = new Gson();
                    values.add(gson.toJson(field.get(object)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String keysStr = String.join(", ", keys);
        String valuesStr = String.join(", ", values);
        return new String[]{keysStr, valuesStr};

    }


    public static String getAnnotationsFromField(Field field, Map<Constraints, Integer> annotationsCountByType) {

        StringBuilder constraints = new StringBuilder();
        try {
            Annotation[] annotations = field.getAnnotations();

            for (Annotation annotation : annotations) {
                Class<? extends Annotation> type = annotation.annotationType();
                Method[] methods = type.getDeclaredMethods();
                for (Method method : methods) {
                    Object value = method.invoke(annotation, (Object[]) null);

                    if (value != null) {
                        Constraints val =((Constraints) value);
                        if (!annotationsCountByType.containsKey(val)) {
                           annotationsCountByType.put(val,1);
                        }else{
                            annotationsCountByType.put(val,annotationsCountByType.get(val)+1);
                        }
                        constraints.append(value).append(" ");
                    }
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return constraints.toString();
    }
}
