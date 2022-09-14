package com.ant.dts.output.kafka.admin;

import com.ant.dts.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * <p>
 *  kafka 分区管理客户端：创建额外的主题分区。
 * </p>
 *
 * @author Ant
 * @since 2022/1/5 6:05 下午
 */
@Slf4j
@Component
public class KafkaPartitionsAdminClient extends AbstracAdminClient {

    protected KafkaPartitionsAdminClient(ApplicationConfig applicationConfig) {
        super(applicationConfig.getKafkaCommonConfig(), applicationConfig.getKafkaAdminConfig());
    }

}
