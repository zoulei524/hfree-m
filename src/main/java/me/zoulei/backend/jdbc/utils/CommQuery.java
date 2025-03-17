package me.zoulei.backend.jdbc.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.dbc.ui.components.north.DataSourceDBC;

/**
 * 2023年9月6日18:20:26   zoulei
 * 数据库查询
 * 
 */
public class CommQuery {
	/**
	 * 查询语句
	 */
	String querySQL=null;
	/**
	 * 数据库连接
	 */
	 Connection conn=null;
	 
	 {
		 
	 }
	
	PreparedStatement statement = null;
	/**
	 * @$comment 查询
	 * @param sql 拼装的sql语句
	 * @return List<HashMap>
	 * @throws Exception 
	 * @throws AppException
	 */
	public List<HashMap<String, Object>> getListBySQL(String sql) throws Exception{
		
		return getListBySQL(sql, false);		
	}
	
	public List<HashMap<String, String>> getListBySQL2(String sql) throws Exception{
		
		return getListBySQL2(sql, true);		
	}
	
	/**
	 * @$comment 查询
	 * @param sql 拼装的sql语句
	 * @return List<HashMap>
	 * @throws Exception 
	 * @throws AppException
	 */
	public List<HashMap<String, Object>> getListBySQL(String sql,boolean isOrder) throws Exception {
		this.setQuerySQL(sql);
		ArrayList<?> vector=this.query(isOrder);
		Iterator<?> iterator = vector.iterator();
		List<HashMap<String, Object>> hmLst=new java.util.ArrayList<HashMap<String, Object>>();
		while (iterator.hasNext())
		{
			HashMap<String, Object> tmp= (HashMap<String, Object>)iterator.next();		
			hmLst.add(tmp);
		}
		return hmLst;		
	}
	/**
	 * @$comment 查询
	 * @param sql 拼装的sql语句
	 * @return List<HashMap>
	 * @throws Exception 
	 * @throws AppException
	 */
	public List<HashMap<String, String>> getListBySQL2(String sql,boolean isOrder) throws Exception{
		this.setQuerySQL(sql);
		ArrayList<?> vector=this.query(isOrder);
		Iterator<?> iterator = vector.iterator();
		List<HashMap<String, String>> hmLst=new java.util.ArrayList<HashMap<String, String>>();
		while (iterator.hasNext())
		{
			HashMap<String, String> tmp= (HashMap<String, String>)iterator.next();		
			hmLst.add(tmp);
		}
		return hmLst;		
	}
	
	/**
	 * 执行查询，返回的记录集中每条记录为一个HashMap对象
	 * @param isOrder 
	 * @throws Exception 
	 */	
	@SuppressWarnings("unchecked")
	public ArrayList query(boolean isOrder) throws Exception {
		this.setConnection();
		Statement stmt=null;
		ResultSet rs=null;
		ResultSetMetaData rsmd=null;
		ArrayList rtnVector=new ArrayList();
		int cols;
		try{
			stmt=conn.createStatement();
			rs=stmt.executeQuery(querySQL);
			rsmd=rs.getMetaData();
			cols=rsmd.getColumnCount();
			String r;
			while(rs.next()){
				HashMap hm= isOrder?new LinkedHashMap():new HashMap();
				for(int j=1;j<=cols;j++){
					r = rs.getString(j);
					hm.put((rsmd.getColumnName(j)).toLowerCase(),r==null?"":r);
				}
				rtnVector.add(hm);
			}
		}catch(Exception e){
			throw new Exception("查询失败",e);
		}finally{
			try{
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			}catch(SQLException ex){
				throw new Exception(ex.toString());
			}
		}
		return rtnVector;	
	}
	
	public void setConnection() {
		if(conn==null) {
			conn = DataSource.openDMConn();
		}
		
	}
	
	public CommQuery() {
		
	}
	

	public CommQuery(DataSourceDBC dbc) {
		this.conn = dbc.con;
	}

	/**
	 * @param string
	 */
	public void setQuerySQL(String string) {
		querySQL = string;
	}
	
}
