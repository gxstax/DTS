package com.heytea.dtc.deliver.converters;

import com.alibaba.otter.canal.protocol.Message;

import java.util.List;

/**
 * <p>
 * Canal消息转换器
 * </p>
 *
 * @author Ant
 * @since 2022/1/7 3:18 下午
 */
public class CanalMessageConverter {

    public static List<TransferMessage> messageToValidMessage(Message message) {
        return TransferMessage.converter(message);
    }

}
