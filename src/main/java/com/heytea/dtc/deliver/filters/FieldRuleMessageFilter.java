package com.heytea.dtc.deliver.filters;

import com.heytea.dtc.constant.enums.MysqlTypeEnum;
import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.resources.ResourceStore;
import com.heytea.dtc.resources.filters.FieldFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字段规则消息过滤
 * </p>
 *
 * @author Ant
 * @since 2022/1/16 3:23 下午
 */
@Slf4j
@Component
public class FieldRuleMessageFilter implements TransferMessageFilter {

    private final List<FieldFilter> fieldFilters;

    public FieldRuleMessageFilter(ResourceStore resourceStore) {
        this.fieldFilters = resourceStore.getFieldFilters();
    }

    /**
     * <p>
     * 有效消息规则过滤
     * </p>
     *
     * @param transferMessages
     * @return transferMessages After Filter
     */
    @Override
    public void doFilter(List<TransferMessage> transferMessages) {
        // 删除不匹配过滤规则数据
        transferMessages.removeIf(this::notMatch);
    }

    /**
     * <p>
     * 是否是不匹配消息
     * </p>
     *
     * @param transferMessage
     * @return boolean
     */
    private boolean notMatch(TransferMessage transferMessage) {
        for (FieldFilter fieldFilter : fieldFilters) {
            // 是否是该库的匹配规则
            if (fieldFilter.getDb().equals(transferMessage.getDb())) {
                // 拿到具体表的匹配规则
                final FieldFilter.FieldRule fieldRule = fieldFilter.getMatchRule(transferMessage.getTable());
                // 开始字段匹配
                final boolean matched = matchFiled(fieldRule, transferMessage);
                return !matched;
            }
        }
        return false;
    }

    /**
     * <p>
     * 字段匹配
     * </p>
     *
     * @param fieldRule
     * @param transferMessage
     * @return boolean
     */
    private boolean matchFiled(FieldFilter.FieldRule fieldRule, TransferMessage transferMessage) {
        if (!ObjectUtils.isEmpty(fieldRule)) {
            final Map<String, TransferMessage.ColumnInfo> colume = transferMessage.getTransferData().getColume();
            // 范围字段判断
            boolean rangeMatch = matchRanges(fieldRule, colume);
            if (!rangeMatch) {
                return false;
            }
            boolean equalMatch = matchEqualField(fieldRule, colume);
            return equalMatch;
        }
        return false;
    }

    private boolean matchRanges(FieldFilter.FieldRule fieldRule, Map<String, TransferMessage.ColumnInfo> colume) {
        final List<FieldFilter.Range> ranges = fieldRule.getRanges();
        // 无范围过滤条件
        if (CollectionUtils.isEmpty(ranges)) {
            return true;
        }
        boolean matched;
        for (FieldFilter.Range range : ranges) {
            // 字段值
            final TransferMessage.ColumnInfo columnInfo = colume.get(range.getField());
            if (ObjectUtils.isEmpty(columnInfo)) {
                continue;
            }

            matched = MysqlTypeEnum.rangeMatched(columnInfo, range);
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    private boolean matchEqualField(FieldFilter.FieldRule fieldRule, Map<String, TransferMessage.ColumnInfo> colume) {
        final List<FieldFilter.Equal> equals = fieldRule.getEquals();
        // 无等值过滤条件
        if (CollectionUtils.isEmpty(equals)) {
            return true;
        }
        boolean matched;
        for (FieldFilter.Equal equal : equals) {
            // 字段值
            final TransferMessage.ColumnInfo columnInfo = colume.get(equal.getField());
            if (ObjectUtils.isEmpty(columnInfo)) {
                continue;
            }

            matched = MysqlTypeEnum.equalMatched(columnInfo, equal);
            if (!matched) {
                return false;
            }
        }
        return true;
    }

}
