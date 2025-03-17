package me.zoulei.ui.components;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import lombok.Data;
import me.zoulei.MainApp;

/**
 * 2023年9月14日11:17:12 zoulei
 * 表格组件，用于配置表格的参数  结构： JPanel（布局是BorderLayout，不留空隙）,里面是JScrollPane,在里面是JTable
 */
@Data
public class GridComponent {
	public EditorGrid editorGrid;
	public void setComp(String tablename,String owner) {
		if(this.editorGrid!=null) {
			MainApp.mainFrame.remove(this.editorGrid);
		}
		this.editorGrid = new EditorGrid(tablename,owner);
		MainApp.mainFrame.add(this.editorGrid,BorderLayout.CENTER);
		this.editorGrid.setHeaderEditable(true);
		this.editorGrid.setTableEditable(true);
		TitledBorder  blackline = BorderFactory.createTitledBorder("表格属性配置");
		blackline.setTitleFont(new Font("黑体", 0, 16));
		this.editorGrid.setBorder(blackline);
	}
}
