package com.chance;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Chance on 16/08/26.
 */
public class TTL_PerMsg {

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
        channel.queueDeclare(QUEUE_NAME, false, false, false, settings); //参数2声明队列持久化

//        String message = "hello world!";
        byte[] messageBodyBytes = "Hello, world!".getBytes();
//        AMQP.BasicProperties properties = new AMQP.BasicProperties();
//        properties.setExpiration("60000");//setExpiration无法使用？
        AMQP.BasicProperties properties = new AMQP.BasicProperties(null, null, null, null, null,
                null, null, "60000", null, null, null, null, null, null);
        channel.basicPublish("", QUEUE_NAME, properties, messageBodyBytes);
        channel.close();
        connection.close();

    }
}
