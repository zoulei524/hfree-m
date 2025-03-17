package me.zoulei.ui.example;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
//from  w  w  w  .j  av a 2s.c o m
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class ComplexCellRenderer implements ListCellRenderer {
  protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

  public Component getListCellRendererComponent(JList list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {
    Font theFont = null;
    Color theForeground = null;
    Icon theIcon = null;
    String theText = null;

    JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
        isSelected, cellHasFocus);

    if (value instanceof Object[]) {
      Object values[] = (Object[]) value;
      theFont = (Font) values[0];
      theForeground = (Color) values[1];
      theIcon = (Icon) values[2];
      theText = (String) values[3];
    } else {
      theFont = list.getFont();
      theForeground = list.getForeground();
      theText = "";
    }
    if (!isSelected) {
      renderer.setForeground(theForeground);
    }
    if (theIcon != null) {
      renderer.setIcon(theIcon);
    }
    renderer.setText(theText);
    renderer.setFont(theFont);
    return renderer;
  }
}

public class Main {
  public static void main(String args[]) {
    Object elements[][] = {
        { new Font("Helvetica", Font.PLAIN, 20), Color.RED, new MyIcon(), "A" },
        { new Font("TimesRoman", Font.BOLD, 14), Color.BLUE, new MyIcon(), "A" },
        { new Font("Courier", Font.ITALIC, 18), Color.GREEN, new MyIcon(), "A" },
        { new Font("Helvetica", Font.BOLD | Font.ITALIC, 12), Color.GRAY, new MyIcon(), "A" },
        { new Font("TimesRoman", Font.PLAIN, 32), Color.PINK, new MyIcon(), "A" },
        { new Font("Courier", Font.BOLD, 16), Color.YELLOW, new MyIcon(), "A" },
        { new Font("Helvetica", Font.ITALIC, 8), Color.DARK_GRAY, new MyIcon(), "A" } };

    JFrame frame = new JFrame("Complex Renderer");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    ListCellRenderer renderer = new ComplexCellRenderer();
    JComboBox comboBox = new JComboBox(elements);
    comboBox.setRenderer(renderer);
    frame.add(comboBox, BorderLayout.NORTH);
    
    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}

class MyIcon implements Icon {

  public MyIcon() {
  }

  public int getIconHeight() {
    return 20;
  }

  public int getIconWidth() {
    return 20;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    g.setColor(Color.RED);
    g.drawRect(0, 0, 25, 25);
  }
}