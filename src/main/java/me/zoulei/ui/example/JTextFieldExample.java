package me.zoulei.ui.example;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JTextFieldExample {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;

   public JTextFieldExample(){
      prepareGUI();
   }
   public static void main(String[] args){
       JTextFieldExample  swingControlDemo = new JTextFieldExample();      
      swingControlDemo.showTextFieldDemo();
   }
   private void prepareGUI(){
      mainFrame = new JFrame("Java Swing JTextField示例");
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
      
      controlPanel.setLayout(new FlowLayout());////FlowLayout是默认布局，它以方向流布局组件。

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
   private void showTextFieldDemo(){
      headerLabel.setText("Control in action: JTextField"); 

      JLabel  namelabel= new JLabel("用户名: ", JLabel.RIGHT);
      JLabel  passwordLabel = new JLabel("密码: ", JLabel.CENTER);
      final JTextField userText = new JTextField(6);
      final JPasswordField passwordText = new JPasswordField(6);      

      JButton loginButton = new JButton("登录");
      loginButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {     
            String data = "用户名： " + userText.getText();
            data += ", 密码: " + new String(passwordText.getPassword()); 
            statusLabel.setText(data);        
         }
      }); 
      controlPanel.add(namelabel);
      controlPanel.add(userText);
      controlPanel.add(passwordLabel);       
      controlPanel.add(passwordText);
      controlPanel.add(loginButton);
      mainFrame.setVisible(true);  
   }
}
//更多请阅读：https://www.yiibai.com/swing/swing_jtextfield.html

