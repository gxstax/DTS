package com.ant.dts.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author Ant
 * @since 2022/1/16 5:48 下午
 */
public class DateTimeUtil {

    public static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";


    public static LocalDateTime stringToLocalDateTime(String dateStr){
        DateTimeFormatter noTime = DateTimeFormatter.ofPattern(dateTimePattern);
        LocalDateTime time = LocalDateTime.parse(dateStr, noTime);
        return time;
    }
}
