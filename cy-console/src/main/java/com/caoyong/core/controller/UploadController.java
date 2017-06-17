package com.caoyong.core.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.caoyong.common.fastdfs.UploadFileVo;
import com.caoyong.common.web.Constants;
import com.caoyong.core.service.product.UploadService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 上传图片
 * @author yong.cao
 * @time 2017年6月12日下午9:49:06
 */
@Controller
@Slf4j
public class UploadController {
	@Autowired
	private UploadService uploadService;
	/**
	 * 上传图片
	 * @param pic
	 */
	@RequestMapping(value=("/upload/uploadPic.do"))
	public void uploadPic(@RequestParam(required=false) MultipartFile pic, 
			HttpServletResponse response){
		log.info("uploadPic start. fliename={}",null!=pic ? pic.getOriginalFilename():"");
		try {
			//上传图片
			UploadFileVo vo = new UploadFileVo();
			vo.setPic(pic.getBytes());
			vo.setName(pic.getOriginalFilename());
			vo.setSize(pic.getSize());
			String path = uploadService.uploadPic(vo);
			//图片url
			String url = Constants.IMG_URL + path;
			//回写图片
			JSONObject jsonObj = new JSONObject();
			if(StringUtils.isNotBlank(path)){
				jsonObj.put("url", url);
				jsonObj.put("isSuccess", true);
			}else{
				jsonObj.put("isSuccess", false);
				jsonObj.put("errorMsg", "上传图片失败!");
			}
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jsonObj.toString());
		} catch (IOException e) {
			log.error("uploadPic io error:{}",e.getMessage(),e);
		} catch (BizException e) {
			log.error("uploadPic biz error:{}",e.getMessage(),e);
		}catch (Exception e) {
			log.error("uploadPic error:{}",e.getMessage(),e);
		}
		log.info("uploadPic end.");
	}
}
