package me.zoulei.ui.example;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

/**
 * 测试类 实现第四列第二行为下拉框，其余行为文本框 第五列一列为下拉框
 */
public class TableWithComboBox2 {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 484, 361);
		frame.getContentPane().add(panel);
		frame.setVisible(true);

		String[][] str = { { "小贾", "13", "160", "帽子", "" }, { "阿P", "16", "165", "rap", "" },
				{ "小李", "19", "158", "做头发", "" } };
		String[] title = { "姓名", "年龄", "身高", "爱好1", "爱好2" };
		JTable table = new JTable(str, title);// 测试表格
		table.putClientProperty("terminateEditOnFocusLost", true);
		table.setFont(new Font("微软雅黑", Font.PLAIN, 12));

		JScrollPane scrollPane_1 = new JScrollPane(table);// 滚动组件
		scrollPane_1.setBounds(0, 0, 484, 361);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane_1);

		JBoxTestCell jc = new JBoxTestCell();// 第四列第二行为下拉框，其余行为文本框
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(2).setCellEditor(jc);

		JButton btn = new JButton("删除");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				//dtm.removeRow(row);
			}
		});
		panel.add(btn);

		String[] str2 = { "rap", "手指舞", "理发", "冲天炮" };
		JComboBox jbox = new JComboBox(str2);
		jbox.setSelectedIndex(0);
		TableColumnModel col = table.getColumnModel();
		col.getColumn(4).setCellEditor(new DefaultCellEditor(jbox));// 设置第5列一列为下拉框
	}
}



/**
 * 自定义celleditor实现
 * 指定单元格设置下拉框，其他单元格设置文本框
 *
 */
class JBoxTestCell extends AbstractCellEditor implements TableCellEditor {
	int row;
	private JTable table;
	private JPanel p1;
	private JComboBox jbox;
	private Object val = null;
	private JTextField jt_0 = new JTextField();
	private JTextField jt_2 = new JTextField();

	public JBoxTestCell() {
		p1 = new JPanel(new BorderLayout());

		p1.setPreferredSize(new Dimension(300, 30));
		String[] interest1 = { "rap", "手指舞", "理发", "冲天炮" };
		jbox = new JComboBox(interest1);
		jbox.setPreferredSize(new Dimension(200, 25));
		jbox.setSelectedIndex(0);
		p1.add(jbox, BorderLayout.CENTER);
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	public void showSave() {
		jbox.setVisible(true);
	}

	public Object getCellEditorValue() {
		if (row == 1) {
			String v1 = jbox.getSelectedItem().toString();
			return v1;
		}
		String result = "";
		switch (this.row) {
		case 0:
			result = jt_0.getText().trim();
			break;
		case 2:
			result = jt_2.getText().trim();
			break;
		}
		return result;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.table = table;
		this.row = row;
		val = value;
		JTextField result = new JTextField();
		switch (this.row) {
		case 0:
			result = jt_0;
			break;
		case 1:
			return this.jbox;
		case 2:
			result = jt_2;
			break;
		}
		return result;
	}
}