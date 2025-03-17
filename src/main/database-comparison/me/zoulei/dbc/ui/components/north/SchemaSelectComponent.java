package me.zoulei.dbc.ui.components.north;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.zoulei.MainApp;
import me.zoulei.backend.jdbc.utils.CommQuery;


/**
 *@ClassName: SchemaSelectComponent
 * 加载数据库模式  oracle和达梦，mysql。
 * 包含数据库连接和选择的模式名称。
 *@author zoulei
 *@date 2023年11月15日11:33:26
 */
public class SchemaSelectComponent {

	String[] items2 = new String[] {"sadsa","dsf","fsdfg","dsadsfx"};
	
//	List<Component> removeComponents = new ArrayList<Component>();
//	public void removeAll() {
//		//第一次进来没渲染，不需要移除。 
//		if(removeComponents.size()>0) {
//			removeComponents.forEach(c->{
//				this.north.remove(c);
//			});
//			MainPanel.mainFrame.getContentPane().repaint();
//		}
//		
//	}
	//选择对比的数据库模式
	public String selectSchema = "";
	
	
	Font font = new Font("宋体", Font.PLAIN, 18);
	//数据库
	public DataSourceDBC dbc;

	
	public void setComp(JPanel north2, DataSourceDBC dbc, String label) {
		this.dbc = dbc;
		//模式列表
		this.searchOwner();
		
		//this.north.add(north2,BorderLayout.CENTER);
		//removeComponents.add(north2);
		
		JLabel  dslabel= new JLabel(label, JLabel.LEFT);
		north2.add(dslabel);
		dslabel.setFont(font);
		
		
		//模式名下拉
		JComboBox<String> cbx2 = new JComboBox<String>(items2);
		
		JScrollPane msmscr = new JScrollPane(cbx2);
		north2.add(msmscr);
		msmscr.setBorder(MainApp.lineBorder);
		//cbx2.setEditable(true);
		//cbx2.setSelectedItem("HY_GBGL_ZZGB");
		this.selectSchema = items2[0];
		cbx2.setPreferredSize(new Dimension(200, 35));
		cbx2.setFont(font);
		
		
		
		
		//选择模式后事件  
		cbx2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				selectSchema = cbx2.getSelectedItem().toString();
				//直接显示下拉选框
				//cbx.setPopupVisible(true);
			}
		});
	}
	
	//查询模式
	public void searchOwner() {
		
		String sql = "select distinct owner schema_name from ALL_TABLES ";
		if(this.dbc.DBType.equalsIgnoreCase("mysql")) {
			sql = "SHOW DATABASES";
		}
		CommQuery cq = new CommQuery(this.dbc);
		try {
			List<HashMap<String, String>> list = cq.getListBySQL2(sql);
			items2 = new String[list.size()];
			for(int i=0;i<list.size();i++) {
				items2[i] = list.get(i).get("schema_name").toUpperCase();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 表名选择下拉框
	 */
	@Data
	@AllArgsConstructor
	public class Item {
		private String key;
		private String value;
		public String toString(){
			return key + "("+(value==null?"":value)+")";
		}
	}
	
	
	
	
}
