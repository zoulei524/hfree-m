package me.zoulei.gencode;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import me.zoulei.backend.templete.grid.CFGParse;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;

/**
 * 将前端和后台的代码生成到文件目录中
 * controller
 * --XXController.java
 * dto
 * dao
 * --entity.java
 * --XXDao.java
 * --XXDao.sql.xml
 * service
 * --XXServiceImpl.java
 * --impl
 * ----XXService.java
 * 
 * 
 * xx.vue
 * js
 * --xx.js
 * @ClassName: Gencode2Files
 * @Desc: TODO
 * @author zoulei
 * @date 2024年1月16日 下午5:43:15
 * 
 * 2024年5月
 * 加了异常枚举和常量包
 */
public class Gencode2Files {

	public Gencode2Files(Map<String, String[]> codemap, TableMetaDataConfig config, String name) {
		try {
			String expdir = config.getTablename().toLowerCase();
			//获取跟目录
			String baseDir = System.getProperty("user.dir");
			//生成目录
			String gendir = baseDir + "/" + expdir;
			
			File f_gendir = new File(gendir);
			if(f_gendir.isDirectory()) {
				
				FileUtils.deleteDirectory(f_gendir);
			}
			
			f_gendir.mkdir();
			
			//vue目录
			String vuedir = gendir + "/vue";
			new File(vuedir).mkdir();
			//java目录
			String javadir = gendir + "/java";
			new File(javadir).mkdir();
			
			/***前端代码**********************/
			File vuefile = new File(vuedir + "/" + initlow(name) + ".vue");
			vuefile.createNewFile();
			
			FileWriter vuefw = new FileWriter(vuefile);
			String vue = codemap.get("   VUE   ")[0] + "\n" 
					+ "<script src=\"./js/"+initlow(name)+".js\"></script>\n"
					+ codemap.get("   CSS   ")[0];
			vuefw.write(vue);
			vuefw.close();
			
			new File(vuedir + "/js").mkdir();
			File jsfile = new File(vuedir + "/js/" + initlow(name) + ".js");
			jsfile.createNewFile();
			FileWriter jsfw = new FileWriter(jsfile);
			String js = codemap.get("     JS     ")[0] ;
			jsfw.write(js);
			jsfw.close();
			/********前端代码结束***************************/
			
			//配置项
			HashMap<String, Object> params = new HashMap<String, Object>();
			CFGParse.parse(params, config);
			params.put("content", codemap.get(" Controller ")[0]);
			params.put("name", this.initcap(name));
			
			/*****controller********************************/
			InputStream is = this.getClass().getResourceAsStream("ftl/ctl.ftl");
			String tpl = IOUtils.toString(is,"utf-8");
			Template template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			//输出目录
			new File(javadir + "/controller").mkdir();
			File controllerfile = new File(javadir + "/controller/" + initcap(name) + "Controller.java");
			FileWriter result = new FileWriter(controllerfile);
		    template.process(params, result);
		    result.close();
		    is.close();
			/*****controller********************************/
			
		    
		    /*****service**********************************************************************/
		    is = this.getClass().getResourceAsStream("ftl/service.ftl");
			tpl = IOUtils.toString(is,"utf-8");
			template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			//输出目录
			String servicedir = javadir + "/service/";
			new File(servicedir).mkdir();
			File servicefile = new File(servicedir+ initcap(name) + "Service.java");
			result = new FileWriter(servicefile);
			params.put("content", codemap.get("  Service   ")[0]);
		    template.process(params, result);
		    result.close();
		    is.close();
		    
		    is = this.getClass().getResourceAsStream("ftl/serviceImp.ftl");
			tpl = IOUtils.toString(is,"utf-8");
			template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			new File(servicedir+ "impl/").mkdir();
			File serviceImpfile = new File(servicedir+ "impl/" + initcap(name) + "ServiceImpl.java");
			result = new FileWriter(serviceImpfile);
			params.put("content", codemap.get("  ServiceImp   ")[0]);
		    template.process(params, result);
		    result.close();
		    is.close();
		    
		    File excelfile = new File(servicedir+ "impl/" + params.get("entity") + "Excel.java");
		    excelfile.createNewFile();
		    result = new FileWriter(excelfile);
		    result.write(codemap.get("  ExcelExp   ")[0]);
		    result.close();
		    /*****service***************************************************************************/
		    
		    /*****dao**********************************************************************/
		    is = this.getClass().getResourceAsStream("ftl/dao.ftl");
			tpl = IOUtils.toString(is,"utf-8");
			template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			//输出目录
			String daodir = javadir + "/dao/";
			new File(daodir).mkdir();
			File daofile = new File(daodir+ initcap(name) + "Dao.java");
			result = new FileWriter(daofile);
			params.put("content", codemap.get("    Dao     ")[0]);
		    template.process(params, result);
		    result.close();
		    is.close();
		    
		    
		    is = this.getClass().getResourceAsStream("ftl/xml.ftl");
			tpl = IOUtils.toString(is,"gbk");
			Configuration configuration = new Configuration(new Version("2.3.30"));
			template = new Template("ctl", tpl, configuration );
		    File xmlfile = new File(daodir+ initcap(name) + "Dao.sql.xml");
		    FileWriterWithEncoding result2 = new FileWriterWithEncoding(xmlfile, "GBK");
			params.put("content", codemap.get("    Xml     ")[0]);
		    template.process(params, result2);
		    result2.close();
		    is.close();
		    /*****dao**********************************************************************/
		    
		    
		    //实体类
		    String entitydir = javadir + "/entity/";
			new File(entitydir).mkdir();
			File entityfile = new File(entitydir + params.get("entity") + ".java");
			entityfile.createNewFile();
			result = new FileWriter(entityfile);
			result.write(codemap.get("    实体类   ")[0]);
			result.close();
			
			
			/*****dto**********************************************************************/
			//输出目录
			String dtodir = javadir + "/dto/";
			new File(dtodir).mkdir();
			File dtofile = new File(dtodir + params.get("entity") + "Dto.java");
			dtofile.createNewFile();
			result = new FileWriter(dtofile);
			result.write(codemap.get("    Dto   ")[0]);
			result.close();

			/*****dto**********************************************************************/
			
			
			/*****enums********************************/
			is = this.getClass().getResourceAsStream("ftl/enum.ftl");
			tpl = IOUtils.toString(is,"utf-8");
			template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			//输出目录
			new File(javadir + "/enums").mkdir();
			File enumfile = new File(javadir + "/enums/" + initcap(name) + "Enum.java");
			result = new FileWriter(enumfile);
		    template.process(params, result);
		    result.close();
		    is.close();
		    
		    //流程2024年8月12日09:01:24
//		    if(config.isFlow()) {
//		    	
//		    }
		    is = this.getClass().getResourceAsStream("ftl/enum2.ftl");
			tpl = IOUtils.toString(is,"utf-8");
			template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			//输出目录
			new File(javadir + "/enums").mkdir();
			File enumM0213file = new File(javadir + "/enums/" + initcap(name) + "M0213Enum.java");
			result = new FileWriter(enumM0213file);
		    template.process(params, result);
		    result.close();
		    is.close();
		    
		    is = this.getClass().getResourceAsStream("ftl/enum3.ftl");
			tpl = IOUtils.toString(is,"utf-8");
			template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			//输出目录
			new File(javadir + "/enums").mkdir();
			File enumY60060file = new File(javadir + "/enums/" + initcap(name) + params.get("entity") +"60Enum.java");
			result = new FileWriter(enumY60060file);
		    template.process(params, result);
		    result.close();
		    is.close();
		    
		    
			/*****enums********************************/
		    
		    
		    
		    /*****constant********************************/
			is = this.getClass().getResourceAsStream("ftl/constants.ftl");
			tpl = IOUtils.toString(is,"utf-8");
			template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
			//输出目录
			new File(javadir + "/constants").mkdir();
			File constantfile = new File(javadir + "/constants/" + initcap(name) + "Constant.java");
			result = new FileWriter(constantfile);
		    template.process(params, result);
		    result.close();
		    is.close();
			/*****constant********************************/
			
			//打开文件夹
			Desktop desktop = Desktop.getDesktop();
			desktop.open(f_gendir);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
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
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
    private String initlow(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'A' && ch[0] <= 'Z'){  
            ch[0] = (char)(ch[0] + 32);  
        }  
        return new String(ch);  
    }  
	
}










