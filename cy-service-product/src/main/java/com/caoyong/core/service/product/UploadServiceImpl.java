package com.caoyong.core.service.product;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Service;

import com.caoyong.common.fastdfs.FastDFSUtils;
import com.caoyong.common.fastdfs.UploadFileVo;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 上传文件服务实现
 * 
 * @author yong.cao
 * @time 2017年6月14日下午11:17:54
 */

@Slf4j
@Service("uploadService")
public class UploadServiceImpl implements UploadService {
    /**
     * 上传图片
     * 
     * @param vo
     * @return
     */
    @Override
    public String uploadPic(UploadFileVo vo) throws BizException {
        log.info("uploadPic start. vo:{}", ToStringBuilder.reflectionToString(vo, ToStringStyle.DEFAULT_STYLE));
        String path = null;
        try {
            path = FastDFSUtils.uploadPic(vo);
            log.info("uploadPic success,path:{}", path);
        } catch (Exception e) {
            log.error("uploadPic error:{}", e.getMessage(), e);
            throw new BizException("upload pic error", e.getMessage(), e);
        }
        log.info("uploadPic end.");
        return path;
    }
}
