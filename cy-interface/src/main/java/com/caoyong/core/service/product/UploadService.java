package com.caoyong.core.service.product;

import com.caoyong.common.fastdfs.UploadFileVo;
import com.caoyong.exception.BizException;

/**
 * 上传文件服务
 * 
 * @author yong.cao
 * @time 2017年6月14日下午11:17:32
 */

public interface UploadService {
    /**
     * 上传图片
     * 
     * @param vo
     * @return
     */
    String uploadPic(UploadFileVo vo) throws BizException;
}
