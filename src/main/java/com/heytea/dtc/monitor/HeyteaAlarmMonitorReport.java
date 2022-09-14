package com.heytea.dtc.monitor;

import com.heytea.starter.core.result.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 告警上报
 * </p>
 *
 * @author Ant
 * @since 2022/4/15 11:30 上午
 */
@Slf4j
@Component
public class HeyteaAlarmMonitorReport implements AlarmReport {

    private final static String postUrl = "http://test-monitor-1-alarm-monitor.heyteago.com/alarm-monitor/open/api";

    private final RestTemplate restTemplate;

    public HeyteaAlarmMonitorReport(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <F> BaseResult report(F alarmInfo) {
        log.info("告警上报 url[{}], param[{}]", postUrl, alarmInfo);
        final ResponseEntity<Object> responseEntity = restTemplate.postForEntity(postUrl, alarmInfo, Object.class);
        log.info("告警上报结束 result[{}]", responseEntity);
        return BaseResult.success("SUCESS");
    }

}
