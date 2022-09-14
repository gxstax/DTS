package com.heytea.dtc.output.kafka.consumer;

import com.heytea.dtc.config.ApplicationConfig;
import com.heytea.dtc.config.properties.kafka.CommonClientProperties;
import com.heytea.dtc.config.properties.kafka.ConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * Kafka 消费者（just a demo）
 * </p>
 *
 * @author Ant
 * @since 2022/1/12 2:50 下午
 */
@Slf4j
@Component
public class KafkaMessageConsumer implements ApplicationListener<ContextRefreshedEvent> {

    private Thread thread = null;

    private Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

    int count = 0;

    private final CommonClientProperties commonClientProperties;
    private final ConsumerProperties consumerProperties;

    private KafkaConsumer<String, String> consumer;

    public KafkaMessageConsumer(ApplicationConfig applicationConfig) {
        this.commonClientProperties = applicationConfig.getKafkaCommonConfig();
        this.consumerProperties = applicationConfig.getKafkaConsumerConfig();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Properties props = new Properties();
        props.putAll(commonClientProperties.getCommonClientConfig());
        props.putAll(consumerProperties.getConsumerConfig());
        this.consumer= new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("heytea"));
        thread = new Thread(this::consumer);
        thread.start();
    }

    private void consumer() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));

                    // 模拟业务处理
                    System.out.printf("topic[heytea]-consumer: offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

                    if (count % 100 == 0) {
                        // 异步提交
                        consumer.commitAsync(offsets, null);
                    }
                    count ++;
                }
            }
        } catch (Exception e) {
            // TODO 异常处理
        } finally {
            consumer.commitSync(offsets);
            consumer.close();
        }
    }

}
