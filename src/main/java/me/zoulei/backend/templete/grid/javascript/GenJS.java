package me.zoulei.backend.templete.grid.javascript;

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
* @date 2023年9月18日14:27:40
* @description 输出dao类相关方法。
 */
@Data
public class GenJS {
	
	/**输出的java代码*/
	private String code;
	
	public GenJS(TableMetaDataConfig config,HashMap<String, Object> params) throws Exception {
		
		InputStream is = this.getClass().getResourceAsStream("js.ftl");
		
		String tpl = IOUtils.toString(is,"utf-8");
		
		//配置项
		CFGParse.parse(params, config);
		
		Template template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		StringWriter result = new StringWriter();
	    template.process(params, result);
	    is.close();
		this.code = result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenJS genCTL = new GenJS(null,null);
		System.out.println(genCTL.code);
	}
	
}
