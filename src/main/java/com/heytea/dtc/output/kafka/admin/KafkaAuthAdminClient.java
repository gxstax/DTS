package com.heytea.dtc.output.kafka.admin;

import com.heytea.dtc.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * kafka 权限管理客户端：包括具体权限的配置与删除。
 * </p>
 *
 * @author Ant
 * @since 2022/1/5 6:08 下午
 */
@Slf4j
@Component
public class KafkaAuthAdminClient extends AbstracAdminClient {

    protected KafkaAuthAdminClient(ApplicationConfig applicationConfig) {
        super(applicationConfig.getKafkaCommonConfig(), applicationConfig.getKafkaAdminConfig());
    }

}
