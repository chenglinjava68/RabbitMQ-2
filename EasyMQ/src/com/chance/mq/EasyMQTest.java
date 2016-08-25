package com.chance.mq;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Chance on 16/08/09.
 */
public class EasyMQTest {

    public static MqFrame mqFrame;
    public static EasyMQ easyMQ;

    public static String routingKey = "#";

    public static void main(String[] args) throws IOException, TimeoutException {
        mqFrame = new MqFrame();    //初始化视窗

        mqFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(mqFrame, "exit?",
                        "Info", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
//                    try {
//                        easyMQ.disconnectMQ();  //关闭MQ连接
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
                    System.exit(0);
                }
            }
        });

        easyMQ = new EasyMQ();
        easyMQ.connectMQ();   //开启MQ连接
        mqFrame.updateMQinfo("RabbitMQ 连接成功");

        new Thread(){   //消息接收线程
            @Override
            public void run() {
                super.run();
                while (true){
                    try {
                        String msg = easyMQ.getFromMQ(getRoutingKey());  //接收routingKey主题消息
                        mqFrame.updateMQinfo(msg+ "("+ getRoutingKey()+ ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        mqFrame.jb_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //具体的发消息事件
                String msg = mqFrame.getSendMsg();
                System.out.println("——正在发送" + msg + "到WSAP消息队列……");
                try {
                    easyMQ.sendToMQ(msg, getRoutingKey());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        mqFrame.jb_clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mqFrame.clearMQinfo();
            }
        });

        mqFrame.jcb1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeRoutingKey();
            }
        });

        mqFrame.jcb2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeRoutingKey();
            }
        });
    }

    public static String getRoutingKey() {
        return routingKey;
    }

    public static void setRoutingKey(String routingKey) {
        EasyMQTest.routingKey = routingKey;
    }

    public static void changeRoutingKey(){
        if(mqFrame.jcb1.isSelected()){
            if(mqFrame.jcb2.isSelected()){
                setRoutingKey("key11.key12");
            }else {
                setRoutingKey("key21.key22");
            }
        }else {
            if(mqFrame.jcb2.isSelected()){
                setRoutingKey("key11.key22");
            }else {
                setRoutingKey("key21.key12");
            }
        }
    }
}
