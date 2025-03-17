package me.zoulei.backend.templete.grid.entity;  
  
  
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dm.jdbc.util.StringUtil;
import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.ui.components.ItemEnum;  

/**
 * 2023年9月8日15:18:59 zoulei
 * 生成实体类
 */
@Data
public class GenEntity {  
    
	public GenEntity(TableMetaDataConfig config){
		this.config = config;
		this.genE();
	}
	
	TableMetaDataConfig config;
	
    private String packageOutPath = Constants.OUTPUT_PACKAGE + ".entity";//指定实体生成所在包的路径  
    private String authorName = Constants.AUTHOR;//作者名字  
    private boolean f_util = false; // 是否需要导入包java.util.*  
    private boolean f_sql = false; // 是否需要导入包java.sql.*  
    private boolean f_math = false; // 是否需要导入包java.math.BigDecimal  
    private String entity_content;
    private String dto_content;
    private String json_content;
      
    //数据库连接  
	/* 
	private static final String URL ="jdbc:oracle:thin:@127.0.0.1:1521:ORCL";   
    private static final String NAME = "scrot";  
    private static final String PASS = "tiger";  
    private static final String DRIVER ="oracle.jdbc.driver.OracleDriver";  
	 */
    
    /* 
     * 构造函数 
     */  
    private void genE(){  
    	List<HashMap<String, String>> tableMetaData = this.config.getTableMetaData();
        
            
        int size = tableMetaData.size();   //统计列  
        //column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
        String colType;
        HashMap<String, String> fconf;
        for (int i = 0; i < size; i++) {  
        	fconf = tableMetaData.get(i);
            colType = fconf.get("data_type");
              
            if(colType.equalsIgnoreCase("date") || colType.equalsIgnoreCase("timestamp")|| colType.equalsIgnoreCase("datetime")){  
                f_util = true;  
            }  
            if(colType.equalsIgnoreCase("blob") || colType.equalsIgnoreCase("char")){  
                f_sql = true;  
            }  
            
            if(DataSource.DBType.equals("oracle")||DataSource.DBType.equals("达梦")){  
            	if(colType.equalsIgnoreCase("number")){  
            		f_math = true;  
                } 
            }
        }  
          
       parse("enety");  
       parse("dto");  
			/*   
            try {  
                File directory = new File("");  
                //System.out.println("绝对路径："+directory.getAbsolutePath());  
                //System.out.println("相对路径："+directory.getCanonicalPath());  
                String path=this.getClass().getResource("").getPath();  
                  
                System.out.println(path);  
                System.out.println("src/?/"+path.substring(path.lastIndexOf("/com/", path.length())) );  
//              String outputPath = directory.getAbsolutePath()+ "/src/"+path.substring(path.lastIndexOf("/com/", path.length()), path.length()) + initcap(tablename) + ".java";  
                String outputPath = directory.getAbsolutePath()+ "/src/"+this.packageOutPath.replace(".", "/")+"/"+initcap(tablename) + ".java";  
                FileWriter fw = new FileWriter(outputPath);  
                PrintWriter pw = new PrintWriter(fw);  
                pw.println(content);  
                pw.flush();  
                pw.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
              */ 
       
    }  
  
    /** 
     * 功能：生成实体类主体代码 
     * @param type 
     * @param colnames 
     * @param colTypes 
     * @param colSizes 
     */  
    private void parse(String type) {  
        StringBuilder sb = new StringBuilder();  
        
        if("enety".equals(type)) {
        	sb.append("package " + this.packageOutPath + ";\n");  
        }else {
        	sb.append("package " + Constants.OUTPUT_PACKAGE + ".dto" + ";\n");  
        }
        sb.append("\n");  
        if(f_math){  
        	sb.append("import java.math.BigDecimal;\n\n"); 
        }
        
        
        sb.append("import lombok.Data;\n\n"); 
        
        if("enety".equals(type)) {
        	sb.append("import javax.persistence.Id;\n"); 
            sb.append("import javax.persistence.Table;\n");
            sb.append("import javax.persistence.Entity;\n"); 
            
            sb.append("import org.hibernate.annotations.Parameter;\n"); 
            sb.append("import org.hibernate.annotations.Type;\n"); 
            sb.append("import com.insigma.business.components.hyfield.HYField;\n"); 
            sb.append("import com.insigma.business.components.hyfield.HYfieldType;\n"); 
        }else {
        	sb.append("import org.hibernate.annotations.Parameter;\n"); 
            sb.append("import org.hibernate.annotations.Type;\n"); 
            sb.append("import com.insigma.business.components.hyfield.HYField;\n"); 
            sb.append("import com.insigma.business.components.hyfield.HYfieldType;\n"); 
        }
        
        sb.append("import java.io.Serializable;\n\n"); 
        
      //判断是否导入工具包  
        if(f_util){  
            sb.append("import java.util.Date;\n");  
        }  
        if(f_sql){  
            sb.append("import java.sql.*;\n");  
        }  
        
        
        String userName = System.getProperty("user.name");
        String hostName = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostName = inetAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //注释部分  
        sb.append("/**\n");  
    	sb.append(" * @author generated by hfree @"+hostName+"@"+userName+"\n");  
        if("enety".equals(type)) {
        	sb.append(" * "+this.config.getTablename()+" 实体类 "+ this.config.getTablecomment() +"\n");  
        	
        }else {
        	sb.append(" * "+this.config.getTablename()+" Dto "+ this.config.getTablecomment() +"\n");  
        	
        }
        sb.append(" * "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())  +" \n");  
        sb.append(" */ \n");  
        //实体部分  
        
        
        
        sb.append("@Data\n");  
        if("enety".equals(type)) {
        	sb.append("@Entity \n");  
            sb.append("@Table(name = \""+(this.config.getTablename().toUpperCase())+"\") \n");  
        }
        sb.append("public class " + initcap(this.config.getTablename().toLowerCase()) +("enety".equals(type)?"":"Dto")+ " implements Serializable {\n");  
        sb.append("\n\tprivate static final long serialVersionUID = "+new Date().getTime()+"L;\n\n");  
        
        //json对象
        StringBuilder jsonSB = new StringBuilder();  
        jsonSB.append("\t\t\t"+this.config.getTablename().toLowerCase()+"EntityData : {\n");
        processAllAttrs(sb,jsonSB,type);//属性  
        //processAllMethod(sb);//get set方法  
        jsonSB.append("\t\t\t}");  
        sb.append("}\n");  
          
        //System.out.println(sb.toString());  
        
        if("enety".equals(type)) {
        	this.entity_content = sb.toString();
        	this.json_content = jsonSB.toString();
        }else {
        	this.dto_content = sb.toString();
        }
        
    }  
      
    /** 
     * 功能：生成所有属性 
     * @param sb 
     * @param jsonSB 
     * @param type 
     */  
    private void processAllAttrs(StringBuilder sb, StringBuilder jsonSB, String type) {  
    	List<HashMap<String, String>> tableMetaData = this.config.getTableMetaData();
    	HashMap<String, String> fconf;
        for (int i = 0; i < tableMetaData.size(); i++) {  
        	fconf = tableMetaData.get(i);
        	//column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
        	if(StringUtil.isNotEmpty(fconf.get("comments"))) {
        		sb.append("\t/** "+fconf.get("comments")+" 长度"+fconf.get("data_length")+" */\n");
        	}
        	
        	if("1".equals(fconf.get("p"))) {
        		if("enety".equals(type)) {
        			sb.append("\t@Id\n");
        		}
        		
        	}
        	//System.out.println(fconf);
        	//2024年1月15日19:10:43 下拉、时间、弹出框都需要特殊字段
        	if("文本".equals(fconf.get("editortype"))) {//非代码字段
        		sb.append("\tprivate " + sqlType2JavaType(fconf.get("data_type")) + " " + (fconf.get("column_name").toLowerCase()) + ";\n\n"); 
                jsonSB.append("\t\t\t\t\"" + (fconf.get("column_name").toLowerCase()) + "\": " +"\"\","  );
                if("Date".equals(sqlType2JavaType(fconf.get("data_type")))) {
                	if("dto".equals(type)) {
                		sb.append("\tprivate String " + (fconf.get("column_name").toLowerCase()) + "_str;\n\n"); 
                	}
                    jsonSB.append("\n\t\t\t\t\"" + (fconf.get("column_name").toLowerCase()) + "_str\": " +"\"\","  );
                }
        	}else {
        		//if("enety".equals(type)) {
        			//2024年1月15日19:10:50  R—必填，E—可编辑，H—隐藏，D—不可编辑, E,R—编辑可变为必填
        			String pvalue = "E";
        			if("必填".equals(fconf.get("validate"))) {
        				pvalue = "R";
        			}
        			
        			String controltype = "";
        			String jstime = "";
        			if("公务员常用时间控件".equals(fconf.get("editortype"))) {
        				controltype = "HYfieldType.DATE";
        				jstime = ", time: \"\"";
        			}else if("下拉选".equals(fconf.get("editortype"))) {
        				controltype = "HYfieldType.SELECT";
        			}else if("弹出框".equals(fconf.get("editortype"))) {
        				controltype = "HYfieldType.POPWIN";
        			}
        			String timetype = "@Parameter(name = \"type\", value = "+controltype+"),";
        			
        			sb.append("\t@Type(type = \"com.insigma.business.components.hyfield.HYfieldType\", parameters = { "+timetype+" @Parameter(name = \"codetype\", value = \""+fconf.get("codetype")+"\"), @Parameter(name = \"p\", value = \""+pvalue+"\")})\n");
        			sb.append("\tprivate HYField " + (fconf.get("column_name").toLowerCase()) + ";\n\n"); 
        			//sb.append("\tprivate " + sqlType2JavaType(fconf.get("data_type")) + " " + (fconf.get("column_name").toLowerCase()) + ";\n\n"); 
//        		}else {
//            		sb.append("\tprivate HYField " + (fconf.get("column_name").toLowerCase()) + " = new HYField(\""+fconf.get("codetype")+"\");\n\n"); 
//        		}
                jsonSB.append("\t\t\t\t\"" + (fconf.get("column_name").toLowerCase()) + "\": " +"{key: \"\", value: \"\", p: \"E\", codetype: \""+fconf.get("codetype")+"\""+jstime+"},"  );
        	}
            
            if(StringUtil.isNotEmpty(fconf.get("comments"))) {
            	jsonSB.append(" /** "+fconf.get("comments")+" */\n");
            }else {
            	jsonSB.append("\n");
            }
            
        }  
          
    }  
  
    /** 
     * 功能：生成所有方法 
     * @param sb 
     */ 
    /*
    private void processAllMethod(StringBuffer sb) {  
          
        for (int i = 0; i < colnames.length; i++) {  
            sb.append("\tpublic void set" + initcap(colnames[i]) + "(" + sqlType2JavaType(colTypes[i]) + " " +   
                    colnames[i] + "){\n");  
            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\n");  
            sb.append("\t}\n");  
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(colnames[i]) + "(){\n");  
            sb.append("\t\treturn " + colnames[i] + ";\n");  
            sb.append("\t}\n");  
        }  
          
    }  
    */
      
    /** 
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
    private String initcap(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'a' && ch[0] <= 'z'){  
            ch[0] = (char)(ch[0] - 32);  
        }  
        return new String(ch);  
    }  
  
    /** 
     * 功能：获得列的数据类型 
     * @param sqlType 
     * @return 
     */  
    public static String sqlType2JavaType(String sqlType) {  
    	
          
        if(sqlType.equalsIgnoreCase("binary_double")||sqlType.equalsIgnoreCase("decimal")||sqlType.equalsIgnoreCase("numeric")){  
            return "Double";  
        }else if(sqlType.equalsIgnoreCase("binary_float")){  
            return "Float";  
        }else if(sqlType.equalsIgnoreCase("blob")){  
            return "Byte[]";  
        }else if(sqlType.equalsIgnoreCase("clob")){  
            return "String";  
        }else if(sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar2")   
                || sqlType.equalsIgnoreCase("varchar2")|| sqlType.equalsIgnoreCase("varchar")
                || sqlType.equalsIgnoreCase("longtext")){  
            return "String";  
        }else if(sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("timestamp")  
                 || sqlType.equalsIgnoreCase("timestamp with local time zone")   
                 || sqlType.equalsIgnoreCase("datetime")   
                 || sqlType.equalsIgnoreCase("timestamp with time zone")){  
            return "Date";  
        }else if(DataSource.DBType.equals("oracle")||DataSource.DBType.equals("达梦")){  
        	if(sqlType.equalsIgnoreCase("number")){
                return "BigDecimal";  
            }else if(sqlType.equalsIgnoreCase("INTEGER")) {
            	return "Long"; 
            }
        }else if(sqlType.equalsIgnoreCase("number")||sqlType.equalsIgnoreCase("int")){  
            return "Long"; 
        }  
          
        return "String";  
    }  
      
    /** 
     * 出口 
     * TODO 
     * @param args 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {
          
//    	GenEntity genEntity = new GenEntity(new TableMetaDataConfig("y339","HY_GBGL_ZZGB",""));  
//        System.out.println(genEntity.getEntity_content());
//        System.out.println(genEntity.getJson_content());
    	System.out.println(ItemEnum.A.toString());
    }  
  
}