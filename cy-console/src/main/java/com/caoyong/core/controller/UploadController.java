package com.caoyong.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.caoyong.common.fastdfs.UploadFileVo;
import com.caoyong.common.fastdfs.UploadResponse;
import com.caoyong.common.web.Constants;
import com.caoyong.core.service.product.UploadService;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 上传图片
 * 
 * @author yong.cao
 * @time 2017年6月12日下午9:49:06
 */
@CrossOrigin
@Controller
@Slf4j
@RequestMapping(value = ("/upload"))
public class UploadController {

    @Reference(version = "1.0.0")
    private UploadService uploadService;

    /**
     * 上传图片
     * 
     * @param pic
     */
    @ResponseBody
    @RequestMapping(value = ("/uploadPic.do"))
    public UploadResponse uploadPic(MultipartFile pic) {
        log.info("uploadPic start. fliename={}", null != pic ? pic.getOriginalFilename() : "");
        UploadResponse resp = new UploadResponse();
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
            if (StringUtils.isNotBlank(path)) {
                resp.setUrl(url);
                resp.setIsSuccess(true);
            } else {
                resp.setErrorMsg("上传图片失败!");
            }
        } catch (IOException e) {
            log.error("uploadPic io error:{}", e.getMessage(), e);
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
        } catch (BizException e) {
            log.error("uploadPic biz error:{}", e.getMessage(), e);
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
        } catch (Exception e) {
            log.error("uploadPic error:{}", e.getMessage(), e);
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
        }
        log.info("uploadPic end.");
        return resp;
    }

    /**
     * 批量上传图片
     * 
     * @param pic
     */
    @RequestMapping(value = ("/uploadPics.do"))
    public @ResponseBody List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics,
                                                 HttpServletResponse response) {
        log.info("uploadPics start.");
        try {
            if (null != pics) {
                List<String> urls = new ArrayList<>();
                //循环保存url信息
                for (MultipartFile pic : pics) {
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
            log.error("uploadPic io error:{}", e.getMessage(), e);
        } catch (BizException e) {
            log.error("uploadPic biz error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("uploadPic error:{}", e.getMessage(), e);
        }
        log.info("uploadPics end.");
        return null;
    }

    /**
     * 上传富文本编辑器图片
     * 
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = ("/uploadFck.do"))
    public UploadResponse uploadFck(HttpServletRequest request) {
        log.info("uploadFck start.");
        UploadResponse resp = new UploadResponse();
        try {
            //将request强转成MultipartRequest,只接收文件类型的请求
            MultipartRequest mu = (MultipartRequest) request;
            Map<String, MultipartFile> fileMap = mu.getFileMap();
            Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
            for (Entry<String, MultipartFile> entry : entrySet) {
                MultipartFile pic = entry.getValue();
                //上传图片
                UploadFileVo vo = new UploadFileVo();
                vo.setPic(pic.getBytes());
                vo.setName(pic.getOriginalFilename());
                vo.setSize(pic.getSize());
                String path = uploadService.uploadPic(vo);
                //图片url
                String url = Constants.IMG_URL + path;
                if (StringUtils.isNotBlank(path)) {
                    resp.setUrl(url);
                    resp.setIsSuccess(true);
                } else {
                    resp.setErrorMsg("上传图片失败!");
                    resp.setError(1);
                }
            }
            log.info("uploadFck end.");
        } catch (JSONException e) {
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
            log.error("uploadFck JSON error:{}", e.getMessage(), e);
        } catch (IOException e) {
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
            log.error("uploadFck io error:{}", e.getMessage(), e);
        } catch (BizException e) {
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
            log.error("uploadFck biz error:{}", e.getMessage(), e);
        } catch (Exception e) {
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
            log.error("uploadFck error:{}", e.getMessage(), e);
        }
        return resp;
    }

    /**
     * dropzone.js批量上传图片
     * 
     * @param pic
     */
    @RequestMapping(value = ("/uploadDropZonePics.do"))
    public UploadResponse uploadDropZonePic(MultipartHttpServletRequest request) {
        log.info("uploadPic uploadDropZonePic start.");
        UploadResponse resp = new UploadResponse();
        try {
            Iterator<String> itr = request.getFileNames();
            List<String> urls = new ArrayList<>();
            while (itr.hasNext()) {
                String uploadedFile = itr.next();
                MultipartFile file = request.getFile(uploadedFile);
                String filename = file.getOriginalFilename();
                byte[] bytes = file.getBytes();

                //上传图片
                UploadFileVo vo = new UploadFileVo();
                vo.setPic(bytes);
                vo.setName(filename);
                vo.setSize(file.getSize());
                String path = uploadService.uploadPic(vo);
                //图片url
                String url = Constants.IMG_URL + path;
                urls.add(url);
            }
            resp.setIsSuccess(true);
            resp.setUrls(urls);
        } catch (IOException e) {
            log.error("uploadDropZonePic io error:{}", e.getMessage(), e);
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
            log.error("uploadFck error:{}", e.getMessage(), e);
        } catch (BizException e) {
            log.error("uploadDropZonePic biz error:{}", e.getMessage(), e);
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
            log.error("uploadFck error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("uploadDropZonePic error:{}", e.getMessage(), e);
            resp.setErrorMsg("上传图片失败:" + e.getMessage());
            resp.setError(1);
            log.error("uploadFck error:{}", e.getMessage(), e);
        }
        log.info("uploadDropZonePic end.");
        return resp;
    }
}
