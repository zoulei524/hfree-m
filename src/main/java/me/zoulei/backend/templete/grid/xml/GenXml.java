package me.zoulei.backend.templete.grid.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.Data;
import me.zoulei.backend.templete.grid.CFGParse;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;

/**
* @author zoulei 
* @date 2023年9月18日14:38:15
* @description 输出xml相关代码。
 */
@Data
public class GenXml {
	
	/**输出的java代码*/
	private String code;
	
	public GenXml(TableMetaDataConfig config) throws Exception {
		
		InputStream is = this.getClass().getResourceAsStream("xml.ftl");
		
		String tpl = IOUtils.toString(is,"utf-8");
		
		//配置项
		HashMap<String, Object> params = new HashMap<String, Object>();
		CFGParse.parse(params, config);
		String tablename = config.getTablename().toLowerCase();
		//查询sql
		String sqlFields = config.getSqlFields();
		String listDataSQL = "select " + sqlFields + " from " + tablename + " t";
		params.put("listDataSQL", listDataSQL);
		
		Template template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		StringWriter result = new StringWriter();
	    template.process(params, result);
	    is.close();
		this.code = result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenXml genCTL = new GenXml(null);
		System.out.println(genCTL.code);
	}
	
	
}
