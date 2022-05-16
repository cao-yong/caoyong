package com.gerp.stats.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.gerp.stats.common.annotation.CheckParam;
import com.gerp.stats.common.exception.ParameterException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author caoyong
 * @version 1.0.0
 * @since 2022-04-15 09:10:29
 **/
@Slf4j
public class CheckParamsNullUtil {
    /**
     * checking obj fields in args can not be null
     *
     * @param t    the parameter object want to check
     * @param args fields want to check, example: "field1", "field2"
     */
    @SafeVarargs
    public static <T> void check(T t, SFunction<T, ?>... args) {
        if (t == null) {
            throw new ParameterException("parameter present is null");
        }

        if (args == null || args.length == 0) {
            throw new ParameterException("checking args did not present");
        }
        //all args need to check
        Set<String> checkArgs = Arrays.stream(args)
                .map(FunctionFieldUtil::funcFiledNameConvertor)
                .collect(Collectors.toSet());
        //get all fields with reflection
        Field[] fields = ReflectUtil.getFields(t.getClass());
        List<String> errorMessages = Lists.newArrayList();
        Stream.of(fields)
                //using hash set rather than array list for performance
                .filter(field -> checkArgs.contains(field.getName()))
                .forEach(field -> {
                    field.setAccessible(true);
                    String type = "";
                    Object value = null;
                    try {
                        //field type name
                        type = field.getType().getName();
                        //field corresponding value
                        value = field.get(t);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    String message = validateFields(field, type, value);
                    if (null != message) {
                        errorMessages.add(message);
                    }
                });
        if (CollectionUtil.isNotEmpty(errorMessages)) {
            throw new ParameterException(String.join(";", errorMessages));
        }
    }

    /**
     * validate fields
     *
     * @param field field that for class
     * @param type  type of the parameter
     * @param value get field value
     */
    @SuppressWarnings("rawtypes")
    private static String validateFields(Field field, String type, Object value) {
        String errorMsg = field.getName() + " is null";
        CheckParam annotation = AnnotationUtils.getAnnotation(field, CheckParam.class);
        if (annotation != null) {
            errorMsg = annotation.value() + CheckParam.NOT_NULL_CN;
        }
        if (null == value) {
            return errorMsg;
        }
        if (String.class.getName().equals(type) && StringUtils.isBlank(value.toString())) {
            return errorMsg;
        }
        if (List.class.getName().equals(type) || Set.class.getName().equals(type)) {
            Collection collection = (Collection) value;
            if (collection.isEmpty()) {
                return errorMsg;
            }
        }
        if (Map.class.getName().equals(type)) {
            Map map = (Map) value;
            if (map.isEmpty()) {
                return errorMsg;
            }
        }
        return null;
    }

    /**
     * check a data nonnull
     *
     * @param data Data would be checked
     * @param tip  tips
     */
    public static void nonNull(Object data, String tip) {
        if (data == null) {
            throw new ParameterException(tip);
        }
    }

}
