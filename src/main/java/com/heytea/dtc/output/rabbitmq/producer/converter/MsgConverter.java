package com.heytea.dtc.output.rabbitmq.producer.converter;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 消息解析器
 * </p>
 *
 * @author Ant
 * @since 2022-04-11 18:37
 */
public class MsgConverter implements MessageConverter {

    /**
     * <p>
     * 消息发送解析器
     * </p>
     *
     * @param object
     * @param messageProperties
     * @return org.springframework.amqp.core.Message
     */
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(JSON.toJSONBytes(object), messageProperties);
    }

    /**
     * <p>
     * 消息接收解析
     * </p>
     *
     * @param message
     * @return java.lang.Object
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String messageBody = null;
        try {
            messageBody = new String(message.getBody(), "UTF-8");
            if (messageBody.contains("\\")) {
                messageBody = StringEscapeUtils.unescapeJava(messageBody);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return messageBody.getBytes();
    }
}
