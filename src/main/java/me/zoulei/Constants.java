package me.zoulei;

import me.zoulei.ui.components.Item;

/**
 * 
* @author zoulei 
* @date 2023年9月18日 上午11:15:51 
* @description  配置项
 */
public class Constants {
	
	public static String VERSION = "1.0.0";
	
	//2024年1月17日15:42:06 废弃
	/**接口路径 RequestMapping  */
	public static String DATA_URL = "/Test";
	/**属性是否换行 配置项*/
	public static Boolean ATTR_NOT_NEW_LINE = true;
	/**作者*/
	public static String AUTHOR = "zoulei";
	/**实体类包*/
	public static String PACKAGE_OUTPATH = "com.insigma.business.entity.yuhang";
	
	/**获取codetype的sql*/
	public static String CODE_VALUE_SQL = "select code_type,type_name from HY_GBGL_ZZGB.code_type order by code_type";
	
	/**存取codetype的项*/
	public static String[] codetype_items=null;
	
	/**导出类的包名，填写到controller前一个目录*/
	public static String OUTPUT_PACKAGE = "com.insigma.business";
	
	/**选择数据库配置 的下拉选*/
	public static Item[] items = null;
	public static void addItem(Item item) {
		if(items!=null) {
			Item[] newItem = new Item[items.length+1];
			System.arraycopy(items, 0, newItem, 0, items.length);
			newItem[newItem.length-1] = item;
			items = newItem;
		}
	}
	
	/**数据库类型 的下拉选*/
	public static String[] dbTypes = new String[] {"mysql","达梦","oracle"};
	
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
