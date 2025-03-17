package me.zoulei.ui.components.south;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dm.jdbc.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.MainApp;
import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.backend.jdbc.utils.CommQuery;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.gencode.Gencode;
import me.zoulei.ui.components.GridComponent;
import me.zoulei.ui.frame.AutoCompletion;

/**
 * 2024年8月8日18:47:54 用于设置流程模型
 * 用于搜索数据库表的组件， 设置表后可以加载表格参数配置组件。  查询模式和表名 oracle和达梦，mysql
 * 
 */
public class FlowSearchComponentNorth  extends JPanel {

	private static final long serialVersionUID = -725870091266444579L;
	Item[] items;
	String[] items2 = new String[] {"sadsa","dsf","fsdfg","dsadsfx"};
	
	FlowGridComponent grid;
	List<Component> removeComponents = new ArrayList<Component>();
	public void removeAll() {
		//第一次进来没渲染，不需要移除。 
		if(removeComponents.size()>0) {
			removeComponents.forEach(c->{
				//MainApp.north.remove(c);
			});
			
			//MainApp.mainFrame.remove(this.grid.getEditorGrid());
			//MainApp.mainFrame.getContentPane().repaint();
		}
		
	}
	
	Font font = new Font("宋体", Font.PLAIN, 18);
	
	public FlowSearchComponentNorth() {
		//初始化代码类别项
		//this.initCodeType();
		//模式列表
		this.searchOwner();
		
		//表格组件
		this.grid = new FlowGridComponent();
		
		//模式表名选择 放到主面板南面的北边
		JPanel north2 = new JPanel();
		if(MainApp.debugbBorder!=null) {
			north2.setBorder(MainApp.debugbBorder);
		}
		this.add(north2,BorderLayout.NORTH);
		
		removeComponents.add(this);
		
		JLabel  dslabel= new JLabel("选择模式: ", JLabel.LEFT);
		north2.add(dslabel);
		dslabel.setFont(font);
		
		/*
		dslabel= new JLabel("表格的属性设置: ", JLabel.LEFT);
		dslabel.setFont(new Font("宋体", Font.PLAIN, 20));
		north2.add(dslabel);
		*/
		
		//模式名下拉
		JComboBox<String> cbx2 = new JComboBox<String>(items2);
		
		JScrollPane msmscr = new JScrollPane(cbx2);
		north2.add(msmscr);
		msmscr.setBorder(MainApp.lineBorder);
		//cbx2.setEditable(true);
		cbx2.setSelectedItem("HY_GBGL_ZZGB");
		cbx2.setPreferredSize(new Dimension(200, 35));
		cbx2.setFont(font);
		
		//表名下拉
		search((String) cbx2.getSelectedItem());
		
		
		dslabel= new JLabel("输入业务表名: ", JLabel.CENTER);
		north2.add(dslabel);
		dslabel.setFont(font);
		
		JComboBox<Item> cbx = new JComboBox<Item>(items);
		//设置下拉最多显示的选项
		cbx.setMaximumRowCount(30);
		cbx.setPreferredSize(new Dimension(1100, 35));
		cbx.setFont(font);
		
		//生成数据库配置及代码按钮
		//JButton genCodeBtn = new JButton("生成代码");
//		genCodeBtn.setFont(font);
//		genCodeBtn.setPreferredSize(new Dimension(90, 35));
		//选择模式后事件  查询表名，将选择表名的下拉框选项重新设置
		cbx2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search((String) cbx2.getSelectedItem());
				cbx.removeAllItems();
				for (Item item : items) {
					cbx.addItem(item);
	            }
				
				//直接显示下拉选框
				//cbx.setPopupVisible(true);
			}
		});
		JScrollPane controlPanel = new JScrollPane(cbx);
		north2.add(controlPanel);
		//下拉框
		controlPanel.setBorder(MainApp.lineBorder);
		//实现搜索
		AutoCompletion.enable(cbx);
		//cbx.setEnabled(false);
        //cbx.setPopupVisible(true);
		
		
		FlowSearchComponentNorth _this = this;
		
		//选择表名事件 选择后加载表格
		cbx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = (Item) cbx.getSelectedItem();
				if(item!=null) {
					grid.setComp(item.getKey(),(String) cbx2.getSelectedItem(),_this);
				}
			}
		});
		//第一次默认加载表
//		Item item = (Item) cbx.getSelectedItem();
//		if(item!=null) {
//			grid.setComp(item.getKey(),(String) cbx2.getSelectedItem(),this);
//		}
		
		
//		north2.add(genCodeBtn);
//		genCodeBtn.addActionListener(new ActionListener() {
//	         public void actionPerformed(ActionEvent e) {     
//	             //生成配置
//	        	 List<HashMap<String, String>> tmd = grid.editorGrid.genTableMetaData();
//	        	 Item item = (Item) cbx.getSelectedItem();
//	        	 try {
//	        		 //生成代码
//					new Gencode().gencode(new TableMetaDataConfig(item.getKey(),item.getValue(), tmd, grid.editorGrid));
//				} catch (Exception e1) {
//					JOptionPane.showMessageDialog(MainApp.mainFrame, e1.getMessage());    
//					e1.printStackTrace();
//				}
//	            
//	         }
//	      });
//		
//		genCodeBtn.setBorder(MainApp.lineBorder);
		//高度
		this.setPreferredSize(new Dimension(-1, 365));
	}
	//查询表
	public void search(String sch) {
		if(StringUtil.isEmpty(sch)) {
			items = new Item[] {};
			//return;
		}
		String sql = "select t.table_name,c.COMMENTS TABLE_COMMENT from All_TABLES t,SYS.ALL_TAB_COMMENTS c where t.TABLE_NAME=c.TABLE_NAME and t.owner = c.owner and t.owner = '"+sch.toUpperCase()+"' order by table_name ";
		if(DataSource.DBType.equalsIgnoreCase("mysql")) {
			sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA='"+sch.toUpperCase()+"' and TABLE_COMMENT!='VIEW'  order by table_name" ;
		}
		
		CommQuery cq = new CommQuery();
		try {
			List<HashMap<String, String>> list = cq.getListBySQL2(sql);
			items = new Item[list.size()];
			for(int i=0;i<list.size();i++) {
				items[i] = new Item(list.get(i).get("table_name"),list.get(i).get("table_comment"));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainApp.mainFrame, e.getMessage());    
			e.printStackTrace();
		}
	}
	//查询模式
	public void searchOwner() {
		
		String sql = "select distinct owner schema_name from ALL_TABLES ";
		if(DataSource.DBType.equalsIgnoreCase("mysql")) {
			sql = "SHOW DATABASES";
		}
		CommQuery cq = new CommQuery();
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
	
	
	
	//选择codetype的下拉框选项
	private void initCodeType() {
		if(Constants.codetype_items!=null) return;
		CommQuery cq = new CommQuery();
		try {
			List<HashMap<String, String>> list = cq.getListBySQL2(Constants.CODE_VALUE_SQL);
			String[] items = new String[list.size()*2+2];
			//文本、公务员常用时间控件、codetype
			int i=0;
			items[i++] = "文本";
			items[i++] = "公务员常用时间控件";
			for (int j = 0; j < list.size(); j++) {
				HashMap<String, String> m = list.get(j);
				String codetype = m.get("code_type");
				String typename = m.get("type_name");
				items[i++] = codetype+":下拉选:"+typename;
				items[i++] = codetype+":弹出框:"+typename;
			}
			Constants.codetype_items = items;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
}
