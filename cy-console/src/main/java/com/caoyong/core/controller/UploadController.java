package com.caoyong.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

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
		} catch (Exception e) {
			log.error("uploadPic error:{}",e.getMessage(),e);
		}
		log.info("uploadPic end.");
	}
	/**
	 * 批量上传图片
	 * @param pic
	 */
	@RequestMapping(value=("/upload/uploadPics.do"))
	public @ResponseBody
	List<String> uploadPics(@RequestParam(required=false) MultipartFile[] pics, 
			HttpServletResponse response){
		log.info("uploadPics start.");
		try {
			if(null!=pics){
				List<String> urls = new ArrayList<>();
				//循环保存url信息
				for(MultipartFile pic : pics){
					//上传图片
					UploadFileVo vo = new UploadFileVo();
					vo.setPic(pic.getBytes());
					vo.setName(pic.getOriginalFilename());
					vo.setSize(pic.getSize());
					String path = uploadService.uploadPic(vo);
					//图片url
					String url = Constants.IMG_URL + path;
					urls.add(url);
				}
				return urls;
			}
		} catch (IOException e) {
			log.error("uploadPic io error:{}",e.getMessage(),e);
		} catch (BizException e) {
			log.error("uploadPic biz error:{}",e.getMessage(),e);
		} catch (Exception e) {
			log.error("uploadPic error:{}",e.getMessage(),e);
		}
		log.info("uploadPics end.");
		return null;
	}
	/**
	 * 上传富文本编辑器图片
	 * @param request
	 * @param response
	 */
	@RequestMapping(value=("/upload/uploadFck.do"))
	public void uploadFck(HttpServletRequest request, HttpServletResponse response){
		log.info("uploadFck start.");
		try {
			//将request强转成MultipartRequest,只接收文件类型的请求
			MultipartRequest mu = (MultipartRequest)request;
			Map<String, MultipartFile> fileMap = mu.getFileMap();
			Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
			for(Entry<String, MultipartFile> entry : entrySet){
				MultipartFile pic = entry.getValue();
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
					jsonObj.put("error", 0);
				}else{
					jsonObj.put("isSuccess", false);
					jsonObj.put("errorMsg", "上传图片失败!");
					jsonObj.put("error", 1);
				}
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write(jsonObj.toString());
			}
			log.info("uploadFck end.");
		} catch (JSONException e) {
			log.error("uploadFck JSON error:{}",e.getMessage(),e);
		} catch (IOException e) {
			log.error("uploadFck io error:{}",e.getMessage(),e);
		} catch (BizException e) {
			log.error("uploadFck biz error:{}",e.getMessage(),e);
		} catch (Exception e) {
			log.error("uploadFck error:{}",e.getMessage(),e);
		}
	}
}
