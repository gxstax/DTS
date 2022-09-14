package com.ant.dts.monitor;

import lombok.Data;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * <p>
 * 告警信息
 * </p>
 *
 * @author Ant
 * @since 2022/4/15 11:24 上午
 */
@Data
public class AlarmInfo {
    /**
     * 告警点编码
     */
    private String pointCode;

    /**
     * 告警消息内容
     */
    private String sourceRecord;

    /**
     * 告警时间点
     */
    private Long eventTime;

    /**
     * 是否出发告警，用于告警消息恢复
     */
    private Boolean triggered;

    public static AlarmInfo buildReport(String nodeName, TreeCacheEvent.Type type) {
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.eventTime = System.currentTimeMillis();
        alarmInfo.pointCode = "ZOOKEEPER_CLUSTER";
        String record = String.format("节点[%s]变动为[%s]状态", nodeName, type);
        alarmInfo.sourceRecord = record;
        alarmInfo.triggered = true;
        return alarmInfo;
    }
}
