package com.caoyong.common.utlis;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.caoyong.common.web.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密工具类
 * 
 * @author yong.cao
 * @time 2017年7月16日下午12:14:51
 */
@Slf4j
public class EncodeUtil {
    /**
     * 密码加密
     * 
     * @param password
     * @return
     */
    public static String encodePassword(String password) {
        log.info("encodePassword start.");
        try {
            //MD5加密
            String algorithm = Constants.MD5;
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            byte[] digest = instance.digest(password.getBytes());
            //十六进制加密
            char[] encodeHex = Hex.encodeHex(digest);
            return new String(encodeHex);
        } catch (NoSuchAlgorithmException e) {
            log.info("encodePassword algorithm name error:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.info("encodePassword error:{}", e.getMessage(), e);
        }
        log.info("encodePassword end.");
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encodePassword("admin"));
    }
}
