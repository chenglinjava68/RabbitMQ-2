package com.chance.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by Chance on 16/08/09.
 */
public class EasyMQ {

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    private String user_name;  //消息服务器用户名
    private String user_pwd;  //消息服务器用户密码
    private String mq_host;  //消息服务器IP地址
    private String mq_name = "WSAP_Cloud";  //消息队列名称
    //设置消息主题标识符，消息标识符用"."隔开，类似打印日志的debug、info、warning、error的选择
    private String[] routing_keys = new String[]{"key11.key12",
            "key21.key22", "key11.key22", "key21.key12"};

    public EasyMQ() {
        //初始化消息队列连接信息
        factory = new ConnectionFactory();
        setUser("admin", "admin");
        setHost("10.34.10.245");
//        setHost("localhost");
    }

    /**
     * 创建消息队列连接和频道
     * @throws IOException
     * @throws TimeoutException
     */
    public void connectMQ() throws IOException, TimeoutException {
        connection = factory.newConnection();
        channel = connection.createChannel();
        // 声明转发器
        channel.exchangeDeclare(mq_name, "topic");
    }

    /**
     * 销毁消息队列连接和频道
     * @throws IOException
     * @throws TimeoutException
     */
    public void disconnectMQ() throws IOException, TimeoutException {
        this.channel.close();
        this.connection.close();
    }

    /**
     * 示例消息发送，具体按照需求
     * @param msg
     * @param routing_key
     * @throws IOException
     */
    public void sendToMQ(String msg, String routing_key) throws IOException {
//        for (String routing_key : routing_keys) {
//            String msg = UUID.randomUUID().toString();
            channel.basicPublish(mq_name, routing_key, null, msg
                    .getBytes());
            System.out.println(" [MQ] Sent routingKey = "+ routing_key+ " ,msg = "+ msg+ ".");
//        }
    }

    /**
     * 示例消息接收，具体按照需求
     * @param routingKeyRegExp "*"可以匹配一个标识符，"#"可以匹配0个或多个标识符。
     * @throws IOException
     * @throws InterruptedException
     */
    public String getFromMQ(String routingKeyRegExp) throws IOException, InterruptedException {
        // 随机生成一个队列
        String queueName = channel.queueDeclare().getQueue();

        //接收所有与routingKeyRegExp(主题选择键)相关的消息
        channel.queueBind(queueName, mq_name, routingKeyRegExp);
        System.out.println(" [MQ] Waiting for messages about kernel. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

//        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            String routingKey = delivery.getEnvelope().getRoutingKey();

            System.out.println(" [MQ] Received routingKey = " + routingKey
                    + ",msg = " + message + ".");
//        }
        return message;
    }

    /**
     * 设置连接MQ服务器的账户
     * @param user_name
     * @param user_pwd
     */
    public void setUser(String user_name, String user_pwd) {
        this.user_name = user_name;
        this.user_pwd = user_pwd;
        this.factory.setUsername(user_name);
        this.factory.setPassword(user_pwd);
    }

    /**
     * 设置连接MQ服务器的IP
     * @param mq_host
     */
    public void setHost(String mq_host) {
        this.mq_host = mq_host;
        this.factory.setHost(mq_host);
    }
}
