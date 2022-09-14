package com.ant.dts.config.properties.kafka;

import lombok.Data;
import org.apache.kafka.clients.admin.AdminClientConfig;
import java.util.Map;

/**
 * <p>
 * kafka 管理端配置信息
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 11:08 上午
 */
@Data
public class AdminProperties {

    /**
     * 管理者端配置信息（Map格式，便于和原生Kafka API 做映射）
     * <p>
     *     这里强烈建议您严格按照 kafka API 中的属性配置信息来一一映射
     *     所以我这里命名成了 kafka API 中的类名,具体配置请参照 {@link AdminClientConfig}
     *     ps: 注意这里的包路径是在 org.apache.kafka.clients.admin 下
     *
     *     示例：如果我们要配置请求超时时间
     *     那么您在配置文件中的配置(以 properties 文件格式为例)为：
     *     ---------------------------------------------------------------------------------------
     *     heytea.data-transfer-center.kafka.admin.admin-client-config.[request.timeout.ms]=600000
     *     ---------------------------------------------------------------------------------------
     *     其中 [request.timeout.ms] 这个key的名字便是取的 {@link AdminClientConfig#REQUEST_TIMEOUT_MS_CONFIG} 对应的字符串信息
     * </p>
     *
     */
    private Map<String, Object> adminClientConfig;

}
