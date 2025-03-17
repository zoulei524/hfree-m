package me.zoulei.frontend.node.css;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.zoulei.frontend.node.Attr;
import me.zoulei.frontend.node.Node;
/**
 * 2023年9月7日10:14:06 zoulei
 * css对象
 */
@Data
public class CSSClassNameNode implements Node{

	public CSSClassNameNode(String name, String comments) {
		this.name = name;
		this.comments = comments;
	}

	public CSSClassNameNode(String name) {
		this.name = name;
	}
	
	/**节点名称 */
	private String name;
	
	/**节点注释 */
	private String comments="";
	
	/**节点注释开始符号 */
	final private String start_symbol = "/*";
	
	/**节点注释结束符号 */
	final private String end_symbol = "*/";
	
	/**节点属性 */
	final private List<Attr> attrs = new ArrayList<Attr>();
	
	/**子节点 */
	final private List<Node> childNodes = new ArrayList<Node>();
	
	/**父节点 */
	private Node parentNode;
	
	/**前缀 */
	private String prefix = ".";
	
	/**
	 * 获取缩进
	 * @return
	 */
	public String getTabs() {
		StringBuilder tabStr = new StringBuilder();
		//缩进
		Node n = this;
		while((n = n.getParentNode())!=null) {
			tabStr.append("	");
		}
		return tabStr.toString();
	}
	
	
	/**
	 * 生成属性
	 * @return
	 */
	public String toAttrsString() {
		StringBuilder attrsStr = new StringBuilder();
		//缩进
		String tabStr = this.getTabs();
		this.attrs.forEach(attr->{
			attrsStr.append(attr.toString(tabStr,false)+"\n");
		});
		if(this.attrs.size()==0) {
			return "\n";
		}
		return attrsStr.toString();
		
	}
	
	
	/**
	 * 生成vue的css脚本
	 */
	public String toString() {
		//缩进
		String tabStr = this.getTabs();
		//输出vue标记语言
		StringBuilder cssStr = new StringBuilder();
		//注释
		if(this.getComments().length()>0) {
			cssStr.append(tabStr+this.getStart_symbol()).append(this.getComments()).append(this.getEnd_symbol()).append("\n");
		}
		//开始样式
		cssStr.append(tabStr).append(this.prefix+this.name+"{").append("\n");
		//属性
		String attrStr = this.toAttrsString();
		cssStr.append(attrStr);
		
		//子节点
		this.childNodes.forEach(cnode->{
			cssStr.append(cnode.toString());
		});
		//结束
		cssStr.append(tabStr+"}\n");
		
		
		return cssStr.toString();
	}
	
	/**
	 * 增加属性
	 * @param attr
	 * @return 
	 */
	public Node addAttr(Attr attr) {
		this.attrs.add(attr);
		return this;
	}
	
	/**
	 * 增加子节点  返回子节点
	 * @param attr
	 * @return 
	 */
	@Override
	public Node appendChild(Node node) {
		node.setParentNode(this);
		this.childNodes.add(node);
		return node;
	}
	
	/**
	 * 增加子节点  返回当前节点
	 * @param attr
	 * @return 
	 */
	@Override
	public Node append(Node node) {
		node.setParentNode(this);
		this.childNodes.add(node);
		return this;
	}



	

	@Override
	public void setParentNode(Node node) {
		this.parentNode = node;
		
	}

	@Override
	public Node getNode(int index) {
		return this.childNodes.get(index);
	}

	@Override
	public Node deleteAt(int index) {
		return this.childNodes.remove(index);
	}

}
