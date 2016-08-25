package com.chance;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * Created by Chance on 16/08/22.
 */
public class MyFrame extends JFrame {

    String data = "{\n" +
            "  \"head\": {\n" +
            "    \"type\": \"service\",\n" +
            "    \"deviceID\": \"\"\n" +
            "  },\n" +
            "  \"body\": {\n" +
            "    \"head\": {\n" +
            "      \"appid\": \"WSAPAPP\",\n" +
            "      \"cmdcode\": \"010001\",\n" +
            "      \"requestid\": \"FE09875DCA453345FE09875DCA453345\"\n" +
            "    },\n" +
            "    \"cmddata\": {\n" +
            "      \"servicename\": \"XXXXX\",\n" +
            "      \"params\": \"aa\"\n" +
            "    }\n" +
            "  }\n" +
            "}\n";
    public JButton jbt_set = new JButton("设 置");
    public JButton jbt_connect = new JButton("连 接");
    public JButton jbt_browse = new JButton("浏 览");
    public JButton jbt_send = new JButton("发 送");
    public JButton jbt_discon = new JButton("断 开");
    public JButton jbt_save = new JButton("保 存");
    public JButton jbt_history = new JButton("历 史");

    public JTextArea jta_recv = new JTextArea("消息接收区");
    public JTextArea jta_send = new JTextArea(data);

    public MyFrame() throws HeadlessException {
        setTitle("MQ消息模拟器");
        setSize(600, 960);
        setResizable(false);
        setLocationRelativeTo(null);
        URL url = this.getClass().getResource("res/zjft2_s.png");
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        setIconImage(img);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        jta_recv.setEditable(false);
        JScrollPane jsp_recv = new JScrollPane(jta_recv);
        jsp_recv.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp_recv.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        jta_recv.setLineWrap(true);
//        jta_recv.setWrapStyleWord(true);
        jsp_recv.setPreferredSize(new Dimension(600, 650));
        this.add(jsp_recv);

        JPanel jpn_control = new JPanel();
        jpn_control.setLayout(new GridLayout(1, 7));
        jpn_control.add(jbt_set);
        jpn_control.add(jbt_browse);
        jpn_control.add(jbt_connect);
        jbt_send.setBackground(Color.white);
        jbt_send.setFont(new Font("宋体", Font.BOLD, 16));
        jpn_control.add(jbt_send);
        jpn_control.add(jbt_discon);
        jpn_control.add(jbt_save);
        jpn_control.add(jbt_history);
        this.add(jpn_control);

        JScrollPane jsp_send = new JScrollPane(jta_send);
        jsp_send.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp_recv.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp_send.setPreferredSize(new Dimension(600, 310));
        this.add(jsp_send);

//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

}
