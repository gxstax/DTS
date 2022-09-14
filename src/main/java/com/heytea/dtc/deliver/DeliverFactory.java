package com.heytea.dtc.deliver;

import com.alibaba.otter.canal.protocol.Message;
import com.heytea.dtc.constant.enums.DeliverClassEnum;
import com.heytea.dtc.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

/**
 * <p>
 * 获取消息投递方
 * </p>
 *
 * @author Ant
 * @since 2022/1/3 6:50 下午
 */
@Slf4j
public class DeliverFactory {

    public static Deliver getDeliver(String deliver) {
        Deliver deliverServer = null;
        Class clazz = DeliverClassEnum.getClassFromDeliver(deliver);

        try {
            deliverServer = (Deliver) SpringContextUtil.getBean(clazz);
        } catch (Exception e) {
            log.warn("实例化消息投递方失败", e);
        }

        // 投递服务未实现则默认不处理消息
        if (Objects.isNull(deliverServer)) {
            return new Deliver() {
                @Override
                public boolean deliver(Message message) {
                    log.info("消息投递服务未实现,默认不处理");
                    return Deliver.super.deliver(message);
                }
            };
        }

        return deliverServer;
    }

}
