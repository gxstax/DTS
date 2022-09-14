package com.ant.dts.output.kafka.admin;

import com.ant.dts.config.properties.kafka.AdminProperties;
import com.ant.dts.config.properties.kafka.CommonClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Kafka 运维管理客户端
 * </p>
 *
 * @author Ant
 * @since 2022/1/5 6:01 下午
 */
@Slf4j
public abstract class AbstracAdminClient implements ApplicationListener<ContextClosedEvent> {

    private AdminClient adminClient;
    private final CommonClientProperties commonClientProperties;
    private final AdminProperties adminProperties;

    protected AbstracAdminClient(CommonClientProperties commonClientProperties, AdminProperties adminProperties) {
        this.commonClientProperties = commonClientProperties;
        this.adminProperties = adminProperties;
    }

    /**
     * <p>
     * 获取 adminClient
     * </p>
     *
     * @param
     * @return {@link AdminClient}
     */
    protected AdminClient getAdminClient() {
        if (Objects.nonNull(adminClient)) {
            return adminClient;
        }
        Map<String, Object> configs = new HashMap<>();
        configs.putAll(commonClientProperties.getCommonClientConfig());
        configs.putAll(adminProperties.getAdminClientConfig());
        this.adminClient = AdminClient.create(configs);
        return adminClient;
    }

    /**
     * <p>
     * 监听容器关闭事件，关闭 AdminClient
     * </p>
     *
     * @param contextClosedEvent
     * @return void
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        if (Objects.nonNull(adminClient)) {
            this.adminClient.close();
            log.info("KafkaAdminClient[{}] 关闭！！！", adminClient);
        }
    }

}
