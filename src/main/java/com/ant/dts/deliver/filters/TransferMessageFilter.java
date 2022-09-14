package com.ant.dts.deliver.filters;

import com.ant.dts.deliver.converters.TransferMessage;

import java.util.List;

/**
 * <p>
 * 消息过滤器
 * </p>
 *
 * @author Ant
 * @since 2022/1/16 3:21 下午
 */
public interface TransferMessageFilter {
    /**
     * <p>
     * 有效消息规则过滤
     * </p>
     *
     * @param transferMessages
     * @return transferMessages After Filter
     */
    void doFilter(List<TransferMessage> transferMessages);

}
