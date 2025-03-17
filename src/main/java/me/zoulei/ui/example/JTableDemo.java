package me.zoulei.ui.example;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class JTableDemo {
	public static void main(String[] agrs) {
		JFrame frame = new JFrame("学生成绩表");
		frame.setLayout(null);
		JPanel p = new JPanel();
		p.setBounds(0, 40, 600, 500);
		frame.setSize(1500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		Object[][] tableDate = new Object[15][8];
		for (int i = 0; i < 15; i++) {
			tableDate[i][0] = "1000" + i;
			for (int j = 1; j < 8; j++) {
				tableDate[i][j] = 0;
			}
		}
		String[] name = { "学号", "软件工程", "Java", "网络", "数据结构", "数据库", "总成绩", "平均成绩" };
		JTable table = new JTable(tableDate, name);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane sp = new JScrollPane(table);
		
		p.add(sp);
		contentPane.add(p);
		frame.setVisible(true);
	}
}
