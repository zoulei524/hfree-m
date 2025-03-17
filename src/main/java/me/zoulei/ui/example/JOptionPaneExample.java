package me.zoulei.ui.example;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JOptionPaneExample {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;

    public JOptionPaneExample() {
        prepareGUI();
    }

    public static void main(String[] args) {
        JOptionPaneExample swingControlDemo = new JOptionPaneExample();
        swingControlDemo.showDialogDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Java/Swing JOptionPane示例(yiibai.com)");
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(350, 100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    private void showDialogDemo() {
        headerLabel.setText("Control in action: JOptionPane");

        JButton okButton = new JButton("是");
        JButton javaButton = new JButton("是/否");
        JButton cancelButton = new JButton("是/否/取消");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "Welcome to yiibai.com");
            }
        });
        javaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int output = JOptionPane.showConfirmDialog(mainFrame, "点击一个按钮", "kaops.com",
                        JOptionPane.YES_NO_OPTION);

                if (output == JOptionPane.YES_OPTION) {
                    statusLabel.setText("选择：'是'");
                } else if (output == JOptionPane.NO_OPTION) {
                    statusLabel.setText("选择：'否'");
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int output = JOptionPane.showConfirmDialog(mainFrame, "点击一个按钮", "Kaops.com",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (output == JOptionPane.YES_OPTION) {
                    statusLabel.setText("选择：'是'");
                } else if (output == JOptionPane.NO_OPTION) {
                    statusLabel.setText("选择：'否'");
                } else if (output == JOptionPane.CANCEL_OPTION) {
                    statusLabel.setText("选择：'取消'");
                }
            }
        });
        controlPanel.add(okButton);
        controlPanel.add(javaButton);
        controlPanel.add(cancelButton);
        mainFrame.setVisible(true);
    }
}
//更多请阅读：https://www.yiibai.com/swing/swing_joptionpane.html

