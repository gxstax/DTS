package com.ant.dts.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 正则匹配工具
 * </p>
 *
 * @author Ant
 * @since 2022/1/19 3:50 下午
 */
public class RegularUtil {

    public static boolean isMatchTable(String pattern, String value) {
        String REGEX = Optional.ofNullable(pattern).orElse("").trim();
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(value);

        return m.find();
    }

}
