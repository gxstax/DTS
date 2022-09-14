package com.ant.dts.resources.mapping.kafka;

import joptsimple.internal.Strings;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author lijunjun
 * @date 2021-11-9 17:13
 */
@Data
public class TopicMapping {

    /**
     * 主题
     */
    private String topic;

    /**
     * 主题对应分区个数
     */
    private Integer partitions;

    /**
     * 指定消息发送的分区号
     */
    private Integer assignPartition;

    /**
     * 路由方式
     */
    private String route;

    /**
     *  监听字段
     */
    private String column;

    /**
     * 额外字段，不用监听变更
     */
    private String extendColumn;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 下游业务需要的目标字段集合
     */
    private Set<String> targetColumns = new HashSet<>();

    private List<Integer> assignPartitions = new ArrayList<>();
    private List<String> columns = new ArrayList<>();
    private List<String> extendColumns = new ArrayList<>();
    private List<String> eventTypes = new ArrayList<>();

    /**
     * <p>
     * 初始化延伸属性
     * </p>
     *
     * @param
     * @return void
     */
    public void initExtendedProperties() {
        // 解析变动字段属性
        Arrays.stream(Optional.ofNullable(this.column).orElse(Strings.EMPTY).split(",")).forEach(c -> {
            this.targetColumns.add(c.trim());
        });

        // 解析额外字段属性
        Arrays.stream(Optional.ofNullable(this.extendColumn).orElse(Strings.EMPTY).split(",")).forEach(ec -> {
            this.targetColumns.add(ec.trim());
        });
    }

    public void setColumn(String column) {
        this.column = column;
        if (StringUtils.isNotEmpty(column))
            Stream.of(column.split(",")).forEach(o -> columns.add(o.trim()));
    }

    public String[] getColumnArr() {
        if (StringUtils.isNotEmpty(column)) {
            return column.split(",");
        }
        return new String[0];
    }

    public void setExtendColumn(String extendColumn) {
        this.extendColumn = extendColumn;
        if (StringUtils.isNotEmpty(extendColumn))
            Stream.of(extendColumn.split(",")).forEach(o -> extendColumns.add(o.trim()));
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
        if (StringUtils.isNotEmpty(eventType))
            Stream.of(eventType.split(",")).forEach(o -> eventTypes.add(o.trim()));
    }


    public void setTopic(String topic) {
        this.topic = topic;
    }

}
