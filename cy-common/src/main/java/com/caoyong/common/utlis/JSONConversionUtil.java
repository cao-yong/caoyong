package com.caoyong.common.utlis;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * json转换工具类
 * @author yong.cao
 * @time 2017年7月22日上午11:56:01
 */
@Slf4j
public class JSONConversionUtil {
	/**
	 * 对象转string
	 * @param o
	 * @return
	 */
	public static String objToString(Object o){
		log.info("objToString start.");
		ObjectMapper om = new ObjectMapper();
		//非空
		om.setSerializationInclusion(Include.NON_NULL);
		StringWriter w = new StringWriter();
		try {
			om.writeValue(w, o);
			String value = w.toString();
			return value;
		} catch (JsonGenerationException e) {
			log.info("objToString JsonGeneration error:", e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.info("objToString JsonMapping error:", e.getMessage(), e);
		} catch (IOException e) {
			log.info("objToString IO error:", e.getMessage(), e);
		} catch (Exception e) {
			log.info("objToString error:", e.getMessage(), e);
		}
		log.info("objToString end.");
		return null;
	}
	
	/**
	 * json string 转对象
	 * @param content
	 * @param cls
	 * @return
	 */
	public static <T> T stringToObj(String content, Class<T> cls){
		log.info("stringToObj start.");
		ObjectMapper om = new ObjectMapper();
		try {
			T value = om.readValue(content, cls);
			return value;
		} catch (JsonParseException e) {
			log.info("objToString JsonParse error:", e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.info("objToString JsonMapping error:", e.getMessage(), e);
		} catch (IOException e) {
			log.info("objToString IO error:", e.getMessage(), e);
		} catch (Exception e) {
			log.info("objToString error:", e.getMessage(), e);
		}
		log.info("stringToObj end.");
		return null;
	}
}
