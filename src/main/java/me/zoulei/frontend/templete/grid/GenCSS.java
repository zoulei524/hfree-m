package me.zoulei.frontend.templete.grid;

import lombok.Data;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.frontend.node.vue.VueNode;

/**
 * 
* @author zoulei 
* @date 2023年9月28日 下午4:01:01 
* @description 生成表格的css
 */
@Data
public class GenCSS {
	
	/**输出的css代码*/
	private String code;
	
	public GenCSS(TableMetaDataConfig config) throws Exception {
		ElTableCssTpl csstpl = new ElTableCssTpl(config);
		
		//生成代码text
		//CSSClassNameNode csscode = csstpl.getInfo_page();
		VueNode styleTag = (VueNode) csstpl.getStyleTag();
		
		this.code = styleTag.toString();
	}
	
}
