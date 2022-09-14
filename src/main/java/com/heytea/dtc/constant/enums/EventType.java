package com.heytea.dtc.constant.enums;

import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lijunjun
 * @date 2021-11-2 20:02
 */
@Getter
public enum EventType {

    INSERT(1, CanalEntry.EventType.INSERT),
    UPDATE(2, CanalEntry.EventType.UPDATE),
    DELETE(3, CanalEntry.EventType.DELETE);

    private int type;

    private CanalEntry.EventType canalEventType;

    EventType(int type, CanalEntry.EventType canalEventType) {
        this.type = type;
        this.canalEventType = canalEventType;
    }

    public static EventType getEventType(CanalEntry.EventType eventType) {
        return Arrays.stream(EventType.values())
                .filter(thisEvent -> thisEvent.canalEventType.equals(eventType)).findAny()
                .orElse(null);
    }

    /**
     * <p>
     * 是否对该事件敏感
     * </p>
     *
     * @param eventType
     * @return boolean true:敏感 false:不敏感
     */
    public static boolean isSensitiveEvent(CanalEntry.EventType eventType) {
        return Arrays.stream(EventType.values())
                .anyMatch(thisEvent -> thisEvent.canalEventType.equals(eventType));
    }

    public static boolean isConfigEvent(String eventType, EventType dataEvent) {
        final String[] split = eventType.split(",");
        return Arrays.stream(split).anyMatch(type -> type.equals(dataEvent.getCanalEventType().name()));
    }
}
