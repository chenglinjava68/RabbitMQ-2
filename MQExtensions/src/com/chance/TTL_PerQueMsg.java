package com.chance;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Chance on 16/08/26.
 */
public class TTL_PerQueMsg {

    public static String QUEUE_NAME = "QUEUE_NAME";

    public static void main(String[] args) throws IOException, TimeoutException {
        //消息发送：
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.34.10.245");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> settings = new HashMap<String, Object>();
//        settings.put("x-max-length", 10);   //设置消息队列最大长度为10
//        settings.put("x-max-length-bytes", 1000000);   //设置消息队列最大不超过1000000字节
        settings.put("x-message-ttl", 60000);   //在QUEUE_NAME超过60秒的消息被丢弃
        channel.queueDeclare(QUEUE_NAME, false, false, false, settings);

        String message = "hello world!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        channel.close();
        connection.close();

    }
}
