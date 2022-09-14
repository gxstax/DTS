package com.heytea.dtc.config;

import com.alibaba.fastjson.JSON;
import com.heytea.dtc.config.properties.CanalProperties;
import com.heytea.dtc.config.properties.kafka.AdminProperties;
import com.heytea.dtc.config.properties.kafka.CommonClientProperties;
import com.heytea.dtc.config.properties.kafka.ConsumerProperties;
import com.heytea.dtc.config.properties.kafka.KafkaProperties;
import com.heytea.dtc.config.properties.kafka.ProducerProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p>
 * DTC 项目配置信息
 * </p>
 *
 * @author Ant
 * @since 2021/12/27 1:01 下午
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "heytea")
public class ApplicationConfig {

    /** 环境变量 **/
    private Env env;

    /** 配置信息 **/
    private DataTransferCenter dataTransferCenter;

    @PostConstruct
    private void init() {
        if (log.isDebugEnabled()) {
            log.debug("工程配置完成 ApplicationConfig [{}]", JSON.toJSONString(this));
        }
    }

    @Data
    public static class Env {
        private Boolean isProduction;
        private String baseurl;
    }

    @Data
    public static class DataTransferCenter {
        private CanalProperties canal;
        private KafkaProperties kafka;
    }

    public CanalProperties getCanalConfig() {
        return dataTransferCenter.canal;
    }

    public CommonClientProperties getKafkaCommonConfig() {
        return dataTransferCenter.kafka.getCommon();
    }

    public AdminProperties getKafkaAdminConfig() {
        return dataTransferCenter.kafka.getAdmin();
    }

    public ProducerProperties getKafkaProducerConfig() {
        return dataTransferCenter.kafka.getProducer();
    }

    public ConsumerProperties getKafkaConsumerConfig() {
        return dataTransferCenter.kafka.getConsumer();
    }

}
