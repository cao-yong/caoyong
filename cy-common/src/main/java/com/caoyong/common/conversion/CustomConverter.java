package com.caoyong.common.conversion;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义转换器
 * @author yong.cao
 * @time 2017年7月16日上午10:12:08
 */
@Slf4j
public class CustomConverter implements Converter<String, String>{

	@Override
	public String convert(String source) {
		log.info("convert start,source:{}", source);
		try {
			if(StringUtils.isNotBlank(source)){
				source = source.trim();
				if(StringUtils.isNotBlank(source)){
					return source;
				}
			}
		} catch (Exception e) {
			log.info("convert end.");
		}
		return null;
	}

}
