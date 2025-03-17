package me.zoulei.dbc.ui.components.north;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang.StringUtils;

import me.zoulei.backend.jdbc.utils.CommQuery;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.dbc.ui.components.center.SQLAdapterUtil;

public class TableSelectUI extends JDialog{

	private static final long serialVersionUID = -7011044858468494782L;
	
	//已选表格
	static List<String> checked_tables = new ArrayList<String>();

	public TableSelectUI(SchemaSelectComponent db1, SchemaSelectComponent db2) throws Exception {
		super(MainPanel.mainFrame,"选择表",true);
		
		JDialog _this = this;
		
		if(db1.dbc==null) {
			throw new RuntimeException("库1未连接！");
		}
		if(db2.dbc==null) {
			throw new RuntimeException("库2未连接！");
		}

	    
	    
	    
	    //添加表
	    String tableSQL1 = String.format(SQLAdapterUtil.getTableSQL(db1.dbc,null), db1.selectSchema);
	    //第一个数据库信息
		CommQuery cq1 = new CommQuery(db1.dbc);
		List<HashMap<String, String>> tablelist1 = cq1.getListBySQL2(tableSQL1);
		//表名处理
		HashMap<String, String> tableMap1 = new LinkedHashMap<String, String>();
		for(HashMap<String, String> t : tablelist1) {
			tableMap1.put(t.get("table_name"), t.get("table_comment"));
		}
		
		
		String tableSQL2 = String.format(SQLAdapterUtil.getTableSQL(db2.dbc,null), db2.selectSchema);
	    //第二个数据库信息
		CommQuery cq2 = new CommQuery(db2.dbc);
		List<HashMap<String, String>> tablelist2 = cq2.getListBySQL2(tableSQL2);
		//表名处理
		for(HashMap<String, String> t : tablelist2) {
			if(StringUtils.isEmpty(t.get("table_name"))) {
				tableMap1.put(t.get("table_name"), t.get("table_comment"));
			}
		}
		
		
		// 创建编辑器窗口的内容面板
	    JPanel editorPanel = new JPanel(new BorderLayout());
	    this.setContentPane(editorPanel);
		
		
		
		int col = 5;
		//表名的面板
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new GridLayout(0,col));
		//添加表名复选框
		List<JCheckBox> cbList = new ArrayList<JCheckBox>();
		for (String tablename : tableMap1.keySet()) {
			//String c = tableMap1.get(tablename);
			JCheckBox checkbox = null;
			if(checked_tables.contains(tablename)) {
				checkbox = new JCheckBox(tablename,true);
			}else {
				checkbox = new JCheckBox(tablename);
			}
			cbList.add(checkbox);
			checkboxPanel.add(checkbox);
		}
		
		JScrollPane scrollPane = new JScrollPane(checkboxPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
	    this.add(scrollPane,BorderLayout.CENTER);
	    
	    //确定按钮
	    // 创建一个按钮，点击后关闭编辑器窗口
	    JButton closeEditorButton = new JButton("确定");
	    closeEditorButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	checked_tables.clear();
	        	StringBuilder tables = new StringBuilder();
	        	for (JCheckBox jCheckBox : cbList) {
	        		if(jCheckBox.isSelected()) {
	        			tables.append("'"+jCheckBox.getText()+"',");
	        			checked_tables.add(jCheckBox.getText());
	        		}
				}
	        	if(tables.length()>0) {
	        		tables.deleteCharAt(tables.length()-1);
	        		SQLAdapterUtil.table = tables.toString();
	        	}else {
	        		SQLAdapterUtil.table = "";
	        	}
	        	//获取复选内容
	        	_this.setVisible(false);
	        	_this.dispose();
	        }
	    });
	    this.add(closeEditorButton,BorderLayout.SOUTH);
	    
	    
	    //清空
	    JButton clearButton = new JButton("清空");
	    clearButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	checked_tables.clear();
	        	SQLAdapterUtil.table = "";
	        	for (JCheckBox jCheckBox : cbList) {
	        		jCheckBox.setSelected(false);
				}
	        }
	    });
	    this.add(clearButton,BorderLayout.NORTH);

	    // 设置编辑器窗口的内容面板和关闭操作
	    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    this.setSize(1550, 950); // 设置编辑器窗口的大小
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸 
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    this.setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight())/2);
	    this.setVisible(true);
	}

	

	
}
