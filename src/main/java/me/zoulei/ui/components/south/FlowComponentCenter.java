package me.zoulei.ui.components.south;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import me.zoulei.Constants;
import me.zoulei.MainApp;
import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.exception.myException;
import me.zoulei.ui.components.Item;
import me.zoulei.ui.components.SearchComponent;

/**
 * 流程生成配置
 * @ClassName: FlowComponentCenter
 * @Desc: TODO
 * @author zoulei
 * @date 2024年8月8日 下午6:46:04
 */
public class FlowComponentCenter extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1192359776935702082L;
	//表名搜索 点击连接后的的其他组件生成
    public SearchComponent searchComponent = new SearchComponent();
    
	public FlowComponentCenter() {
		
		//加上选择流程副表配置
		
		
		String baseDir = System.getProperty("user.dir")+"/dsProp";
		
		//读取数据库配置
//		String url = baseDir + "/001.properties";
//		Properties p = new Properties();
		if(Constants.items==null) {
			try {
				//p.load(new InputStreamReader(new FileInputStream(url), "utf-8"));
				//获取所有数据库配置
				File[] listFiles = new File(baseDir).listFiles();
				Constants.items = new Item[listFiles.length];
				int i=0;
				for(File f : listFiles) {
					Properties fp = new Properties();
					fp.load(new InputStreamReader(new FileInputStream(f), "utf-8"));
					Constants.items[i++] = new Item(fp.getProperty("desc"), fp, f);
				}
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(MainApp.mainFrame, "读取1.properties失败："+e.getMessage());
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		JLabel  lclabel= new JLabel("流程模型库: ", JLabel.LEFT);
		this.add(lclabel);
		//选择数据库配置控件
		JComboBox<Item> dbSource = new JComboBox<Item>(Constants.items);
		dbSource.setSize(60, 30);
		//ui初始化
		
		JLabel  dslabel= new JLabel("数据库: ", JLabel.LEFT);
		JComboBox<String> dsText = new JComboBox<String>(Constants.dbTypes);
		//JTextField dsText = new JTextField("",5);
		
		JLabel  driverlabel= new JLabel("驱动: ", JLabel.LEFT);
		JTextField driverText = new JTextField("",14);
		
		JLabel  urllabel= new JLabel("jdbc-URL: ", JLabel.LEFT);
		JTextField urlText = new JTextField("",30);
		
		JLabel  namelabel= new JLabel("用户名: ", JLabel.RIGHT);
		JTextField userText = new JTextField("",12);
		
		JLabel  passwordLabel = new JLabel("密码: ", JLabel.CENTER);
		JTextField passwordText = new JTextField("",12);      

		JButton loginButton = new JButton("连接数据库");
		loginButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {     
	            //测试数据库连接
	        	DataSource.dsprop = new Properties();
	            DataSource.dsprop.setProperty("user", userText.getText());
	            DataSource.dsprop.setProperty("password", passwordText.getText());
	            DataSource.dsprop.setProperty("url", urlText.getText());
	            DataSource.dsprop.setProperty("forname", driverText.getText());
	            try {
	            	DataSource.DBType = dsText.getSelectedItem().toString();
					DataSource.testDMConn();
					//JOptionPane.showMessageDialog(MainApp.mainFrame, "连接成功！");    
					//表格配置组件
			        //GridComponent grid = new GridComponent();
			        //grid.setComp();
					
					
					//searchComponent.setComp();
			        loginButton.setEnabled(false);
					/*
			        String url = this.getClass().getResource("./dsProp/1.properties").getPath();
			        try {
						DataSource.dsprop.store(new FileWriter(url), "1112");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			        */
				} catch (myException e1) {
					JOptionPane.showMessageDialog(MainApp.mainFrame, "连接失败："+e1.getMessage());    
				}
	            
	         }
	      }); 
		
		JButton saveButton = new JButton("保存连接");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {    
				Item item = (Item) dbSource.getSelectedItem();
				if(item!=null) {
					String name = JOptionPane.showInputDialog("连接名称：",item);
					if(StringUtils.isEmpty(name)) {
						return;
					}
					Properties p = item.getProp();
					p.setProperty("user", userText.getText());
					p.setProperty("password", passwordText.getText());
					p.setProperty("url", urlText.getText());
					p.setProperty("forname", driverText.getText());
					p.setProperty("DBType", dsText.getSelectedItem().toString());
					p.setProperty("desc", name);
					try {
						item.getFile().delete();
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(item.getFile()), "utf-8");
						p.store(outputStreamWriter, new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
						outputStreamWriter.close();
						JOptionPane.showMessageDialog(MainApp.mainFrame, "保存成功！"); 
						//刷新下拉选
						//dbSource.removeAllItems();
						for (int i = 0; i < Constants.items.length; i++) {
							Item m = Constants.items[i];
							if(item.getKey().equals(m.getKey())) {
								item.setKey(name);
								dbSource.requestFocus();
								break;
							}
							//dbSource.addItem(m);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(MainApp.mainFrame, "保存失败："+e1.getMessage());    
					}

	        	 }
	         }
		});
		
		
		
		JButton saveAsButton = new JButton("另存为");
		saveAsButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {    
	        	 Item item = (Item) dbSource.getSelectedItem();
	        	 if(item!=null) {
	        		String name = JOptionPane.showInputDialog("请输入名称：");
					if(StringUtils.isEmpty(name)) {
						return;
					}
					Properties p = new Properties();
					p.setProperty("user", userText.getText());
		            p.setProperty("password", passwordText.getText());
		            p.setProperty("url", urlText.getText());
		            p.setProperty("forname", driverText.getText());
		            p.setProperty("DBType", dsText.getSelectedItem().toString());
		            p.setProperty("desc", name);
		            try {
		            	//原文件不删除
		            	//item.getFile().delete();
		            	int itemCount = dbSource.getItemCount()+1;
		            	
		            	File newFile = new File(baseDir + "/"+String.format("%03d", itemCount)
		            					+(UUID.randomUUID().toString().replaceAll("-", ""))+".properties");
		            	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(newFile), "utf-8");
						p.store(outputStreamWriter, new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
						outputStreamWriter.close();
						Item newitem = new Item(name, p, newFile);
						dbSource.addItem(newitem);
						dbSource.setSelectedItem(newitem);
						Constants.addItem(newitem);
						JOptionPane.showMessageDialog(MainPanel.mainFrame, "保存成功！"); 
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(MainPanel.mainFrame, "保存失败："+e1.getMessage());    
					}

	        	 }
	         }
		});
		
	    this.setLayout(new FlowLayout());//FlowLayout是默认布局，它以方向流布局组件。
	    //数据源选择控件
	    this.add(dbSource);
	    //选择数据源事件
	    dbSource.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = (Item) dbSource.getSelectedItem();
				if(item!=null) {
					Properties p = item.getProp();
					dsText.setSelectedItem(p.getProperty("DBType"));
					driverText.setText(p.getProperty("forname"));
					urlText.setText(p.getProperty("url"));
					userText.setText(p.getProperty("user"));
					passwordText.setText(p.getProperty("password"));
					//连接按钮可用
					loginButton.setEnabled(true);
					//断开数据库连接
	            	DataSource.closeCon();
	            	//先移除组件
	            	//searchComponent.removeAll();
				}
			}
		});
		
	    
	    
	    this.add(dslabel);
	    this.add(dsText);
	    this.add(driverlabel);
	    this.add(driverText);
	    
	    this.add(urllabel);
	    this.add(urlText);
	    this.add(namelabel);
	    this.add(userText);
	    this.add(passwordLabel);       
	    this.add(passwordText);
	    this.add(loginButton);
	    this.add(saveButton);
	    this.add(saveAsButton);
	    //位置及大小
	    //controlPanel.setBounds(0, 5, 1700, 35);
	    dbSource.setSelectedIndex(0);
	}
	
}
