package com.heytea.dtc.constant.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * <p>
 * Kafka 路由类型
 * </p>
 *
 * @author Ant
 * @since 2022/1/12 7:09 下午
 */
@Getter
public enum RouteType {

    RANDOM("RANDOM"),
    ROUND_ROBIN("ROUND_ROBIN"),
    COLUMN("COLUMN")

    ;

    private String prefix;

    RouteType(String prefix) {
        this.prefix = prefix;
    }

    public static RouteType getRouteType(String routeTypeValue) {
        return Arrays.stream(RouteType.values())
                .filter(thisRoute -> thisRoute.name().equals(routeTypeValue)).findAny()
                .orElse(RouteType.ROUND_ROBIN);
    }

    /**
     * <p>
     * 是否是字段路由规则
     * </p>
     *
     * @param routeRule
     * @return boolean
     */
    public static boolean isColumnRule(String routeRule) {
        return routeRule.startsWith(COLUMN.prefix);
    }

    /**
     * <p>
     * 是否是轮询路由规则
     * </p>
     *
     * @param routeRule
     * @return boolean
     */
    public static boolean isRoundRobinRule(String routeRule) {
        return routeRule.startsWith(ROUND_ROBIN.prefix);
    }

    /**
     * <p>
     * 提取路由字段名称
     * </p>
     *
     * @param routeRule
     * @return columnName
     */
    public static String extractColumnName(String routeRule) {
        // 配置错误返回默认分区号
        if (!RouteType.isColumnRule(routeRule)) {
            return null;
        }
        return routeRule.substring(routeRule.indexOf("[") + 1, routeRule.lastIndexOf("]"));
    }

}
