package com.caoyong.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务
 * 
 * @author caoyong
 * @time 2018年1月5日 上午11:34:02
 */
@Slf4j
public class SampleJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("execute sampleJob");
    }
}
