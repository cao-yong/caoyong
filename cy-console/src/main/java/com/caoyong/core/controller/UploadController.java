package com.caoyong.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 上传图片
 * @author yong.cao
 * @time 2017年6月12日下午9:49:06
 */
@Controller
@Slf4j
public class UploadController {

	@RequestMapping(value=("/upload/uploadPic.do"))
	public void uploadPic(@RequestParam(required=false) MultipartFile pic){
		log.info("uploadPic start. fliename={}",null!=pic ? pic.getOriginalFilename():"");
		
	}
}
