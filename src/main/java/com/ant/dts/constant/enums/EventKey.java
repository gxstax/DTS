package com.ant.dts.constant.enums;

/**
 * @author lijunjun
 * @date 2021-11-2 20:19
 */
public interface EventKey {

    //事件DB
    String DB = "DB";

    //事件table
    String TABLE_NAME = "TABLE_NAME";

    //事件类型
    String EVENT_TYPE = "EVENT_TYPE";

    //事件时间
    String EVENT_TIME = "EVENT_TIME";

    //数据
    String DATA = "DATA";
    String CHANGED = "CHANGED";
    String BEFORE_CHANGED = "BEFORE";
    String AFTER_CHANGED = "AFTER";

    //旧数据
    String OLD_DATA = "OLD";

    //新数据
    String NEW_DATA = "NEW";

    //队列
    String TOPIC = "TOPIC";

}
