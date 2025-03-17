package me.zoulei.frontend.node;

/**
 * 
* @author zoulei 
* @date 2023年9月28日 下午4:52:48 
* @description 节点接口  css和vue通过节点对象生成代码
 */
public interface Node {
	//返回当前节点
	Node append(Node node);
	//返回子节点
	Node appendChild(Node node);
	Node getNode(int index);
	Node deleteAt(int index);
	Node addAttr(Attr attr);
	void setParentNode(Node node);
	Node getParentNode();
	
}
