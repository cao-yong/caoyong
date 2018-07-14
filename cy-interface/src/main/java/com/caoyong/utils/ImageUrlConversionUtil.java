package com.caoyong.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.enums.ImageSizeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 图片url统一处理工具类
 * 
 * @author yong.cao
 * @time 2018年7月14日 上午10:51:33
 */
@Slf4j
public class ImageUrlConversionUtil {

    /**
     * 对返回结果统一处理图片url
     * 
     * @param fromObj 要转换的对象
     * @param size 图片大小
     * @param convertField 要处理的字段
     * @return 处理后的对象
     * @throws BizException 业务异常
     */
    public static void convertingImageUrl(Object fromObj, ImageSizeEnum size, String... convertField)
            throws BizException {
        log.info("convertingImageUrl start.");
        if (fromObj == null || convertField == null || convertField.length < 1) {
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL.getMsg(), "Object为空");
        }
        //设置图片默认大小为480_320
        if (size == null) {
            size = ImageSizeEnum.IMG_SIZE_320;
        }
        for (String field : convertField) {
            //传出的所有字段进行检查
            //getFields()只能获取public的字段，这里用getDeclaredFields()获取对象所有字段
            for (Field declaredField : fromObj.getClass().getDeclaredFields()) {
                //开启修改访问权限  
                declaredField.setAccessible(true);
                //当字段是string并且是我们要检查的字段时对该字段修改
                if ("java.lang.String".equals(declaredField.getType().getName())
                        && declaredField.getName().equals(field)) {
                    converting(fromObj, declaredField.getName(), size);
                }
                if ("java.util.List".equals(declaredField.getType().getName())) {

                    try {
                        //获取getter与setter方法
                        PropertyDescriptor propertyDesc = new PropertyDescriptor(declaredField.getName(),
                                fromObj.getClass());
                        Method readMethod = propertyDesc.getReadMethod();
                        //调用getter方法
                        Object invoke = readMethod.invoke(fromObj);
                        //只处理array list中的对象
                        if (invoke instanceof ArrayList) {
                            List<?> list = (ArrayList<?>) invoke;
                            for (Object obj : list) {
                                convertingImageUrl(obj, size, field);
                            }
                        }
                    } catch (IntrospectionException e) {
                        log.warn("list property convertingImageUrl IntrospectionException error");
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        log.warn("list property readMethod.invoke error");
                    }
                }
            }
        }
        log.info("convertingImageUrl end.");
    }

    private static void converting(Object fromObj, String fieldName, ImageSizeEnum size) {
        try {
            //获取getter与setter方法
            PropertyDescriptor propertyDesc = new PropertyDescriptor(fieldName, fromObj.getClass());
            Method readMethod = propertyDesc.getReadMethod();
            Method writeMethod = propertyDesc.getWriteMethod();

            //调用getter方法
            Object invoke = readMethod.invoke(fromObj);
            if (invoke instanceof String) {
                //对imagUrl进行处理
                String imgUrl = (String) invoke;
                String imageUrl = "";
                if (StringUtils.isNotBlank(imgUrl)) {
                    Optional<String> findFirst = ImageSizeEnum.getAllNames().stream()
                            .filter(imgSize -> imgUrl.contains(imgSize)).findFirst();
                    if (findFirst.isPresent() && !size.getName().equals(findFirst.get())) {
                        imageUrl = imgUrl.replace(findFirst.get(), size.getName());
                    } else {
                        String[] split = imgUrl.split("\\.");
                        imageUrl = split[0] + size.getName() + "." + split[1];
                    }
                }
                //调用setter方法
                writeMethod.invoke(fromObj, imageUrl);
            }

        } catch (IntrospectionException e) {
            log.warn("converting IntrospectionException error");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.warn("converting readMethod.invoke error");
        }
    }
}
