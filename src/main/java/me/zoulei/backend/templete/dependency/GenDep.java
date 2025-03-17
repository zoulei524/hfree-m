package me.zoulei.backend.templete.dependency;

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
* @date 2023年9月18日 下午12:03:27 
* @description 输出控制类相关方法。
 */
@Data
public class GenDep {
	
	/**输出的java代码*/
	private String code;
	
	public GenDep(TableMetaDataConfig config) throws Exception {
		
		InputStream is = this.getClass().getResourceAsStream("dep.ftl");
		String tpl = IOUtils.toString(is,"utf-8");
		//配置项
		HashMap<String, Object> params = new HashMap<String, Object>();
		//CFGParse.parse(params, config);
		
		Template template = new Template("dep", tpl, new Configuration(new Version("2.3.30")) );
		StringWriter result = new StringWriter();
	    template.process(params, result);
		this.code = result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenDep genCTL = new GenDep(null);
		System.out.println(genCTL.code);
	}
	
	
}
