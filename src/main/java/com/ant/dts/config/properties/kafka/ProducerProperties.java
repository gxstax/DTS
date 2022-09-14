package com.ant.dts.config.properties.kafka;

import lombok.Data;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.util.Map;

/**
 * <p>
 * Kafka生产端配置信息
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 11:03 上午
 */
@Data
public class ProducerProperties {

    /**
     * 生产者端配置信息（Map格式，便于和原生Kafka API 做无差异性映射）
     * <p>
     *     这里强烈建议您严格按照 kafka API 中的属性配置信息来一一映射
     *     所以我这里命名成了 kafka API 中的类名,具体配置请参照 {@link ProducerConfig}
     *
     *     示例：配置 生产端的压缩算法
     *     那么您在配置文件中的配置(以properties文件格式为例)为：
     *
     *     properties 格式文件配置如下(其中，中扩号中的配置参数和kafka原生 API 中保持一致即可):
     *     ------------------------------------------------------------------------------------------
     *     heytea.data-transfer-center.kafka.producer.producer-config.[compression.type]=gzip #压缩算法
     *     -------------------------------------------------------------------------------------------
     *     其中 [compression.type] 这个key的名字便是取的 {@link ProducerConfig#COMPRESSION_TYPE_CONFIG} 对应的字符串信息
     * </p>
     */
    private Map<String, Object> producerConfig;

}
