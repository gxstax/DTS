package com.ant.dts.deliver;

import com.alibaba.otter.canal.protocol.Message;

/**
 * <p>
 * 输出接口
 * </p>
 *
 * @author Ant
 * @since 2022/1/3 4:16 下午
 */
public interface Deliver {

    /**
     * <p>
     * 消息投递
     * </p>
     *
     * @param message
     * @return boolean
     */
    default boolean deliver(Message message) {
        return false;
    }

}
