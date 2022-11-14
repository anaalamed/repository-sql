package repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
}
