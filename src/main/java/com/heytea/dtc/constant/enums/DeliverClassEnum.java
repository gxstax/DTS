package com.heytea.dtc.constant.enums;

import com.heytea.dtc.exception.DTCException;
import com.heytea.dtc.output.kafka.producer.KafkaProducer;

import java.util.Arrays;

/**
 * <p>
 * 消息投递方
 * </p>
 *
 * @author Ant
 * @since 2022/1/3 6:52 下午
 */
public enum DeliverClassEnum {

    KAFKA("kafka", KafkaProducer.class),
    ROCKET_MQ("rocketMQ", null),
//    RABBIT_MQ("rabbitMQ", RabbitMqProducer.class)

    ;
    private String deliverName;
    private Class clazz;

    DeliverClassEnum(String deliverName, Class clazz) {
        this.deliverName = deliverName;
        this.clazz = clazz;
    }


    public static Class getClassFromDeliver(String deliver) {
        return Arrays.stream(DeliverClassEnum.values())
                .filter(deliverclass -> deliverclass.deliverName.equals(deliver)).findAny()
                .orElseThrow(() -> new DTCException("指定的投递方式[" + deliver + "]未实现！")).clazz;
    }

}
