package com.caoyong.core.service.staticpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

/**
 * 静态化
 * @author yong.cao
 * @time 2017年7月13日下午10:23:10
 */
@Slf4j
public class StaticPageServiceImpl implements StaticPageService, ServletContextAware{
	
	private Configuration conf;
	
	private ServletContext servletContext;
	
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer){
		this.conf = freeMarkerConfigurer.getConfiguration();
	}
	/**
	 * 静态化商品
	 */
	@Override
	public void productStaticPage(Map<String, Object> root, String id){
		log.info("productStaticPage start. id:{}", id);
		//输出全路径
		String path = getPath("/html/product/" + id + ".html");
		
		File f = new File(path);
		
		File parentFile = f.getParentFile();
		
		//判断父文件是否存在
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		Writer out = null;
		try {
			Template template = conf.getTemplate("product.html");
			out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			template.process(root, out);
		} catch (IOException e) {
			log.error("productStaticPage io error:", e.getMessage(), e);
		} catch (Exception e) {
			log.error("productStaticPage error:", e.getMessage(), e);
		} finally {
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					log.error("colse io error");
				}
			}
		}
		log.info("productStaticPage end.");
	}
	/**
	 * 获取全路径
	 */
	@Override
	public String getPath(String name){
		log.info("getPaht start.name:{}", name);
		String path = null;
		try {
			path = servletContext.getRealPath(name);
		} catch (Exception e) {
			log.error("getPath error:", e.getMessage(), e);
		}
		log.info("getPath end.");
		return path;
	}
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
