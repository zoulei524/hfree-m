package me.zoulei.dbc.ui.components.samples;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class AddTitleBorder2JPanel {

   public static void main(String[] args) {
      createWindow();
   }

   private static void createWindow() {    
      JFrame frame = new JFrame("Swing AddTitleBorder2JPanel示例(yiibai.com)");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      createUI(frame);
      frame.setSize(560, 200);      
      frame.setLocationRelativeTo(null);  
      frame.setVisible(true);
   }

   private static void createUI(JFrame frame){
      //Create a border
      Border blackline = BorderFactory.createTitledBorder("标题");
      JPanel panel = new JPanel();
      LayoutManager layout = new FlowLayout();  
      panel.setLayout(layout);       

      JPanel panel1 = new JPanel();
      String spaces = "                   ";

      panel1.add(new JLabel(spaces + " 添加标题边框到JPanel " + spaces));  
      panel1.setBorder(blackline);

      panel.add(panel1);
      frame.getContentPane().add(panel, BorderLayout.CENTER);    
   }
}

