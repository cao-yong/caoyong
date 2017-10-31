package com.caoyong.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 参数验空工具类
 * 
 * @author yong.cao
 * @time 2017年6月24日上午10:34:12
 */
@Slf4j
public class CheckParamsUtil {

    public static void check(Object o, Class<?> cz, String... arg) throws BizException {
        if (o == null) {
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL.getMsg(), "Object为空");
        }
        //getFields()只能获取public的字段，这里用getDeclaredFields()获取对象所有字段
        Field fields[] = cz.getDeclaredFields();
        for (Field field : fields) {
            //开启修改访问权限  
            field.setAccessible(true);
            for (String attribute : arg) {
                //验证需要校验的参数
                if (attribute.equals(field.getName())) {
                    String type = "";
                    Object value = null;
                    try {
                        //获取字段类型名称
                        type = field.getType().getName();
                        //获取字段的值
                        value = field.get(o);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    //string类型数据
                    if ("java.lang.String".equals(type)) {
                        if (StringUtils.isBlank(value.toString())) {
                            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL.getMsg(), attribute + "为空");
                        }
                        //string以外的对象类型
                    } else {
                        if (null == value) {
                            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL.getMsg(), attribute + "为空");
                        }
                    }

                }
            }
        }
    }
}
