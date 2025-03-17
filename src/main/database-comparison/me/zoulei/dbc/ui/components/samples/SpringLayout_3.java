package me.zoulei.dbc.ui.components.samples;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class SpringLayout_3 extends JFrame {
    
    private static final long serialVersionUID = -6045918631932051025L;
    
    public SpringLayout_3() {
        // TODO Auto-generated constructor stub
        Container container = getContentPane();
        SpringLayout springLayout = new SpringLayout();
        container.setLayout(springLayout);
        JLabel topicLabel = new JLabel("主题 :");
        JLabel contentLabel = new JLabel("内容 :");
        final JTextField textField = new JTextField(30);
        JTextArea textArea = new JTextArea(3, 30);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);
        textArea.setLineWrap(true);
        final JButton confirmButton = new JButton("取消");
        JButton cancelButton = new JButton("确认");
        Spring st = Spring.constant(10);
        Spring st2 = Spring.constant(30);
        container.add(topicLabel);
        springLayout.putConstraint(SpringLayout.NORTH, topicLabel, st,SpringLayout.NORTH, container);
        springLayout.putConstraint(SpringLayout.WEST, topicLabel, st,SpringLayout.WEST, container);
        container.add(textField);
        springLayout.putConstraint(SpringLayout.WEST, textField, st2, SpringLayout.EAST, topicLabel);
        springLayout.putConstraint(SpringLayout.NORTH, textField, 0,SpringLayout.NORTH, topicLabel);
        springLayout.putConstraint(SpringLayout.EAST, textField, Spring.minus(st),SpringLayout.EAST, container);
        container.add(contentLabel);
        springLayout.putConstraint(SpringLayout.WEST, contentLabel, 0, SpringLayout.WEST, topicLabel);
        springLayout.putConstraint(SpringLayout.NORTH, contentLabel, st, SpringLayout.SOUTH, topicLabel);
        container.add(scrollPane);
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, contentLabel);
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, textField);
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, Spring.minus(st), SpringLayout.EAST, container);
        container.add(confirmButton);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, Spring.minus(st), SpringLayout.NORTH, confirmButton);
        springLayout.putConstraint(SpringLayout.EAST, confirmButton, Spring.minus(st), SpringLayout.EAST, container);
        springLayout.putConstraint(SpringLayout.SOUTH, confirmButton, Spring.minus(st), SpringLayout.SOUTH, container);
        container.add(cancelButton);
        springLayout.putConstraint(SpringLayout.EAST, cancelButton, Spring.minus(st), SpringLayout.WEST, confirmButton);
        springLayout.putConstraint(SpringLayout.NORTH, cancelButton, 0, SpringLayout.NORTH, confirmButton);
        // make the text field focused every time the window is activated
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // TODO Auto-generated method stub
                textField.requestFocus();
            }
        });
    }
    
    public static void main(String[] args) {

        // TODO Auto-generated method stub

        SpringLayout_3 frame = new SpringLayout_3();

        frame.setTitle("使用弹簧布局管理器");

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setBounds(100, 100, 600, 300);

    }
}