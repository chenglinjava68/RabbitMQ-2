package com.chance.mq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * Created by Chance on 16/08/09.
 */
public class MqFrame extends JFrame {

    private JLabel jl_msg = new JLabel("要发送的消息：", SwingConstants.LEFT);
    private JTextField jtf_msg = new JTextField("Default WSAP_Cloud Msg", 1);
    public JButton jb_send = new JButton("发送消息");
    public JButton jb_clear = new JButton("清空消息");
    private JTextArea jta_mq = new JTextArea("——RabbitMQ测试正在进行初始化……");
    public JCheckBox jcb1 = new JCheckBox("key1", true);
    public JCheckBox jcb2 = new JCheckBox("key2", true);

    public MqFrame() throws HeadlessException {
        super("RabbitMQ消息测试");
        setSize(1000, 600);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        jPanel.add(jl_msg);
        jPanel.add(jtf_msg);
        jPanel.add(jb_send);
        jPanel.add(jb_clear);
        jPanel.add(jcb1);
        jPanel.add(jcb2);
        this.add(jPanel);

        jta_mq.setRows(100);
        jta_mq.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(jta_mq);
        jScrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(jScrollPane);

//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

//        registEvents(); //添加事件响应
    }

    public void registEvents(){
        jb_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //具体的发消息事件
                String msg = getSendMsg();
                System.out.println("——正在发送"+ msg+ "到WSAP消息队列……");
            }
        });
        jb_clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMQinfo();
            }
        });
    }

    public String getSendMsg(){
        return this.jtf_msg.getText();
    }

    public void updateMQinfo(String newMsg){
        String info = this.jta_mq.getText();
        this.jta_mq.setText(info+"\n——"+ new Date().toString()+"："+ newMsg);
    }

    public void clearMQinfo(){
        this.jta_mq.setText("");
    }
}
