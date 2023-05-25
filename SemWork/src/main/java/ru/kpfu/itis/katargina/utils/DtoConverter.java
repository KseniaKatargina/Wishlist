package ru.kpfu.itis.katargina.utils;

import ru.kpfu.itis.katargina.dto.WishlistDto;
import ru.kpfu.itis.katargina.models.Wishlist;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {

    public static <T, D> D toDto(T entity, Class<D> dtoClass) {
        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();

            Field[] entityFields = entity.getClass().getDeclaredFields();
            Field[] dtoFields = dtoClass.getDeclaredFields();

            for (Field dtoField : dtoFields) {
                Field entityField = findMatchingFieldWithDto(dtoField, entityFields);
                if (entityField != null) {
                    entityField.setAccessible(true);
                    dtoField.setAccessible(true);
                    if (dtoField.getName().endsWith("Id")) {
                        Method getIdMethod = entityField.getType().getMethod("getId");
                        Long id = (Long) getIdMethod.invoke(entityField.get(entity));
                        dtoField.set(dto, id);
                    } else if (dtoField.getName().endsWith("Ids")) {
                        List<Long> ids = ((List<?>) entityField.get(entity)).stream()
                                .map(e -> {
                                    try {
                                        Method getIdMethod = e.getClass().getMethod("getId");
                                        return (Long) getIdMethod.invoke(e);
                                    } catch (NoSuchMethodException | IllegalAccessException |
                                             InvocationTargetException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                })
                                .collect(Collectors.toList());
                        dtoField.set(dto, ids);
                    } else {
                        dtoField.set(dto, entityField.get(entity));
                    }
                }
            }

            return dto;
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T, D> T fromDto(D dto, Class<T> entityClass) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();

            Field[] entityFields = entityClass.getDeclaredFields();
            Field[] dtoFields = dto.getClass().getDeclaredFields();

            for (Field entityField : entityFields) {
                Field dtoField = findMatchingField(entityField, dtoFields);
                if (dtoField != null) {
                    entityField.setAccessible(true);
                    dtoField.setAccessible(true);
                    if (dtoField.getName().endsWith("Id")) {
                        Constructor<?>[] entityListClass = entityField.getType().getConstructors();
                        Constructor<?> idConstructor = null;
                        for (Constructor constructor : entityListClass){
                            if(constructor.getParameterTypes().length == 1){
                                idConstructor = constructor;
                            }
                        }
                        Long id = (Long) dtoField.get(dto);
                        Object entityValue = idConstructor.newInstance(id);
                        entityField.set(entity, entityValue);
                    } else if (dtoField.getName().endsWith("Ids")) {
                        List<Object> entityList = ((java.util.List<Long>) dtoField.get(dto)).stream()
                                .map(id -> {
                                    try {

                                        Class<?> someClass = Class.forName(entityField.getGenericType().getTypeName().replace("java.util.List<", "").replace(">", ""));
                                        Object entityListObj = someClass.getDeclaredConstructor().newInstance();
                                        java.lang.reflect.Method setIdMethod = someClass.getMethod("setId", Long.class);
                                        setIdMethod.invoke(entityListObj, id);
                                        return entityListObj;
                                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                                             java.lang.reflect.InvocationTargetException | ClassNotFoundException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                })
                                .collect(Collectors.toList());
                        entityField.set(entity, entityList);
                    } else {
                        entityField.set(entity, dtoField.get(dto));
                    }
                }
            }

            return entity;
        } catch (InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, D> List<D> toDtoList(List<T> entityList, Class<D> dtoClass) {
        return entityList.stream()
                .map(entity -> toDto(entity, dtoClass))
                .collect(Collectors.toList());
    }


    public static <T, D> List<T> fromDtoList(List<D> dtoList, Class<T> entityClass) {
        return dtoList.stream()
                .map(dto -> fromDto(dto, entityClass))
                .collect(Collectors.toList());
    }

    private static Field findMatchingField(Field entityField, Field[] dtoFields) {
        for (Field dtoField : dtoFields) {
            if (entityField.getName().equalsIgnoreCase(dtoField.getName())) {
                if (entityField.getType().equals(dtoField.getType())) {
                    return dtoField;
                }
            } else if (dtoField.getName().endsWith("Id") &&
                    entityField.getName().equalsIgnoreCase(dtoField.getName().replace("Id", "")) &&
                    dtoField.getType().equals(Long.class)) {
                return dtoField;
            } else if (dtoField.getName().endsWith("Ids") &&
                    entityField.getName().equalsIgnoreCase(dtoField.getName().replace("Ids", "") + "s")) {
                return dtoField;
            }
        }
        return null;
    }

    private static Field findMatchingFieldWithDto(Field dtoField, Field[] entityFields) {
        for (Field entityField : entityFields) {
            if (entityField.getName().equalsIgnoreCase(dtoField.getName())) {
                if (entityField.getType().equals(dtoField.getType())) {
                    return entityField;
                }
            } else if (dtoField.getName().endsWith("Id") &&
                    entityField.getName().equalsIgnoreCase(dtoField.getName().replace("Id", "")) &&
                    dtoField.getType().equals(Long.class)) {
                return entityField;
            } else if (dtoField.getName().endsWith("Ids") &&
                    entityField.getName().equalsIgnoreCase(dtoField.getName().replace("Ids", "") + "s")) {
                return entityField;
            }
        }
        return null;
    }
}
