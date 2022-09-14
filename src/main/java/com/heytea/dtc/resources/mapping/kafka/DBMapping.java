package com.heytea.dtc.resources.mapping.kafka;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijunjun
 * @date 2021-11-2 21:10
 */
@Getter
@Setter
public class DBMapping {

    /**
     * 库名称
     */
    private String db;

    /**
     * 表映射列表
     */
    private List<TableMapping> tables;


    private Map<String, TableMapping> tableMappingMap = new ConcurrentHashMap<>();

    /**
     * <p>
     * 初始化延伸属性
     * </p>
     *
     * @param
     * @return void
     */
    public void initExtendedProperties() {
        this.tables.stream().filter(Objects::nonNull).forEach(tableMapping -> {
            tableMappingMap.put(tableMapping.getTable(), tableMapping);
            // 初始化tableMapping延伸属性
            tableMapping.initExtendedProperties();
        });
    }

    public void setDb(String db) {
        this.db = db;
    }

    public void setTables(List<TableMapping> tableMappings) {
        this.tables = tableMappings;
        if (tableMappings != null) {
            for (TableMapping mapping : tableMappings) {
                tableMappingMap.put(mapping.getTable(), mapping);
            }
        }
    }

    public TableMapping getTableMapping(String table){
        return tableMappingMap.get(table);
    }

}
