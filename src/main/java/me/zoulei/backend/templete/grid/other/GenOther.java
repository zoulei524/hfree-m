package me.zoulei.backend.templete.grid.other;

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
public class GenOther {
	
	/**输出的java代码*/
	private String code;
	
	public GenOther(TableMetaDataConfig config) throws Exception {
		
		InputStream is = this.getClass().getResourceAsStream("other.ftl");
		
		String tpl = IOUtils.toString(is,"utf-8");
		
		//配置项
		HashMap<String, Object> params = new HashMap<String, Object>();
		CFGParse.parse(params, config);
		
		Template template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		StringWriter result = new StringWriter();
		try {
			template.process(params, result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			is.close();
		}
	    
	    
		this.code = result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenOther genCTL = new GenOther(null);
		System.out.println(genCTL.code);
	}
	
}
