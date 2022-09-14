package com.heytea.dtc.constant.enums;

import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.resources.filters.FieldFilter;
import com.heytea.dtc.util.DateTimeUtil;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 数据库字段类型
 * </p>
 *
 * @author Ant
 * @since 2022/1/16 5:05 下午
 */
@Getter
public enum MysqlTypeEnum {

    TINYINT("tinyint"),
    INT("int"),
    BIGINT("bigint"),
    DECIMAL("decimal"),
    VARCHAR("varchar"),
    TEXT("text"),
    TIMESTAMP("timestamp")

    ;

    private String prefix;

    MysqlTypeEnum(String prefix) {
        this.prefix = prefix;
    }

    /**
     * <p>
     * 范围值匹配
     * </p>
     *
     * @param columnInfo
     * @param range
     * @return boolean
     */
    public static boolean rangeMatched(TransferMessage.ColumnInfo columnInfo, FieldFilter.Range range) {
        // TINYINT 类型
        if (columnInfo.getMysqlType().contains(TINYINT.prefix)) {
            return intRangeMatched(columnInfo.getAfter(), range.getGe(), range.getLt());
        }
        // INT 类型
        if (columnInfo.getMysqlType().contains(INT.prefix)) {
            return intRangeMatched(columnInfo.getAfter(), range.getGe(), range.getLt());
        }
        // BIGINT 类型
        if (columnInfo.getMysqlType().contains(BIGINT.prefix)) {
            return longRangeMatched(columnInfo.getAfter(), range.getGe(), range.getLt());
        }

        // DECIMAL 类型
        if (columnInfo.getMysqlType().contains(DECIMAL.prefix)) {
            return decimalRangeMatched(columnInfo.getAfter(), range.getGe(), range.getLt());
        }

        // TIMESTAMP 类型
        if (columnInfo.getMysqlType().contains(TIMESTAMP.prefix)) {
            return timestampRangeMatched(columnInfo.getAfter(), range.getGe(), range.getLt());
        }

        return false;
    }

    /**
     * <p>
     * 等值匹配
     * </p>
     *
     * @param columnInfo
     * @param equal
     * @return boolean
     */
    public static boolean equalMatched(TransferMessage.ColumnInfo columnInfo, FieldFilter.Equal equal) {
        // TINYINT 类型
        if (columnInfo.getMysqlType().contains(TINYINT.prefix)) {
            return intEqualMatched(columnInfo.getAfter(), equal.getValue());
        }
        // INT 类型
        if (columnInfo.getMysqlType().contains(INT.prefix)) {
            return intEqualMatched(columnInfo.getAfter(), equal.getValue());
        }
        // BIGINT 类型
        if (columnInfo.getMysqlType().contains(BIGINT.prefix)) {
            return longEqualMatched(columnInfo.getAfter(), equal.getValue());
        }

        // DECIMAL 类型
        if (columnInfo.getMysqlType().contains(DECIMAL.prefix)) {
            return decimalEqualMatched(columnInfo.getAfter(), equal.getValue());
        }

        // TIMESTAMP 类型
        if (columnInfo.getMysqlType().contains(TIMESTAMP.prefix)) {
            return timestampEqualMatched(columnInfo.getAfter(), equal.getValue());
        }
        return columnInfo.getAfter().equals(equal.getValue());
    }

    private static boolean intEqualMatched(String after, String value) {
        final Integer targetValuel = Integer.valueOf(after);
        final Integer equal = Integer.valueOf(value);
        return targetValuel.equals(equal);
    }

    private static boolean longEqualMatched(String after, String value) {
        final Long targetValuel = Long.valueOf(after);
        final Long equal = Long.valueOf(value);
        return targetValuel.equals(equal);
    }

    private static boolean decimalEqualMatched(String after, String value) {
        final BigDecimal targetValuel = new BigDecimal(after);
        final BigDecimal equal = new BigDecimal(value);
        return targetValuel.compareTo(equal)==0;
    }

    private static boolean timestampEqualMatched(String after, String value) {
        final LocalDateTime targetValuel = DateTimeUtil.stringToLocalDateTime(after);
        final LocalDateTime equal = DateTimeUtil.stringToLocalDateTime(value);
        return targetValuel.compareTo(equal)==0;
    }

    public static boolean intRangeMatched(String value, String ge, String lt) {
        final Integer targetValuel = Integer.valueOf(value);
        final Integer geVal = Integer.valueOf(ge);
        final Integer ltVal = Integer.valueOf(lt);

        return targetValuel >= geVal && targetValuel < ltVal;
    }

    public static boolean longRangeMatched(String value, String ge, String lt) {
        final Long targetValuel = Long.valueOf(value);
        final Long geVal = Long.valueOf(ge);
        final Long ltVal = Long.valueOf(lt);

        return targetValuel >= geVal && targetValuel < ltVal;
    }

    public static boolean decimalRangeMatched(String value, String ge, String lt) {
        final BigDecimal targetValuel = new BigDecimal(value);
        final BigDecimal geVal = new BigDecimal(ge);
        final BigDecimal ltVal = new BigDecimal(lt);

        return targetValuel.compareTo(geVal) >= 0 && targetValuel.compareTo(ltVal) < 0;
    }

    public static boolean timestampRangeMatched(String value, String ge, String lt) {
        final LocalDateTime targetValuel = DateTimeUtil.stringToLocalDateTime(value);
        final LocalDateTime geVal = DateTimeUtil.stringToLocalDateTime(ge);
        final LocalDateTime ltVal = DateTimeUtil.stringToLocalDateTime(lt);

        return targetValuel.compareTo(geVal) >=0 && targetValuel.isBefore(ltVal);
    }

}
