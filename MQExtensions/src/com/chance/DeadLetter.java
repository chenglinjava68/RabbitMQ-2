package com.chance;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Chacne on 2016/8/8.
 */
public class DeadLetter {

    private final static String EXCHANGE_NAME = "EXCHANGE_NAME";
    private final static String QUEUE_NAME = "QUEUE_NAME";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.34.10.245");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");//声明交换机为“direct”类型
        Map<String, Object> settings = new HashMap<String, Object>();
        settings.put("x-dead-letter-exchange", EXCHANGE_NAME);//给交换机添加DLXs
        settings.put("x-dead-letter-routing-key", "some-routing-key");//给特定路由添加DLXs
        channel.queueDeclare(QUEUE_NAME, false, false, false, settings);

        String message = new Date().toLocaleString() + " : log something";
        // 往转发器上发送消息
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
