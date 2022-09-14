package com.heytea.dtc.output.kafka.producer.sender;

import com.heytea.dtc.config.properties.kafka.CommonClientProperties;
import com.heytea.dtc.config.properties.kafka.ProducerProperties;
import com.heytea.dtc.resources.ResourceStore;
import com.heytea.dtc.resources.partition.TopicPartitionRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * <p>
 * 消息生产
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 2:46 下午
 */
@Slf4j
public abstract class AbstracMessageSender<K, V> implements MessageProducerSender<K, V>, ApplicationListener<ContextClosedEvent> {
    /**
     * 配置信息
     */
    protected ProducerProperties producerProperties;
    protected CommonClientProperties commonClientProperties;
    protected List<TopicPartitionRule> topicPartitionRules;

    /**
     * 生产者对象
     */
    protected KafkaProducer<K, V> producer;

    /**
     * 回调函数
     */
    protected Callback callback;

    public AbstracMessageSender(ProducerProperties producerProperties,
                                CommonClientProperties commonClientProperties,
                                ResourceStore resourceStore,
                                Callback callback) {
        this.producerProperties = producerProperties;
        this.commonClientProperties = commonClientProperties;
        this.callback = callback;
        this.topicPartitionRules = resourceStore.getPartitionRule();
        this.producer = initProducer(producerProperties, commonClientProperties, this.topicPartitionRules);
    }

    /**
     * <p>
     * 初始化生产者
     * </p>
     *
     * @param producerProperties
     * @param commonClientProperties
     * @return {@link KafkaProducer}
     */
    protected KafkaProducer<K,V> initProducer(ProducerProperties producerProperties,
                                              CommonClientProperties commonClientProperties,
                                              List<TopicPartitionRule> topicPartitionRules) {
        // 叠加参数信息（这里put时要按照顺序--> 小范围覆盖大范围）
        Map<String, Object> configs = new HashMap<>();
        configs.putAll(commonClientProperties.getCommonClientConfig());
        configs.putAll(producerProperties.getProducerConfig());

        return new KafkaProducer<K, V>(configs);
    }

    /**
     * 发送消息
     *
     * <p>
     * 发消息最好适用带有回调的方式发送
     * -- 这里即使你没有传 callBack 也会构建一个默认的 CallBack 对象
     * 不建议使用无 CallBack 参数的 API
     * </p>
     *
     * @param record
     * @return {@link RecordMetadata}
     */
    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> record) {
        return producer.send(record, callback);
    }

    /**
     * 发送消息
     *
     * <p>
     * 发消息最好使用带有回调的方式发送
     * </p>
     *
     * @param record
     * @return {@link RecordMetadata}
     */
    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback) {
        return producer.send(record, callback);
    }

    /**
     * <p>
     * 关闭生产者
     * </p>
     *
     * @param contextClosedEvent
     * @return void
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        if (Objects.nonNull(producer)) {
            producer.close();
            log.info("Kafka producer[{}] 关闭！！！", producer);
        }
    }

}
