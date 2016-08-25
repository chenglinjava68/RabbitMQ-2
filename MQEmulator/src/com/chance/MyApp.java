package com.chance;

import com.rabbitmq.client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.TimeoutException;

/**
 * Created by Chance on 16/08/23.
 */
public class MyApp implements ActionListener {

    public static void main(String[] args) {
        new MyApp();
    }

    public static String MQ_IP = "10.34.10.245";
    public static String MQ_QUEUE = "MQ_NAME";
    public static String MQ_USERNAME = "admin";
    public static String MQ_PASSWORD = "admin";
    public static Connection connection;
    public static Channel channel;

    public static final int MSG_RECV = 1;
    public static final int MSG_SEND = 2;
    public static Boolean FLAG = true;

    public static MyFrame myFrame;
    public static JTextArea jta_recv;
    public static JTextArea jta_send;

    public MyApp() {
        initUIStyle();
        myFrame = new MyFrame();
        myFrame.jbt_set.addActionListener(this);
        myFrame.jbt_connect.addActionListener(this);
        myFrame.jbt_browse.addActionListener(this);
        myFrame.jbt_send.addActionListener(this);
        myFrame.jbt_discon.addActionListener(this);
        myFrame.jbt_save.addActionListener(this);
        myFrame.jbt_history.addActionListener(this);
        jta_recv = myFrame.jta_recv;
        jta_send = myFrame.jta_send;
        jta_recv.setText("--" + new Date().toString() + "：MQ消息模拟器启动成功！");
//        myFrame.setVisible(true);
    }

    /**
     * 对UI事件进行相应操作处理
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myFrame.jbt_set) {
            //属性设置对话框
            final MyDialog myDialog = new MyDialog();
            myDialog.setJtf_ip(MQ_IP);
            myDialog.setJtf_queue(MQ_QUEUE);
            myDialog.jbt_confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MQ_IP = myDialog.getJtf_ip();
                    MQ_QUEUE = myDialog.getJtf_queue();
                    myDialog.dispose();
                    disconMQ();    //重新连接监听MQ
                    connectMQ();
                    recvMsg();
                }
            });
            myDialog.jbt_concel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    myDialog.dispose();
                }
            });
            myDialog.setVisible(true);

        } else if (e.getSource() == myFrame.jbt_connect) {
            connectMQ();    //MQ连接
            recvMsg();  //监听消息

        } else if (e.getSource() == myFrame.jbt_browse) {
            //MQ报文加载
            openFile(MSG_SEND);

        } else if (e.getSource() == myFrame.jbt_send) {
            //MQ报文发送
            String message = jta_send.getText();
            try {
                channel.basicPublish("", MQ_QUEUE,
                        MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
//                System.out.println(" [x] Sent '" + message + "'");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } else if (e.getSource() == myFrame.jbt_discon) {
            disconMQ(); //MQ断开连接

        } else if (e.getSource() == myFrame.jbt_save) {
            //保存MQ消息
            JFileChooser jfc_file = new JFileChooser();
            jfc_file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc_file.setMultiSelectionEnabled(false);
            jfc_file.showOpenDialog(myFrame);

            if (jfc_file.getSelectedFile() != null) {
                String filePath = jfc_file.getSelectedFile().getAbsolutePath();
                try {
                    FileOutputStream fos = new FileOutputStream(filePath);
                    fos.write(jta_recv.getText().getBytes());
                    fos.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        } else if (e.getSource() == myFrame.jbt_history) {
            //MQ历史消息
            openFile(MSG_RECV);
        } else {
            System.out.println("No ActionCommand Matched!");
        }
    }

    /**
     * 连接RabbitMQ服务器
     */
    public static void connectMQ() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MQ_IP);
        factory.setUsername(MQ_USERNAME);
        factory.setPassword(MQ_PASSWORD);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(MQ_QUEUE, true, false, false, null);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (TimeoutException e1) {
            e1.printStackTrace();
        }
        jta_recv.append("\n--" + new Date().toString() + "："+MQ_IP +"连接成功！");
    }

    /**
     * 监听消息，并对UI更新
     */
    public static void recvMsg() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (FLAG) {
                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    QueueingConsumer.Delivery delivery = null;
                    try {
                        channel.basicConsume(MQ_QUEUE, true, consumer);
                        delivery = consumer.nextDelivery();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String msg = new String(delivery.getBody());
                    jta_recv.append("\n--" + new Date().toString() + "--[" + MQ_QUEUE + "]：\n" + msg);
//                    System.out.println(" [x] Received '" + msg + "'");
                }
            }
        }.start();
    }

    public static void disconMQ() {
        FLAG = false;
        try {
            channel.close();
            connection.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (TimeoutException e1) {
            e1.printStackTrace();
        }
        jta_recv.append("\n--" + new Date().toString() + "：已断开与RabbitMQ服务器连接！");
    }

    /**
     * 载入文件，根据msgType 载入相应位置
     * @param msgType
     */
    public static void openFile(int msgType) {
        JTextArea jta_temp;
        JFileChooser jfc_file = new JFileChooser();
        jfc_file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc_file.setMultiSelectionEnabled(false);
        if (msgType == MSG_SEND) {
            jfc_file.setDialogTitle("选择发送报文");
            jta_temp = jta_send;
        } else if (msgType == MSG_RECV) {
            jfc_file.setDialogTitle("选择历史报文");
            jta_temp = jta_recv;
        } else return;
        jfc_file.showOpenDialog(myFrame);

        if (jfc_file.getSelectedFile() != null) {
            String filePath = jfc_file.getSelectedFile().getAbsolutePath();
            System.out.println(filePath);
            try {
                FileInputStream fis = new FileInputStream(filePath);
                byte[] content = new byte[fis.available()];
                fis.read(content);
                jta_temp.setText(new String(content));
                jta_temp.setCaretPosition(0);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化APP全局风格
     */
    public static void initUIStyle() {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        Object key = null;
        Object value = null;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            value = UIManager.get(key);
            //设置全局的背景色
//            if (key instanceof String) {
//                if (((String) key).endsWith(".background")) {
//                    UIManager.put(key, Color.white);
//                }
//            }
            //设置全局的字体
            if (value instanceof Font) {
                UIManager.put(key, new Font("宋体", Font.PLAIN, 14));
            }
        }
    }
}
