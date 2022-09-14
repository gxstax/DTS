package com.heytea.dtc.resources.mapping.rabbitmq;

import joptsimple.internal.Strings;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * RabbitMq 对应 DB 映射信息
 * </p>
 *
 * @author Ant
 * @since 2022/4/11 9:29 下午
 */
@Data
public class RabbitMqDbMapping {
    /**
     * 库名称
     */
    private String db;

    /**
     * 表映射列表
     */
    private List<TableMapping> tables;

    private Map<String, TableMapping> tableMappingMap = new ConcurrentHashMap<>();

    public void initExtendedProperties() {
        this.tables.stream().filter(Objects::nonNull).forEach(tableMapping -> {
            tableMappingMap.put(tableMapping.getTable(), tableMapping);
            // 初始化tableMapping延伸属性
            tableMapping.initExtendedProperties();
        });
    }


    @Data
    public class TableMapping {
        /**
         * 表名称
         */
        private String table;

        /**
         * 主题列表
         */
        private List<ExchangeMapping> exchanges;

        private Map<String, ExchangeMapping> exchangeMappingMap = new ConcurrentHashMap<>();


        /**
         * <p>
         * 初始化延伸属性
         * </p>
         *
         * @param
         * @return void
         */
        public void initExtendedProperties() {
            this.exchanges.stream().filter(Objects::nonNull).forEach(exchangeMapping -> {
                exchangeMappingMap.put(exchangeMapping.getExchangeName(), exchangeMapping);
                // 初始化延伸属性
                exchangeMapping.initExtendedProperties();
            });
        }
    }

    @Data
    public class ExchangeMapping {
        /**
         * 交换机名称
         */
        private String exchangeName;

        /**
         * 路由键
         */
        private Integer routeKey;

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

        public String[] getColumnArr() {
            if (StringUtils.isNotEmpty(column)) {
                return column.split(",");
            }
            return new String[0];
        }

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
    }

}
