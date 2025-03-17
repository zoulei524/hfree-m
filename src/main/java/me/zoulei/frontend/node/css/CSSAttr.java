package me.zoulei.frontend.node.css;

import lombok.Data;
import me.zoulei.frontend.node.Attr;

/**
 * 2023年9月6日13:15:29  zoulei
 * 节点的属性
 */
@Data
public class CSSAttr implements Attr{
	
	public CSSAttr(String name) {
		super();
		this.name = name;
	}
	public CSSAttr(String name,String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public CSSAttr(String name,String value,String comments) {
		super();
		this.name = name;
		this.value = value;
		this.comments = comments;
	}
	/** 属性名称 */
	private String name;
	
	/** 属性值 */
	private String value="";
	
	/**属性注释 */
	private String comments="";
	
	/**属性注释开始符号 */
	final private String start_symbol = "/*";
	
	/**属性注释结束符号 */
	final private String end_symbol = "*/";
	
	
	/**
	 * 生成属性
	 * @return
	 */
	@Override
	public String toString(String tabStr, boolean isaddBlank) {
		tabStr = tabStr+"	";
		StringBuilder attrsStr = new StringBuilder();
		
		
		
		
		//注释
		if(this.getComments().length()>0) {
			attrsStr.append(tabStr);
			attrsStr.append(this.getStart_symbol()).append(this.getComments()).append(this.getEnd_symbol()).append("\n");
		}
		
		attrsStr.append(tabStr);
		attrsStr.append(this.name+": "+this.value+";");
		
		return attrsStr.toString();
	}
	
	
}
