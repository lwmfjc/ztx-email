package com.lyclass;

import sun.plugin2.message.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ClassTest {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("name", "123");
        maps.put("age1", "23");
        Object pageByVm = ClassTest.findPageByVm(maps, LyEntity.class);
        System.out.println(pageByVm);

    }

    public static Object findPageByVm(HashMap<String, String> hashMap, Class c) throws IllegalAccessException, InstantiationException {
        Object t = c.newInstance();

        Class<?> curClass = t.getClass();
        Class<?> superclass = t.getClass().getSuperclass();
        Field[] fieldsCur = curClass.getDeclaredFields();
        Field[] fieldsSuper = superclass.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(fieldsCur));
        fields.addAll(Arrays.asList(fieldsSuper));


        Set<Map.Entry<String, String>> entries = hashMap.entrySet();//获取所有的参数名
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();//遍历key
            String filedName = entry.getKey();

            for (Field field : fields) {
                if (field.getName().equals(filedName)) {
                    field.setAccessible(true);
                    String value = entry.getValue();
                    Object valueFinal = castToFinalClass(field, value);
                    field.set(t, valueFinal);
                    break;
                }
            }
        }
        return t;
    }

    private static Object castToFinalClass(Field field,String value){
        Class<?> type = field.getType();
        Object valueFinal = value;
        if (type.getName().endsWith("int") || type.getName().endsWith("Integer")) {
            valueFinal = Integer.parseInt(value);
        }
        return valueFinal;
    }
}
