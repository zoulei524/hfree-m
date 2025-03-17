package me.zoulei.ui.components.south;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import lombok.Data;
import me.zoulei.MainApp;
import me.zoulei.ui.components.EditorGrid;

/**
 * 2023年9月14日11:17:12 zoulei
 * 表格组件，用于配置表格的参数  结构： JPanel（布局是BorderLayout，不留空隙）,里面是JScrollPane,在里面是JTable
 */
@Data
public class FlowGridComponent {
	public EditorGrid editorGrid;
	public void setComp(String tablename,String owner, FlowSearchComponentNorth flowSearchComponentNorth) {
		if(this.editorGrid!=null) {
			flowSearchComponentNorth.remove(this.editorGrid);
		}
		this.editorGrid = new EditorGrid(tablename,owner,true);
		flowSearchComponentNorth.add(this.editorGrid,BorderLayout.CENTER);
		this.editorGrid.setHeaderEditable(true);
		this.editorGrid.setTableEditable(true);
		TitledBorder  blackline = BorderFactory.createTitledBorder("业务表格属性配置");
		blackline.setTitleFont(new Font("黑体", 0, 16));
		if(MainApp.debugbBorder!=null) {
			this.editorGrid.setBorder(MainApp.debugbBorder);
		}else {
			this.editorGrid.setBorder(blackline);
		}
		this.editorGrid.setPreferredSize(new Dimension(MainApp.MAIN_WIDTH-40,310));
		
	}
}
