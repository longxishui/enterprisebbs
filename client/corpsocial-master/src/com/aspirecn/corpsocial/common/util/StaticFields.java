/**
 *
 */
package com.aspirecn.corpsocial.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * This class can be used to parse other classes containing constant
 * definitions.
 * <p/>
 * The class <code>StaticFields</code>
 *
 * @author lizhuo_a
 * @version 1.0
 */
public class StaticFields {

    private final Map<String, Object> fieldCache = new HashMap<String, Object>();
    private String className;

    @SuppressWarnings("rawtypes")
    public StaticFields(Class... clazzs) {
        for (Class clazz : clazzs) {
            this.className = clazz.getName();
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                if (isStaticModifiers(field.getModifiers())) {
                    String name = field.getName();
                    try {
                        Object value = field.get(null);
                        this.fieldCache.put(name, value);
                    } catch (IllegalAccessException ex) {
                        // ignore it
                    }
                }
            }
        }
    }

    private boolean isStaticModifiers(int modifiers) {
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)
                && Modifier.isFinal(modifiers);
    }

    public String getClassName() {
        return className;
    }

    public int getSize() {
        return fieldCache.keySet().size();
    }

    public Map<String, Object> getFieldCache() {
        return fieldCache;
    }

    public Object getObject(String key) {
        return fieldCache.get(key);
    }

    public String getString(String key) {
        return getObject(key).toString();
    }

    public Integer getNumber(String key) {
        return getValue(key, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> requiredType) {
        Object value = getObject(key);
        if (requiredType.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
}
