package me.zoulei.backend.templete.grid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;

import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.backend.jdbc.utils.CommQuery;
import me.zoulei.ui.components.EditorGrid;

/**
 * 2023年9月8日15:19:22 zoulei
 * List<HashMap<String, String>> tableMetaData
 * 获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
 */
@Data
public class TableMetaDataConfig {
	/**字段信息集合*/
	List<HashMap<String, String>> tableMetaData;
	/**表名*/
	private String tablename;
	/**表名中文描述*/
	private String tablecomment="";
	/**主键*/
	private String pk="";
	/**在表格保存时非空校验项  js 代码，在生成form对象的时候生成。所以确保先生成vue代码再生成js */
	private String rules;
	/**导出excel的配置信息  在生成表格vue代码的时候生成该信息*/
	private String excelCFG;
	
	/**首字母大写的主键*/
	private String pkU="";
	/**字段拼接 表名别名为t*/
	private String sqlFields="";
	/**是否有增删改查功能*/
	private boolean iscrud = true;
	/**是否有分页功能*/
	private boolean pagination = false;
	/**是否有导出excel功能*/
	private boolean exportExcel = false;
	/**是否维护日志*/
	private boolean addLog = false;
	/**所有的code_type*/
	private String codetype_json = "";
	/**接口路径*/
	private String dataUrl = Constants.DATA_URL;
	/**类包名路径*/
	private String outputpackage = Constants.OUTPUT_PACKAGE;
	/**
	 * 目前就2中情况，一种传tablename，生成实体类，一种传tablename和查询的sql，生成dto
	 */
	private String type;
	private String sql = "select t.column_name,nvl(c.comments,t.column_name) comments,data_type,to_char(data_length) data_length ,nvl(pk.p,0) p  "
			+ " from ALL_tab_cols t,ALL_col_comments c, "
				+ " (select 1 p,cu.column_name from ALL_cons_columns cu, ALL_constraints au "
				+ " where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' "
				+ " and au.table_name=cu.table_name and au.table_name = upper('%s') and cu.OWNER=au.OWNER and cu.owner = upper('%s')) pk"
			+ " where t.TABLE_NAME=c.table_name and t.COLUMN_NAME=c.column_name and t.owner = c.owner"
			+ " and pk.column_name(+) =c.column_name  and t.owner=upper('%s')"
			+ " and t.TABLE_NAME=upper('%s')   ORDER BY t.COLUMN_ID";//and t.data_type in('VARCHAR2')
	
	private String mysql_sql = "select "
			+ " b.column_name,"
			+ " case when b.column_comment='' or b.column_comment=null then b.column_name else b.column_comment end as comments,b.data_type,"
			+ " CASE when b.CHARACTER_MAXIMUM_LENGTH!='' and b.CHARACTER_MAXIMUM_LENGTH is not null then b.CHARACTER_MAXIMUM_LENGTH "
			+ "		when b.NUMERIC_PRECISION!='' and b.NUMERIC_PRECISION is not null then concat(b.NUMERIC_PRECISION,',',b.NUMERIC_SCALE) "
			+ "		else b.DATETIME_PRECISION "
			+ "	  end as data_length,"
			+ " case when b.COLUMN_KEY='PRI' then 1 else 0 end p "
			+ " from information_schema.COLUMNS b "
			+ " where b.table_schema = '%s' and b.table_name = '%s' order by b.ORDINAL_POSITION";
	
	
	
	/**
	 * 2023年9月8日18:04:28 zoulei
	 * List<HashMap<String, String>> tableMetaData
	 * 传入表名tablename 和 sql
	 * 获取数据库表的各种信息 字段名column_name 字段备注comments(无) 字段类型data_type 字段长度data_length 主键p（无）
	 */
	public TableMetaDataConfig(String tablename, String sql) throws Exception{
		this.tablename = tablename;
		this.sql = "select * from("+sql+") a where 1=2";
		this.type = "sql";
		this.initMetaData();
	}
	
	/**
	 * 2023年9月8日16:37:39 zoulei
	 * List<HashMap<String, String>> tableMetaData
	 * 传入表名tablename  获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
	 */
	public TableMetaDataConfig(String tablename,String owner,String e) throws Exception{
		this.tablename = tablename;
		
		if(DataSource.DBType.equalsIgnoreCase("mysql")) {
			this.sql = String.format(this.mysql_sql, owner,tablename);
		}else {
			this.sql = String.format(this.sql, tablename,owner,owner,tablename);
		}
		this.type = "table";
		this.initMetaData();
	}
	
	

	//从ui界面上点击【生成代码】按钮 传入界面上调整后的的表格参数。获取表格配置信息 
	public TableMetaDataConfig(String tablename,String tablecomment, List<HashMap<String, String>> tableMetaData, EditorGrid editorGrid) throws Exception{
		this.tablename = tablename;
		Pattern filePattern = Pattern.compile("[\\\\/:*?\"<>|]");
		tablecomment = filePattern.matcher(tablecomment==null?"":tablecomment).replaceAll("");
		this.tablecomment = tablecomment;
		this.type = "table";
		this.tableMetaData = tableMetaData;
		StringBuilder sb = new StringBuilder();
		tableMetaData.forEach(m->{
			//生成xml的sql的字段
			sb.append("t."+m.get("column_name").toLowerCase()+",");
			String sqlType = m.get("data_type");
			if(sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("timestamp")  
	                 || sqlType.equalsIgnoreCase("timestamp with local time zone")   
	                 || sqlType.equalsIgnoreCase("timestamp with time zone")){  
	            //时间to_char(created_time,'yyyy-mm-dd hh:mm:ss') created_time_str
				sb.append("to_char(t."+m.get("column_name").toLowerCase()+",'yyyy-mm-dd hh:mm:ss') "+m.get("column_name").toLowerCase()+"_str"+",");
	        }
			//主键
			if("1".equals(m.get("p"))) {
				this.pk+=m.get("column_name").toLowerCase()+",";
				this.pkU+= Constants.initcap(m.get("column_name").toLowerCase()) +",";
			}
		});
		if(sb.length()>0) {
			sb.deleteCharAt(sb.length()-1);
		}
		if(this.pk.length()>0) {
			this.pk = this.pk.substring(0, this.pk.length()-1);
			this.pkU = this.pkU.substring(0, this.pkU.length()-1);
		}
		this.sqlFields = sb.toString();
		//是否有增删改查功能
		this.iscrud = editorGrid.crudCheckBox.isSelected();
		//是否有分页功能
		this.pagination = editorGrid.paginationCheckBox.isSelected();
		//是否导出excel
		this.exportExcel = editorGrid.excelCheckBox.isSelected(); 
		//是否维护日志
		this.addLog = editorGrid.logCheckBox.isSelected();
		//类包名路径
		this.outputpackage = editorGrid.packageInput.getText();
		
		//将所有的codetype转成json
		this.codetype_json = editorGrid.codetypes.toString();
	}
	
	//从数据库获取表结构配置
	public void initMetaData() throws Exception {
		CommQuery cq = new CommQuery();
		List<HashMap<String, String>> list = cq.getListBySQL2(this.sql);
		if("sql".equals(this.type)) {
			List<HashMap<String, String>> sqllist = new ArrayList<HashMap<String,String>>();
			//创建连接  
	        Connection con = null;  
	        PreparedStatement pStemt = null;  
	        try {  
	            con = DataSource.openDMConn();
	            pStemt = con.prepareStatement(this.sql);  
	            ResultSetMetaData rsmd = pStemt.getMetaData();  
	            int size = rsmd.getColumnCount();   //统计列  
	            HashMap<String, String> c;
	            for (int i = 0; i < size; i++) {  
	            	c = new HashMap<String, String>();
	                c.put("column_name", rsmd.getColumnName(i + 1));
	                c.put("data_type", rsmd.getColumnTypeName(i + 1));
	                c.put("data_length", rsmd.getColumnDisplaySize(i + 1)+"");
	                sqllist.add(c);  
	            }  
	            this.tableMetaData = sqllist;
	            pStemt.close();
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        } 
		}else {
			this.tableMetaData = list;
		}
		
		
	}
	
}
