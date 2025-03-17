package me.zoulei.ui.example;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
 
public class TableWithComboBox {
 
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		String[] columnIdentifiers = {"one", "two", "three"};//表头
		String[][] data = new String[5][3];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				if(j == 2) {
					continue;
				}
				data[i][j] = "asd";
			}
		}
		model.setDataVector(data, columnIdentifiers);
		table.setModel(model);
		//强烈建议用Vector集合来作为下拉框的选项值，方便删除修改下拉框中选项值的内容。
		Vector<String> vector = new Vector<String>();
		vector.add("a");
		vector.add("b");
		vector.add("c");
		
		final JComboBox<String> comboBox = new JComboBox<String>(vector);
		//下拉框监听
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println(comboBox.getSelectedItem());
				}
			}
		});
		//表格编辑器
		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);
		frame.setBounds(100, 100, 300, 200);
		frame.setVisible(true);
	}
}
