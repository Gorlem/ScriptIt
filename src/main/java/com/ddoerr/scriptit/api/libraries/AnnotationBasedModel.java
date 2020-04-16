package com.ddoerr.scriptit.api.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.annotations.Setter;
import com.ddoerr.scriptit.api.languages.ContainedResultFactory;
import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.exceptions.ConversionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AnnotationBasedModel implements Model {
    private Map<String, Method> getterMethods = new HashMap<>();
    private Map<String, Field> getterFields = new HashMap<>();
    private Map<String, Method> setterMethods = new HashMap<>();
    private Map<String, Field> setterFields = new HashMap<>();
    private Map<String, List<Method>> functionMethods = new HashMap<>();

    public AnnotationBasedModel() {
        parse();
    }

    public void parse() {
        Class<?> modelClass = this.getClass();
        List<Class<?>> classes = new ArrayList<>();

        while (modelClass != null) {
            classes.add(modelClass);
            Collections.addAll(classes, modelClass.getInterfaces());
            modelClass = modelClass.getSuperclass();
        }

        List<Method> methods = classes.stream()
                .flatMap(c -> Arrays.stream(c.getMethods()))
                .collect(Collectors.toList());

        for (Method method : methods) {
            if (method.isAnnotationPresent(Callable.class)) {
                String name = method.getName();
                if (!functionMethods.containsKey(name)) {
                    functionMethods.put(name, new ArrayList<>());
                }
                functionMethods.get(name).add(method);
            }

            if (method.isAnnotationPresent(Getter.class)) {
                Type[] parameterTypes = method.getGenericParameterTypes();
                Type returnType = method.getGenericReturnType();
                if (parameterTypes.length != 0 || returnType == Void.TYPE) {
                    throw new IllegalArgumentException("Getter has to have no parameter (got " + parameterTypes.length + ") and a return type (got " + returnType.getTypeName() + ")");
                }

                String name = method.getName();
                if (!name.startsWith("get")) {
                    throw new IllegalArgumentException("Getter name has to start with `get`");
                }
                getterMethods.put(name.substring(3, 4).toLowerCase() + name.substring(4), method);
            }

            if (method.isAnnotationPresent(Setter.class)) {
                Type returnType = method.getGenericReturnType();
                Type[] parameterTypes = method.getGenericParameterTypes();

                if (returnType != Void.TYPE || parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Setter has to have 1 parameter (got " + parameterTypes.length + ") and no return type (got " + returnType.getTypeName() + ")");
                }

                String name = method.getName();
                if (!name.startsWith("set")) {
                    throw new IllegalArgumentException("Setter name has to start with `set`");
                }
                setterMethods.put(name.substring(3, 4).toLowerCase() + name.substring(4), method);
            }
        }

        List<Field> fields = classes.stream()
                .flatMap(c -> Arrays.stream(c.getFields()))
                .collect(Collectors.toList());

        for (Field field : fields) {
            if (field.isAnnotationPresent(Getter.class)) {
                getterFields.put(field.getName(), field);
            }

            if (field.isAnnotationPresent(Setter.class)) {
                setterFields.put(field.getName(), field);
            }
        }
    }

    @Override
    public boolean hasGetter(String key) {
        return getterMethods.containsKey(key) || getterFields.containsKey(key);
    }

    @Override
    public boolean hasSetter(String key) {
        return setterMethods.containsKey(key) || setterFields.containsKey(key);
    }

    @Override
    public boolean hasFunction(String key) {
        return functionMethods.containsKey(key);
    }

    @Override
    public <T> T runGetter(String key, ContainedResultFactory<T> factory) {
        if (getterMethods.containsKey(key)) {
            Method method = getterMethods.get(key);
            Type returnType = method.getGenericReturnType();
            try {
                return factory.from(returnType, method.invoke(this));
            } catch (IllegalAccessException | InvocationTargetException | ConversionException e) {
                e.printStackTrace();
                return factory.nullValue();
            }
        } else if (getterFields.containsKey(key)) {
            Field field = getterFields.get(key);
            Type type = field.getType();
            try {
                return factory.from(type, field.get(this));
            } catch (IllegalAccessException | ConversionException e) {
                e.printStackTrace();
                return factory.nullValue();
            }
        }
        throw new IllegalArgumentException("No getter for " + key);
    }

    @Override
    public void runSetter(String key, ContainedValue value) {
        if (setterMethods.containsKey(key)) {
            Method method = setterMethods.get(key);
            Type type = method.getGenericParameterTypes()[0];
            try {
                method.invoke(this, value.to(type));
            } catch (IllegalAccessException | InvocationTargetException | ConversionException e) {
                e.printStackTrace();
            }
            return;
        } else if (setterFields.containsKey(key)) {
            Field field = setterFields.get(key);
            Type type = field.getType();
            try {
                field.set(this, value.to(type));
            } catch (IllegalAccessException | ConversionException e) {
                e.printStackTrace();
            }
            return;
        }
        throw new IllegalArgumentException("No setter for " + key);
    }

    @Override
    public <T> T runFunction(String key, ContainedValue[] values, ContainedResultFactory<T> factory) {
        List<Method> methods = functionMethods.get(key);
        List<Object> parameters = new ArrayList<>();
        Method callableMethod = null;

        for (Method method : methods) {
            try {
                Type[] parameterTypes = method.getGenericParameterTypes();

                if (parameterTypes.length != values.length) {
                    continue;
                }

                for (int i = 0; i < parameterTypes.length; i++) {
                    Type type = parameterTypes[i];
                    parameters.add(values[i].to(type));
                }
            } catch (ConversionException e) {
                parameters.clear();
                continue;
            }
            callableMethod = method;
            break;
        }

        if (callableMethod == null) {
            throw new IllegalArgumentException("No overload of function " + key + " found with given parameters");
        }

        Type returnType = callableMethod.getGenericReturnType();

        try {
            return factory.from(returnType, callableMethod.invoke(this, parameters.toArray()));
        } catch (IllegalAccessException | InvocationTargetException | ConversionException e) {
            e.printStackTrace();
            return factory.nullValue();
        }
    }
}
