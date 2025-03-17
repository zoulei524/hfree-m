package me.zoulei.ui.example;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
 
public class JComboBoxTest  extends JFrame implements KeyListener {
 
	private JComboBox cbx;
    private JTextField jtf;
    public JComboBoxTest() {
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(null);
        cbx = new JComboBox(getItems());
        cbx.setEditable(true);
        cbx.setBounds(20, 20, 80, 20);
        jtf = (JTextField)cbx.getEditor().getEditorComponent();
        jtf.addKeyListener(this);
        c.add(cbx);
        setVisible(true);
    }

    public void keyPressed(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    @SuppressWarnings("unchecked")
    public void keyReleased(KeyEvent e) {
        Object obj = e.getSource();
        if (obj == jtf) {
            String key = jtf.getText();
            cbx.removeAllItems();
            for (Object item : getItems()) {
                if (((String)item).startsWith(key)) { //这里是包含key的项目都筛选出来，可以把startsWith改成contains就是筛选以key开头的项目
                    cbx.addItem(item);
                }
            }
            jtf.setText(key);
        }
    }
    public Object[] getItems() {
        return new Object[] {
                "abcd", "acdef", "cdefg", "defg"
        };
    }
    public static void main(String[] args) {
        new JComboBoxTest();
    }
}


