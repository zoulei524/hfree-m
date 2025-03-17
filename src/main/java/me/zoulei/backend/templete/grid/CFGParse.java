package me.zoulei.backend.templete.grid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import me.zoulei.Constants;

/**
 * 2023年10月31日16:09:00 zoulei
 * 设置模板的基本参数
 */
public class CFGParse {
	/**
	 * @param params tpl模板的参数
	 * @param config  表格的参数
	 */
	public static void parse(HashMap<String, Object> params, TableMetaDataConfig config) {
		
		String tablename = config.getTablename().toLowerCase();
		String author = Constants.AUTHOR;
		String entity = initcap(tablename);
		String tablecomment = config.getTablecomment();
		String otherParams = "";
		params.put("tablename", tablename);
		params.put("author", author);
		//首字母大写的表名
		params.put("entity", entity);
		params.put("tablecomment", tablecomment);
		params.put("otherParams", otherParams);
		//时间
		params.put("time", new SimpleDateFormat("yyyy年M月d日 HH:mm:ss").format(new Date()));
		params.put("config", config);
	}
	
	
	/** 
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
    public static String initcap(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'a' && ch[0] <= 'z'){  
            ch[0] = (char)(ch[0] - 32);  
        }  
        return new String(ch);  
    }  
}
