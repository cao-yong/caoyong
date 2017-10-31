package com.caoyong.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 复制对象工具
 * 
 * @author caoyong
 * @time 2017年10月30日 下午6:19:05
 */
public class BeanConvertionHelp {

    /**
     * 拷贝原始Bean中同名属性值到目标bean中
     * 
     * @param toClazz
     * @param fromObj
     * @return
     * @throws Exception
     */
    public static <E> E

            copyBeanFieldValue(Class<E> toClazz, Object fromObj) throws Exception {

        E objCopy = toClazz.getConstructor().newInstance();

        Class<? super E> superClass = toClazz.getSuperclass();
        Field[] superFields = null;

        //基于获取父类属性一览
        if (superClass != null) {
            superFields = superClass.getDeclaredFields();
        }

        // 基于目标Bean生成属性一览
        Field[] fields = toClazz.getDeclaredFields();

        //最终处理Field列表
        Field[] finalFields = null;

        if (superFields != null && superFields.length > 0) {
            finalFields = new Field[superFields.length + fields.length];
            //复制父类属性一览
            System.arraycopy(superFields, 0, finalFields, 0, superFields.length);
            //复制自身属性一览
            System.arraycopy(fields, 0, finalFields, superFields.length, fields.length);
        } else {
            finalFields = fields;
        }

        // 拷贝属性值
        for (Field f : finalFields) {
            // 获得读方法 (fromObj.getXXX)
            PropertyDescriptor pdGet;
            try {
                pdGet = new PropertyDescriptor(f.getName(), fromObj.getClass());
            } catch (IntrospectionException e) {
                // 原始对象中不包括该属性，继续拷贝
                //log.warn("原始对象中不包括该属性，继续拷贝 ： " + f.getName());
                continue;
            }
            Method getMehod = pdGet.getReadMethod();

            // 获得写方法 (toClazz.setXXX)
            PropertyDescriptor pdSet = new PropertyDescriptor(f.getName(), toClazz);
            Method writeMethod = pdSet.getWriteMethod();

            // 值Copy
            Object getValue = getMehod.invoke(fromObj);
            writeMethod.invoke(objCopy, getValue);
        }
        // 返回对象
        return objCopy;
    }

}
