package me.zoulei.wkds.ui.components.north.createSQL;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.Data;
import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.backend.templete.grid.CFGParse;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;


@Data
public class GenCSQL {
	
	/**输出的java代码*/
	private String code;
	
	public GenCSQL(HashMap<String, Object> params) throws Exception {
		
		InputStream is = this.getClass().getResourceAsStream("csql.ftl");
		
		String tpl = IOUtils.toString(is,"utf-8");
		
		//配置项
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		CFGParse.parse(params, config);
		
		
		Template template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		StringWriter result = new StringWriter();
	    template.process(params, result);
	    is.close();
		this.code = result.toString();
	}
	
	public static void main(String[] args) throws Exception {
	}
	
}
