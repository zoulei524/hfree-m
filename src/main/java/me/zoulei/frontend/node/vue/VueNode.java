package me.zoulei.frontend.node.vue;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.frontend.node.Attr;
import me.zoulei.frontend.node.Node;

/**
 * 2023年9月6日13:09:01   zoulei
 * vue节点对象
 */
@Data
public class VueNode implements Node{
	
	/**节点属性 */
	final private List<Attr> attrs = new ArrayList<Attr>();
	
	/**子节点 */
	final private List<Node> childNodes = new ArrayList<Node>();
	
	/**父节点 */
	private Node parentNode;
	
	/**节点名称 */	
	private String name;
	
	/**节点注释 */
	private String comments="";
	
	/**节点注释开始符号 */
	final private String start_symbol = "<!--";
	
	/**节点注释结束符号 */
	final private String end_symbol = "-->";
	
	/**节点内容 */
	private String text="";
	
	/**属性是否换行 配置项*/
	private boolean isAttrNotNewLine = Constants.ATTR_NOT_NEW_LINE;
	
	/**
	 * 生成vue的标记语言
	 */
	public String toString() {
		//缩进
		String tabStr = this.getTabs();
		//输出vue标记语言
		StringBuilder vueStr = new StringBuilder();
		//注释
		if(this.getComments().length()>0) {
			vueStr.append(tabStr+this.getStart_symbol()).append(this.getComments()).append(this.getEnd_symbol()).append("\n");
		}
		//开始标签
		vueStr.append(tabStr+"<").append(this.name);
		//属性
		String attrStr = this.toAttrsString();
		if(this.attrs.size()>0) {
			if(this.attrs.size()==1||this.isAttrNotNewLine()) {
				vueStr.append(" " + attrStr);
				//开始标签结束
				if("img".equalsIgnoreCase(this.name)||"br".equalsIgnoreCase(this.name)) {
					vueStr.append(" /");
				}
				vueStr.append(">\n");
			}else {
				vueStr.append(attrStr);
				//开始标签结束
				if("img".equalsIgnoreCase(this.name)||"br".equalsIgnoreCase(this.name)) {
					vueStr.append(" /");
				}
				vueStr.append(tabStr+">\n");
			}
			
		}else {
			vueStr.append(">\n");
		}
		
		//子节点
		this.childNodes.forEach(cnode->{
			vueStr.append(cnode.toString());
		});
		
		//标签内容
		if(this.text.length()>0&&!"img".equalsIgnoreCase(this.name)&&!"br".equalsIgnoreCase(this.name)) {
			vueStr.append(tabStr+"	").append(this.text).append("\n");
		}
		
		//结束标签
		if(!"img".equalsIgnoreCase(this.name)&&!"br".equalsIgnoreCase(this.name)) {
			vueStr.append(tabStr+"</").append(this.name).append(">\n");
		}
		
		return vueStr.toString();
	}
	
	/**
	 * 获取缩进
	 * @return String
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
	 * @return String
	 */
	public String toAttrsString() {
		StringBuilder attrsStr = new StringBuilder();
		//缩进
		String tabStr = this.getTabs();
		boolean isaddBlank = this.attrs.size()>1&&!this.isAttrNotNewLine;
		this.attrs.forEach(attr->{
			if(this.isAttrNotNewLine) {
				attrsStr.append(attr.toString(tabStr,isaddBlank)+" ");
			}else {
				attrsStr.append(attr.toString(tabStr,isaddBlank)+"\n");
			}
		});
		
		if(this.attrs.size()>1&&!this.isAttrNotNewLine) {//两个以上属性头尾都要换行
			return "\n" + attrsStr.toString();
		}else if(this.attrs.size()==1) {//只有一个属性就连标签一起不换号
			return attrsStr.deleteCharAt(attrsStr.length()-1).toString();
		}else{
			return attrsStr.toString();
		}
		
	}
	
	/**
	 * 增加属性
	 * @param attr
	 * @return VueNode
	 */
	@Override
	public Node addAttr(Attr attr) {
		this.attrs.add(attr);
		return this;
	}
	
	/**
	 * 增加子节点  返回子节点
	 * @param VueNode
	 * @return VueNode
	 */
	@Override
	public Node appendChild(Node node) {
		node.setParentNode(this);
		this.childNodes.add(node);
		return node;
	}
	
	
	/**
	 * 根据下标获取子节点
	 * @param index
	 * @return VueNode
	 */
	@Override
	public Node getNode(int index) {
		return this.childNodes.get(index);
	}
	
	@Override
	public Node deleteAt(int index) {
		return this.childNodes.remove(index);
	}
	
	/**
	 * 增加子节点  返回当前节点
	 * @param attr
	 * @return 
	 */
	@Override
	public VueNode append(Node node) {
		node.setParentNode(this);
		this.childNodes.add(node);
		return this;
	}

	public VueNode(String name) {
		super();
		this.name = name;
	}
	public VueNode(String name,String comments) {
		super();
		this.name = name;
		this.comments = comments;
	}
	
	public VueNode(String name,String text,String comments) {
		super();
		this.name = name;
		this.text = text;
		this.comments = comments;
	}
	
	
	
}
