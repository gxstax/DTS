package com.heytea.dtc.deliver.converters;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.heytea.dtc.constant.enums.EventType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Canal 消息有效信息
 * </p>
 *
 * @author Ant
 * @since 2022/1/7 2:26 下午
 */
@Data
@Slf4j
public class TransferMessage {

    private String id;
    private String db;
    private String table;
    private EventType eventType;
    private long eventTime;
    private TransferData transferData;

    @Data
    public static class TransferData {

        private Map<String, ColumnInfo> colume;

        public TransferData() {
            this.colume = new HashMap<>();
        }
    }

    @Data
    public static class ColumnInfo {
        /**
         * 是否变动
         */
        private boolean changed;

        /**
         * 是否主键
         */
        private boolean isPrimary;

        /**
         * 变动前的值
         */
        private String before;

        /**
         * 字段类型
         */
        private String mysqlType;

        /**
         * 变动后的值 (只有更新时间才会有值)
         */
        private String after;
    }

    public TransferMessage() {
        this.transferData = new TransferData();
    }

    public TransferMessage(String db, String table, EventType eventType, long eventTime) {
        this.db = db;
        this.table = table;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.transferData = new TransferData();
    }


    /**
     * <p>
     * 消息转换
     * </p>
     *
     * @param message canal protocol 原始消息
     * @return canalValidMessages
     */
    public static List<TransferMessage> converter(Message message) {
        // 结果集
        List<TransferMessage> transferMessages = new ArrayList<>();

        Iterator<CanalEntry.Entry> entryIt = message.getEntries().iterator();
        while(entryIt.hasNext()) {
            final CanalEntry.Entry entry = entryIt.next();
            final CanalEntry.EntryType entryType = entry.getEntryType();
            // 过滤掉不是 '行数据' 的数据
            if (!entryType.equals(CanalEntry.EntryType.ROWDATA)) {
                continue;
            }

            List<TransferMessage> batchTransferMessage = new ArrayList<>();
            try {
                batchTransferMessage = parseEntry(entry);
            } catch (InvalidProtocolBufferException e) {
                log.warn("消息解析失败", e);
                throw new RuntimeException(e);
            }

            if (!CollectionUtils.isEmpty(batchTransferMessage)) {
                transferMessages.addAll(batchTransferMessage);
            }
        }

        return transferMessages;
    }

    /**
     * <p>
     * 消息解析
     * </p>
     *
     * @param entry
     * @return {@link TransferMessage}
     */
    static List<TransferMessage> parseEntry(CanalEntry.Entry entry) throws InvalidProtocolBufferException {

        // CanalValidMessage 消息模版（用户返回对象复制头信息）
        TransferMessage templateValidMessage = new TransferMessage();

        // 消息头
        CanalEntry.Header header = entry.getHeader();
        // 消息体
        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

        // 过滤不敏感事件 (不处理非 INSERT、UPDATA、DELETE 事件)
        if (!EventType.isSensitiveEvent(header.getEventType())) {
            return null;
        }
        // 过滤掉 DDL
        if (rowChange.getIsDdl()) {
            return null;
        }

        // 从消息头提取信息
        templateValidMessage.extractInfoFromHeader(header);

        // 从数据行提取消息
        List<TransferMessage> transferMessages = new ArrayList<>();
        templateValidMessage.extractInfoFromRowChange(rowChange, transferMessages);

        return transferMessages;
    }

    /**
     * <p>
     * 提取 Canal 消息头信息
     * </p>
     *
     * @param header
     * @return void
     */
    private void extractInfoFromHeader(CanalEntry.Header header) {
        this.db = header.getSchemaName();
        this.table = header.getTableName();
        this.eventType = EventType.getEventType(header.getEventType());
        this.eventTime = header.getExecuteTime();
    }

    /**
     * <p>
     * 提取Canal消息体信息
     * </p>
     *
     * @param rowChange
     * @return void
     */
    private void extractInfoFromRowChange(CanalEntry.RowChange rowChange, List<TransferMessage> transferMessages) {

        final Iterator<CanalEntry.RowData> rowDatasIt = rowChange.getRowDatasList().iterator();
        while (rowDatasIt.hasNext()) {
            TransferMessage transferMessage = new TransferMessage(this.db, this.table, this.eventType, this.eventTime);
            transferMessages.add(transferMessage);

            // 原始列数据
            final CanalEntry.RowData rowData = rowDatasIt.next();
            final List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            final List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();

            // 解析列数据
            parseColumn(beforeColumnsList, afterColumnsList, transferMessage);
        }
    }

    /**
     * <p>
     * 解析列数据
     * </p>
     *
     * @param beforeColumnsList
     * @param afterColumnsList
     * @param transferMessage
     * @return void
     */
    private void parseColumn(final List<CanalEntry.Column> beforeColumnsList,
                             final List<CanalEntry.Column> afterColumnsList,
                             TransferMessage transferMessage) {
        final TransferData transferData = transferMessage.getTransferData();

        // 变动前列数据
        final Iterator<CanalEntry.Column> beforeColumnIt = beforeColumnsList.iterator();
        while (beforeColumnIt.hasNext()) {
            final CanalEntry.Column column = beforeColumnIt.next();
            // 填充变动数据信息
            ColumnInfo columnInfo = new ColumnInfo();
            transferData.colume.put(column.getName(), columnInfo);
            columnInfo.before = column.getValue();
            columnInfo.isPrimary = column.getIsKey();
            columnInfo.mysqlType = column.getMysqlType();
        }

        // 变动后列数据
        final Iterator<CanalEntry.Column> afterColumnIt = afterColumnsList.iterator();
        while (afterColumnIt.hasNext()) {
            final CanalEntry.Column column = afterColumnIt.next();
            if (transferData.colume.containsKey(column.getName())) {
                ColumnInfo columnInfo = transferData.colume.get(column.getName());
                columnInfo.after = column.getValue();
                columnInfo.changed = column.getUpdated();
            } else {
                ColumnInfo columnInfo = new ColumnInfo();
                transferData.colume.put(column.getName(), columnInfo);
                columnInfo.after = column.getValue();
                columnInfo.isPrimary = column.getIsKey();
                columnInfo.changed = column.getUpdated();
                columnInfo.isPrimary = column.getIsKey();
                columnInfo.mysqlType = column.getMysqlType();
            }
        }

    }

}
