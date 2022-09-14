package com.ant.dts.config.properties.kafka;

import lombok.Data;
import org.apache.kafka.clients.CommonClientConfigs;
import java.util.Map;

/**
 * <p>
 * 客户端共有参数
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 1:39 下午
 */
@Data
public class CommonClientProperties {

    /**
     * 客户端公共配置信息（Map格式，便于和原生Kafka API 做映射）
     * <p>
     *     公共配置配置一些通用的配置信息，比如 bootstrap.servers
     *     其它差异性配置，尽量配置到对应的客户端
     *
     *     示例：配置kafka 服务集群
     *     那么您在配置文件中的配置(以properties文件格式为例)：
     *     -----------------------------------------------------------------------------------------------------
     *     heytea.data-transfer-center.kafka.common.common-client-config.[bootstrap.servers]=10.250.210.238:9092
     *     -----------------------------------------------------------------------------------------------------
     *     其中 [bootstrap.servers] 这个key的名字便是取的 {@link CommonClientConfigs#BOOTSTRAP_SERVERS_CONFIG} 对应的字符串信息
     * </p>
     */
    private Map<String, Object> commonClientConfig;

}
