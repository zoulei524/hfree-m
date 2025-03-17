package me.zoulei.dbc.ui.components.north;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

import me.zoulei.MainApp;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.exception.myException;

/**
 * 2023年11月15日11:33:00 zoulei
 * 数据源
 */
public class DataSourceDBC {
	
	/**
	 * 分别为oracle、达梦、和mysql， 若是其它文本会报错
	 */
	public String DBType = "";
	
	
	
	public Connection con;
	public Connection openDMConn(){
		try {
			if(con!=null&&!con.isClosed())return con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Class.forName(dsprop.getProperty("forname")).newInstance();
			con = DriverManager.getConnection(dsprop.getProperty("url"),dsprop);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainPanel.mainFrame, "连接失败："+e.getMessage());    
			return null;
		}
		return con;
	}
	
	
	public Properties dsprop ;
	static {
		//dsprop.setProperty("forname", "dm.jdbc.driver.DmDriver");
	}
	/**
	 * 测试连接
	 * @return
	 * @throws myException 
	 * @throws Exception 
	 */
	public void testDMConn() throws myException{
		
		Connection con = null;
		try {
			Class.forName(dsprop.getProperty("forname")).newInstance();
			con = DriverManager.getConnection(dsprop.getProperty("url"),dsprop);
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new myException(e.getMessage());
		}
		
	}
	public void closeCon() {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
}
