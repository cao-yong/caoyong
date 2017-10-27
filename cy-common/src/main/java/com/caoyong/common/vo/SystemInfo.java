package com.caoyong.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统信息实体
 * 
 * @author caoyong
 * @time 2017年10月27日 下午3:51:38
 */
@Getter
@Setter
@ToString
public class SystemInfo {
    private Float  usedMemory;
    private Float  freeMemory;
    private Float  totalMemory;
    private String os;
    private String osBit;
    private String userHome;
    private String javaEnv;
    private String javaVm;
}
