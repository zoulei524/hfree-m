package me.zoulei.dbc.ui.components.samples;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class TextJProgressBar {
    public void init() {
        JFrame jFrame = new JFrame("test JProgress");
        JCheckBox jCheckBox = new JCheckBox("不确定进度");
        JCheckBox jCheckBox2 = new JCheckBox("不绘制边框");

        JProgressBar jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        // 处理复选框的点击行为
        jCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取一下复选框有没有选中
                boolean selected = jCheckBox.isSelected();
                // 设置当前进度条不确定进度
                jProgressBar.setIndeterminate(selected);
                // 进度百分比是否展示
                jProgressBar.setStringPainted(!selected);
            }
        });
        jCheckBox2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                boolean selected = jCheckBox2.isSelected();
                jProgressBar.setBorderPainted(selected);
            }
        });
        jProgressBar.setStringPainted(true);
        jProgressBar.setBorderPainted(true);

        Box createVerticalBox = Box.createVerticalBox();
        createVerticalBox.add(jCheckBox);
        createVerticalBox.add(jCheckBox2);
//      jFrame.add(createVerticalBox, BorderLayout.CENTER);
//      jFrame.add(jProgressBar, BorderLayout.EAST);

        jFrame.setLayout(new FlowLayout());
        jFrame.add(createVerticalBox);
        jFrame.add(jProgressBar);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
//      // 循环修改进度条的进度
        for (int i = 0; i < 100; i++) {
            jProgressBar.setValue(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] age) {
        new TextJProgressBar().init();
    }
}