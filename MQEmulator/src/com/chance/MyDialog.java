package com.chance;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by Chance on 16/08/23.
 */
class MyDialog extends JDialog {

    public JTextField jtf_ip = new JTextField("10.34.10.245");
    public JTextField jtf_queue = new JTextField("MQ_NAME");

    public JButton jbt_confirm = new JButton("确 定");
    public JButton jbt_concel = new JButton("取 消");

    public MyDialog() {
        setTitle("MQ设置");
        setSize(200, 120);
        setResizable(false);
        setLocationRelativeTo(null);
        setModal(true);
        URL url = this.getClass().getResource("res/zjft2_s.png");
        Image img = Toolkit.getDefaultToolkit().getImage(url);
        setIconImage(img);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JLabel jlb_ip = new JLabel("MQ地址：");
        JLabel jlb_queue = new JLabel("MQ队列：");

        JPanel jpn_ip = new JPanel();
        jpn_ip.setLayout(new BoxLayout(jpn_ip, BoxLayout.X_AXIS));
        jpn_ip.add(jlb_ip);
        jpn_ip.add(jtf_ip);

        JPanel jpn_queue = new JPanel();
        jpn_queue.setLayout(new BoxLayout(jpn_queue, BoxLayout.X_AXIS));
        jpn_queue.add(jlb_queue);
        jpn_queue.add(jtf_queue);

        JPanel jpn_control = new JPanel();
        jpn_control.setLayout(new GridLayout(1, 2));
        jpn_control.add(jbt_confirm);
        jpn_control.add(jbt_concel);

        this.add(jpn_ip);
        this.add(jpn_queue);
        this.add(jpn_control);
//        setVisible(true);
    }

    public String getJtf_ip() {
        return jtf_ip.getText();
    }

    public void setJtf_ip(String jtf_ip) {
        this.jtf_ip.setText(jtf_ip);
    }

    public String getJtf_queue() {
        return jtf_queue.getText();
    }

    public void setJtf_queue(String jtf_queue) {
        this.jtf_queue.setText(jtf_queue);
    }
}
