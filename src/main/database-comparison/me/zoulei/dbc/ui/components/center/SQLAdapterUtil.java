package me.zoulei.dbc.ui.components.center;

import org.apache.commons.lang.StringUtils;

import me.zoulei.dbc.ui.components.north.DataSourceDBC;

/**
 * 2023年11月29日09:09:59 zoulei
 * 区分mysql或者oracle的sql
 */
public class SQLAdapterUtil {

	public SQLAdapterUtil() {
	}

	public static String getColumnSQL(DataSourceDBC dbc) {
		String SCHEMA_NAME = "SCHEMA_NAME";
		if(dbc.DBType.equals("oracle")) {
			SCHEMA_NAME = "owner";
		}
		String columnSQL = "";
		if(dbc.DBType.equals("mysql")) {
			columnSQL = "SELECT b.table_name,b.column_name, b.column_comment, b.data_type,\r\n"
					+ "	CASE WHEN b.CHARACTER_MAXIMUM_LENGTH != ''  AND b.CHARACTER_MAXIMUM_LENGTH IS NOT NULL THEN b.CHARACTER_MAXIMUM_LENGTH  \r\n"
					+ "	     WHEN b.NUMERIC_PRECISION != ''  AND b.NUMERIC_PRECISION IS NOT NULL THEN concat( b.NUMERIC_PRECISION, ',', b.NUMERIC_SCALE ) ELSE \r\n"
					+ "			 b. DATETIME_PRECISION  END AS data_length,\r\n"
					+ "			CASE WHEN b.COLUMN_KEY = 'PRI' THEN 1 ELSE 0 END p   "
					+ ","
					+ "concat(\"ALTER TABLE \",\"`\",b.TABLE_NAME,\"`\",\" add \",\" \",\"`\",b.COLUMN_NAME,\"`\",\" \",b.COLUMN_TYPE , \" \",\r\n"
					+ "if(b.CHARACTER_SET_NAME is null,\" \",concat(\" character set \",b.CHARACTER_SET_NAME,\" \")),\r\n"
					+ "if(b.COLLATION_NAME is null,\" \",concat(\" COLLATE \",\"'\",b.COLLATION_NAME,\"' \")),\r\n"
					+ "if(b.IS_NULLABLE='NO',\" NOT NULL \",\" null \"), \r\n"
					+ "if(b.COLUMN_DEFAULT is null , if(b.EXTRA='auto_increment' or b.IS_NULLABLE='NO',\" \",\" DEFAULT null \") ,concat(\" DEFAULT \",if(b.DATA_TYPE='timestamp' or b.DATA_TYPE='bit' ,b.COLUMN_DEFAULT,concat(\"'\",b.COLUMN_DEFAULT,\"'\")))),\r\n"
					+ "if(b.EXTRA is null ,\" \",concat(\" \",b.EXTRA,\" \"  )), "
					+ "\" COMMENT \",\" \",\"'\",b.COLUMN_COMMENT,\"'\",\";\") add_column,b.COLUMN_DEFAULT"
					+ " "
					+ " FROM information_schema.COLUMNS b"
					+ " WHERE   b.table_schema = upper('%s') "
					+ " and b.TABLE_NAME in(select TABLE_NAME from information_schema.tables where TABLE_TYPE='BASE TABLE' and table_schema = upper('%s') )"
					+ (StringUtils.isNotEmpty(table)?" and b.TABLE_NAME in("+table+")":"")
					+ "		ORDER BY b.table_name,b.ORDINAL_POSITION";
		}else if(dbc.DBType.equals("达梦")||dbc.DBType.equals("oracle")) {
			columnSQL = "select /*+VIEW_PULLUP_FLAG(1)*/t.TABLE_NAME,t.column_name,\r\n"
					+ "       c.comments column_comment,\r\n"
					+ "       data_type,\r\n"
					+ "       nvl2(t.DATA_PRECISION,t.DATA_PRECISION||nvl2(t.DATA_SCALE,','||t.DATA_SCALE,''),data_length||'') data_length,\r\n"
//					+ "       nvl((select 1 p\r\n"
//					+ "          from ALL_cons_columns cu, ALL_constraints au\r\n"
//					+ "         where cu.constraint_name = au.constraint_name\r\n"
//					+ "           and au.constraint_type = 'P'\r\n"
//					+ "           and au.table_name = cu.table_name\r\n"
//					+ "           and cu.OWNER = au.OWNER\r\n"
//					+ "           \r\n"
//					+ "           and au.table_name = t.TABLE_NAME\r\n"
//					+ "           and cu.owner = t.owner\r\n"
//					+ "           and cu.COLUMN_NAME=t.COLUMN_NAME\r\n"
//					+ "           ),0) p,\r\n"
					+ "nvl(p.p,0) p,"
					+ ""
					+ "('-- Add/modify columns'||chr(10)||'alter table %s.'||t.TABLE_NAME||' add '||t.COLUMN_NAME||' '||\r\n"
					+ "(case when t.DATA_TYPE in('NVARCHAR2','NUMBER','CHAR','VARCHAR2','RAW')\r\n"
					+ "         then t.DATA_TYPE || '(' || nvl2(t.DATA_PRECISION,t.DATA_PRECISION||nvl2(t.DATA_SCALE,','||t.DATA_SCALE,''),t.data_length||'') || ')'\r\n"
					+ "          else t.DATA_TYPE end)\r\n"
					+ "||nvl2(t.DATA_DEFAULT,' default '||'SdefaultS','')||decode(t.NULLABLE,'N',' not null ','')  \r\n"
					+ "||';') add_column,\r\n"
					+ "('-- Add comments to the columns'||chr(10)||'comment on column %s.'||t.TABLE_NAME||'.'||t.COLUMN_NAME||' is '||''''||c.COMMENTS||''';' ) add_COMMENTS,"
					+ "t.DATA_DEFAULT COLUMN_DEFAULT"
					+ ""
					+ "  from ALL_tab_cols t"
					+ " left join\r\n"
					+ "	 (select 1 p,au.table_name,cu.COLUMN_NAME\r\n"
					+ "          from ALL_cons_columns cu, ALL_constraints au\r\n"
					+ "         where cu.constraint_name = au.constraint_name\r\n"
					+ "           and au.constraint_type = 'P'\r\n"
					+ "           and au.table_name = cu.table_name\r\n"
					+ "           and cu.OWNER = au.OWNER\r\n"
					+ "           and cu.owner = upper('%s')) p \r\n"
					+ "  on  p.table_name = t.TABLE_NAME and p.COLUMN_NAME=t.COLUMN_NAME"
					+ ""
					+ ",\r\n"
					+ "       ALL_col_comments c\r\n"
					+ "      \r\n"
					+ " where t.TABLE_NAME = c.table_name\r\n"
					+ "   and t.COLUMN_NAME = c.column_name\r\n"
					+ "   and t.owner = c."+SCHEMA_NAME+"\r\n"
					+ "   and t.owner = upper('%s')\r\n"
					+ "   and t.TABLE_NAME in (select TABLE_NAME from all_tables where OWNER = upper('%s'))"
					+ (StringUtils.isNotEmpty(table)?" and t.TABLE_NAME in("+table+")":"")
					+ " ORDER BY t.TABLE_NAME,t.COLUMN_ID";
		}
		return columnSQL;
	}
	//选择的表格
	public static String table = "";

	public static String getTableSQL(DataSourceDBC dbc,String table) {
		String tableSQL = "";
		if(dbc.DBType.equals("mysql")) {
			tableSQL = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.TABLES "
					+ " WHERE TABLE_SCHEMA=upper('%s') and TABLE_TYPE='BASE TABLE' "
					+ (StringUtils.isNotEmpty(table)?" and table_name in("+table+")":"")
					+ " order by table_name ";
		}else if(dbc.DBType.equals("达梦")||dbc.DBType.equals("oracle")) {
			tableSQL = "select c.TABLE_NAME,c.COMMENTS TABLE_COMMENT from all_tab_comments c,all_tables u  "
					+ " where c.OWNER=u.OWNER and c.TABLE_NAME=u.TABLE_NAME and c.OWNER = upper('%s')"
					+ (StringUtils.isNotEmpty(table)?" and u.TABLE_NAME in("+table+")":"")
					+ " order by u.TABLE_NAME ";
		}
		return tableSQL;
	}
	
	public static String getTableSQL(DataSourceDBC dbc) {
		return getTableSQL(dbc, table);
	}
}
