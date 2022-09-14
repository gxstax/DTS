package com.ant.dts.deliver;

import com.alibaba.otter.canal.protocol.Message;
import com.ant.dts.deliver.converters.CanalMessageConverter;
import com.ant.dts.deliver.converters.TransferMessage;
import com.ant.dts.deliver.filters.TransferMessageFilter;
import com.ant.dts.util.SpringContextUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息承接方
 * </p>
 *
 * @author Ant
 * @since 2022/1/7 5:28 下午
 */
public abstract class AbstractDeliver implements Deliver {

    /**
     * <p>
     * 消息投递
     * </p>
     *
     * @param message
     * @return boolean
     */
    @Override
    public boolean deliver(Message message) {
        // 消息格式转换
        List<TransferMessage> transferMessages = CanalMessageConverter.messageToValidMessage(message);

        // 过滤消息
        messageFilter(transferMessages);

        // 消息投递
        return deliver(transferMessages);
    }

    protected abstract boolean deliver(List<TransferMessage> transferMessages);

    /**
     * <p>
     * 消息过滤（过滤不匹配的消息信息）
     * </p>
     *
     * @param transferMessages
     * @return void
     */
    private void messageFilter(List<TransferMessage> transferMessages) {
        final Map<String, Object> transferMessageFilters = SpringContextUtil.getBeansOfType(TransferMessageFilter.class);
        transferMessageFilters.entrySet().stream().forEach(filters -> {
            final TransferMessageFilter filter = (TransferMessageFilter) filters.getValue();
            filter.doFilter(transferMessages);
        });
    }

}
