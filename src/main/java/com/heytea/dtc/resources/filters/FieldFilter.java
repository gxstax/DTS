package com.heytea.dtc.resources.filters;

import com.heytea.dtc.util.RegularUtil;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 字段过滤
 * </p>
 *
 * @author Ant
 * @since 2022/1/16 2:40 下午
 */
@Data
public class FieldFilter {
    /**
     * 数据库
     */
    private String db;

    /**
     * 字段过滤规则
     */
    private List<FieldRule> rules;

    @Data
    public static class FieldRule {
        /**
         * 表名称
         */
        private String table;

        /**
         * 范围过滤字段
         */
        private List<Range> ranges;

        private List<Equal> equals;
    }

    /**
     * 字段范围对象
     */
    @Data
    public static class Range {
        /**
         * 字段名
         */
        private String field;

        /**
         * ge 值
         */
        private String ge;

        /**
         * lt 值
         */
        private String lt;
    }

    /**
     * 等值对象
     */
    @Data
    public static class Equal {
        /**
         * 字段名
         */
        private String field;

        private String value;
    }

    public FieldRule getMatchRule(String tableName) {
        final Optional<FieldRule> any = this.rules.stream().filter(Objects::nonNull).filter(fieldRule ->
                RegularUtil.isMatchTable(fieldRule.getTable(), tableName)
        ).findAny();

        return any.orElse(null);
    }

}
