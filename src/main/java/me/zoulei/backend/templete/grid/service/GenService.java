package me.zoulei.backend.templete.grid.service;

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
* @date 2023年9月18日14:26:12
* @description 输出service相关方法。
 */
@Data
public class GenService {
	
	/**输出的java代码*/
	private String code;
	private String codeimp;
	private String codeexcel;
	
	public GenService(TableMetaDataConfig config) throws Exception {
		
		InputStream is = this.getClass().getResourceAsStream("service.ftl");
		
		String tpl = IOUtils.toString(is,"utf-8");
		
		//配置项
		HashMap<String, Object> params = new HashMap<String, Object>();
		CFGParse.parse(params, config);
		
		Template template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		StringWriter result = new StringWriter();
	    template.process(params, result);
	    is.close();
		this.code = result.toString();
		
		
		is = this.getClass().getResourceAsStream("serviceImp.ftl");
		tpl = IOUtils.toString(is,"utf-8");
		template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		result = new StringWriter();
	    template.process(params, result);
	    is.close();
		this.codeimp = result.toString();
		
		
		is = this.getClass().getResourceAsStream("excel.ftl");
		tpl = IOUtils.toString(is,"utf-8");
		template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		result = new StringWriter();
	    template.process(params, result);
	    is.close();
		this.codeexcel = result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenService genCTL = new GenService(null);
		System.out.println(genCTL.code);
	}
	
	
}
