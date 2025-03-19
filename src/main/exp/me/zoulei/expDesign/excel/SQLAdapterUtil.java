package me.zoulei.expDesign.excel;

import org.apache.commons.lang.StringUtils;

import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.dbc.ui.components.north.DataSourceDBC;

/**
 * 数据库设计文档的导出sql 2025年3月18日11:42:34
 */
public class SQLAdapterUtil {

	public SQLAdapterUtil() {
	}

	public static String getColumnSQL() {
		String SCHEMA_NAME = "SCHEMA_NAME";
		if(DataSource.DBType.equals("oracle")) {
			SCHEMA_NAME = "owner";
		}
		String columnSQL = "";
		if(DataSource.DBType.equals("mysql")) {
			columnSQL = "";
		}else if(DataSource.DBType.equals("达梦")||DataSource.DBType.equals("oracle")) {
			columnSQL = "select /*+VIEW_PULLUP_FLAG(1)*/t.TABLE_NAME,t.column_name,\r\n"
					+ "       c.comments column_comment,\r\n"
					+ "       data_type,\r\n"
					+ "       nvl2(t.DATA_PRECISION,t.DATA_PRECISION||nvl2(t.DATA_SCALE,','||t.DATA_SCALE,''),data_length||'') data_length,\r\n"
					+ "t.DATA_DEFAULT COLUMN_DEFAULT,t.NULLABLE\r\n"
					+ "from ALL_tab_cols t ,\r\n"
					+ "       ALL_col_comments c\r\n"
					+ "      \r\n"
					+ " where t.TABLE_NAME = c.table_name\r\n"
					+ "   and t.COLUMN_NAME = c.column_name\r\n"
					+ "   and t.owner = c."+SCHEMA_NAME+"\r\n"
					+ "   and t.owner = upper('%s')\r\n"
					+ "   and t.TABLE_NAME in (select TABLE_NAME from all_tables where OWNER = upper('%s')) \r\n"
					+ (StringUtils.isNotEmpty(table)?" and t.TABLE_NAME in("+table+")":"")
					+ "   ORDER BY t.TABLE_NAME,t.COLUMN_ID";
		}
		return columnSQL;
	}
	//选择的表格
	public static String table = "";

	public static String getTableSQL(String table) {
		String tableSQL = "";
		if(DataSource.DBType.equals("mysql")) {
			tableSQL = " ";
		}else if(DataSource.DBType.equals("达梦")||DataSource.DBType.equals("oracle")) {
			tableSQL = "select c.TABLE_NAME, \r\n"
					+ "         c.COMMENTS TABLE_COMMENT,\r\n"
					+ "         pk.cs,pk.constraint_name\r\n"
					+ "    from all_tab_comments c, \r\n"
					+ "         all_tables u,\r\n"
					+ "         (\r\n"
					+ "     	 select listagg(cu.COLUMN_NAME,',') within group(order by cu.position) cs,au.constraint_name,au.table_name\r\n"
					+ "          from ALL_cons_columns cu, ALL_constraints au\r\n"
					+ "         where cu.constraint_name = au.constraint_name\r\n"
					+ "           and au.constraint_type = 'P'\r\n"
					+ "           and au.table_name = cu.table_name\r\n"
					+ "           and cu.OWNER = au.OWNER\r\n"
					+ "           and cu.owner = upper('%s')\r\n"
					+ "   		 group by au.table_name,au.constraint_name            \r\n"
					+ "         ) pk\r\n"
					+ "   where c.OWNER=u.OWNER \r\n"
					+ "     and c.TABLE_NAME=u.TABLE_NAME \r\n"
					+ "     and c.OWNER = upper('%s') \r\n"
					+ "     and c.TABLE_NAME=pk.TABLE_NAME(+)\r\n"
					+ (StringUtils.isNotEmpty(table)?" and u.TABLE_NAME in("+table+")":"")
					+ " order by u.TABLE_NAME ";
		}
		return tableSQL;
	}
	
	public static String getTableSQL() {
		return getTableSQL(table);
	}
}
