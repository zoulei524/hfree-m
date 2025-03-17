package me.zoulei.ui.example;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingJLabel {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;

   public SwingJLabel(){
      prepareGUI();
   }
   public static void main(String[] args){
       SwingJLabel  swingControlDemo = new SwingJLabel();      
      swingControlDemo.showLabelDemo();
   }
   private void prepareGUI(){
      mainFrame = new JFrame("Java Swing JLable示例");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(3, 1));

      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    
      statusLabel.setSize(350,100);
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
   private void showLabelDemo(){
      headerLabel.setText("Control in action: JLabel");      
      JLabel label  = new JLabel("", JLabel.CENTER);        
      label.setText("这是一个简单的Swing JLable示例(by yiibai.com)");
      label.setOpaque(true);
      label.setBackground(Color.GRAY);
      label.setForeground(Color.WHITE);
      controlPanel.add(label);

      mainFrame.setVisible(true);  
   }
}
//更多请阅读：https://www.yiibai.com/swing/swing_jlabel.html

