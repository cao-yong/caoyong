package com.caoyong.common.utlis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.caoyong.common.vo.SystemInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 查询系统信息
 * 
 * @author caoyong
 * @time 2017年10月27日 下午3:49:37
 */
@Slf4j
public class SystemInfoUtil {
    /**
     * 获取系统信息
     * 
     * @return
     */
    public static SystemInfo getSystemInfo() {
        log.info("getSystemInfo start.");
        SystemInfo info = new SystemInfo();
        try {
            Sigar sigar = new Sigar();
            Mem mem = sigar.getMem();
            // 内存总量  
            long total = mem.getTotal();
            info.setTotalMemory(computerMemery(total));
            // 当前内存使用量  
            long used = mem.getUsed();
            info.setUsedMemory(computerMemery(used));
            // 当前内存剩余量  
            long free = mem.getFree();
            info.setFreeMemory(computerMemery(free));
            //操作系统信息
            OperatingSystem os = OperatingSystem.getInstance();
            info.setOs(os.getVendorName());
            info.setOsBit(os.getDataModel() + "位");
            //用户目录
            Properties props = System.getProperties();
            info.setUserHome(props.getProperty("user.home"));
            info.setJavaEnv(props.getProperty("java.version"));
            info.setJavaVm(props.getProperty("java.vm.name"));
            if (StringUtils.isBlank(os.getVendorName())) {
                info.setOs(props.getProperty("os.name"));
            }

        } catch (SigarException e) {
            log.info("getSystemInfo error:{}", e.getCause(), e);
        }
        return info;
    }

    /**
     * 把内存从byte转成M并转成float
     * 
     * @param memory
     * @return
     */
    private static float computerMemery(long memory) {
        BigDecimal mem = new BigDecimal(memory).divide(new BigDecimal(1024 * 1024L)).setScale(2, RoundingMode.HALF_UP);
        return mem.floatValue();
    }
}
