package persistence.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class EntityDiffAnalyzer {
    private Logger logger;

    private EntityDiffAnalyzer(Logger logger) {
        this.logger = logger;
    }

    public static <T> EntityDiffAnalyzer newInstance(Class<T> type) {
        return new EntityDiffAnalyzer(Logger.getLogger(type));
    }

    public void trace(String message) {
        if (logger.isTraceEnabled()) {
            if (message != null && !message.isEmpty()) logger.trace(message);
        }
    }

    public <E> void trace(String message, E entity) {
        if (logger.isTraceEnabled()) {
            logger.trace(message + entity);
        }
    }

    public <E> String diffByMethod(E leftEntity, E rightEntity, String... excludedNames) {
        Map<String, Method> rightEntityMethods = toMapOfMethods(rightEntity);
        StringBuilder builder = new StringBuilder();
        Method[] leftEntityMethods = leftEntity.getClass()
                .getDeclaredMethods();
        for (Method leftEntityMethod : leftEntityMethods) {
            if (isDiffable(leftEntityMethod, excludedNames)) {
                Method rightEntityMethod = rightEntityMethods.get(leftEntityMethod.getName());
                Object leftEntityObject = getObject(leftEntity, leftEntityMethod);
                Object rightEntityObject = getObject(rightEntity, rightEntityMethod);
                diff(builder, leftEntityMethod.getName(), leftEntityObject, rightEntityObject);
            }
        }
        return diff(builder, leftEntity);
    }

    public <E> String diffByField(E leftEntity, E rightEntity, String... excludedNames) {
        Map<String, Field> rightEntityFields = toMapOfFields(rightEntity);
        StringBuilder builder = new StringBuilder();
        Field[] leftEntityFields = leftEntity.getClass()
                .getDeclaredFields();
        for (Field leftEntityField : leftEntityFields) {
            if (isDiffable(leftEntityField, excludedNames)) {
                Field rightEntityField = rightEntityFields.get(leftEntityField.getName());
                Object leftEntityObject = getObject(leftEntity, leftEntityField);
                Object rightEntityObject = getObject(rightEntity, rightEntityField);
                diff(builder, leftEntityField.getName(), leftEntityObject, rightEntityObject);
            }
        }
        return diff(builder, leftEntity);
    }

    public <E> String diff(E leftEntity, E rightEntity, String... excludedNames) {
        return diffByField(leftEntity, rightEntity, excludedNames);
    }

    private void diff(StringBuilder builder, String entityName, Object leftEntityObject, Object rightEntityObject) {
        if (!(leftEntityObject instanceof Boolean) &&
                !(leftEntityObject instanceof Number) &&
                !(leftEntityObject instanceof String) &&
                !(leftEntityObject instanceof Date) &&
                !(leftEntityObject instanceof Collection) &&
                (leftEntityObject != null)) {
            builder.append(diff(leftEntityObject, rightEntityObject));
        } else if (getHashCode(leftEntityObject) != getHashCode(rightEntityObject)) {
            String leftEntityObjectAsString = toString(leftEntityObject);
            String rightEntityObjectAsString = toString(rightEntityObject);
            builder.append("\n + ");
            builder.append(entityName);
            builder.append("\n   - left: ");
            builder.append(diff(leftEntityObjectAsString, rightEntityObjectAsString));
            builder.append("\n   - right: ");
            builder.append(diff(rightEntityObjectAsString, leftEntityObjectAsString));
        }
    }

    private String diff(StringBuilder builder, Object leftEntity) {
        String diff = builder.toString();
        if (diff.length() > 0) {
            String className = leftEntity.getClass()
                    .getSimpleName();
            StringBuilder header = new StringBuilder();
            header.append("\n[");
            header.append(className);
            header.append(diff);
            header.append("\n");
            header.append(className);
            header.append("]");
            diff = header.toString();
        }
        return diff;
    }

    private String diff(String leftString, String rightString) {
        String diff = StringUtils.difference(leftString, rightString);
        return StringUtils.strip(diff, "]");
    }

    private Map<String, Method> toMapOfMethods(Object rightEntity) {
        Map<String, Method> mapOfMethods = new HashMap<String, Method>();
        if (rightEntity != null) {
            Method[] methods = rightEntity.getClass()
                    .getDeclaredMethods();
            for (Method method : methods) {
                mapOfMethods.put(method.getName(), method);
            }
        }
        return mapOfMethods;
    }

    private Map<String, Field> toMapOfFields(Object rightEntity) {
        Map<String, Field> mapOfFields = new HashMap<String, Field>();
        if (rightEntity != null) {
            Field[] fields = rightEntity.getClass()
                    .getDeclaredFields();
            for (Field field : fields) {
                mapOfFields.put(field.getName(), field);
            }
        }
        return mapOfFields;
    }

    private boolean isDiffable(Method method, String... excludedNames) {
        boolean isDiffable = false;
        if (method != null
                && !isExcludedName(method.getName(), excludedNames)
                && method.getName()
                .startsWith("get")) {
            int modifiers = method.getModifiers();
            isDiffable = !Modifier.isStatic(modifiers);
            if (method.getAnnotation(XmlTransient.class) != null) {
                isDiffable = false;
            }
        }
        return isDiffable;
    }

    private boolean isDiffable(Field field, String... excludedNames) {
        boolean isDiffable = false;
        if (field != null && !isExcludedName(field.getName(), excludedNames)) {
            int modifiers = field.getModifiers();
            isDiffable = !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
            if (field.getAnnotation(XmlTransient.class) != null) {
                isDiffable = false;
            }
        }
        return isDiffable;
    }

    private boolean isExcludedName(String fieldName, String... excludedNames) {
        boolean isExcludedName = false;
        for (String name : excludedNames) {
            if (name.equals(fieldName)) {
                isExcludedName = true;
                break;
            }
        }
        return isExcludedName;
    }

    private Object getObject(Object owner, Method method) {
        Object object = null;
        try {
            if (owner != null && method != null) {
                method.setAccessible(true);
                object = method.invoke(owner);
            }
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        } catch (IllegalArgumentException ignore) {
        }
        return object;
    }

    private Object getObject(Object owner, Field field) {
        Object object = null;
        try {
            if (owner != null && field != null) {
                field.setAccessible(true);
                object = field.get(owner);
            }
        } catch (IllegalAccessException ignore) {
        }
        return object;
    }

    private int getHashCode(Object object) {
        return (object == null) ? 1 : object.hashCode();
    }

    private String toString(Object nullableEntity) {
        String toString = null;
        if (nullableEntity != null) {
            toString = nullableEntity.toString();
        }
        if (toString != null && toString.isEmpty()) {
            toString = "[zero-length string]";
        }
        if (toString != null && StringUtils.isBlank(toString)) {
            toString = "[blank string]";
        }
        return (toString == null) ? "null" : toString;
    }
}