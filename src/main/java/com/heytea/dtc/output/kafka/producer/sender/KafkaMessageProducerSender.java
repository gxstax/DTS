package com.heytea.dtc.output.kafka.producer.sender;

import com.heytea.dtc.config.ApplicationConfig;
import com.heytea.dtc.output.kafka.producer.sender.callback.MessageSendCallBack;
import com.heytea.dtc.resources.ResourceStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 消息发送句柄
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 5:10 下午
 */
@Slf4j
@Component
public class KafkaMessageProducerSender extends AbstracMessageSender<String, Object> {

    public KafkaMessageProducerSender(ApplicationConfig applicationConfig, ResourceStore resourceStore) {
        super(applicationConfig.getKafkaProducerConfig(),
                applicationConfig.getKafkaCommonConfig(),
                resourceStore,
                new MessageSendCallBack());
    }

}
