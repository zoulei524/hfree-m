package me.zoulei.backend.templete.grid.logsql;

import java.util.UUID;

import lombok.Data;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;

/**
 * 
 * @ClassName: GenLogSQL
 * @Desc: 若是表格维护需要记录维护日志，需要往code_table_col和code_table里插入数据。该类用于生成插入sql脚本
 * @author zoulei
 * @date 2024年3月4日 上午10:30:12
 */
@Data
public class GenLogSQL {

	/**输出的sql*/
	private String code;
	
	public GenLogSQL(TableMetaDataConfig config) {
		StringBuilder sb = new StringBuilder();
		String tablecomment = config.getTablecomment();
		String tablename = config.getTablename().toUpperCase();
		sb.append("-- code_table_col日志记录脚本。\n\n -- 插入语句请核对需求按实际情况执行！\n\n");
		sb.append("insert into code_table(TABLE_CODE,TABLE_NAME,ISLOOK) VALUES ('"+tablename+"', '"+tablecomment+"', '0');\n");
		
		config.getTableMetaData().forEach(d->{
			if("是".equals(d.get("islog"))) {
				String code_type = d.get("codetype");
				sb.append("insert into code_table_col(CTCI,TABLE_CODE,COL_CODE,COL_NAME,CODE_TYPE, COL_LECTION_CODE,COL_LECTION_NAME, COL_DATA_TYPE,IS_ZBX,ZBX_TJ,ISLOOK)\n"
						+ "values ('"+UUID.randomUUID()+"','"+tablename+"','"+d.get("column_name")+"','"+code_type+"','"+d.get("comments")+"','"+tablename+"','"+tablecomment+"','varchar2','0','0','0');\n\n");
			}
		});
		
		this.code = sb.toString();
	}

}
