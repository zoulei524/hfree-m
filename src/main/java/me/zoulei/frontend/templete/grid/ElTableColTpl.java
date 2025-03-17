package me.zoulei.frontend.templete.grid;

import me.zoulei.frontend.node.vue.VueNode;

/**
 * 2023年9月6日18:39:35  zoulei
 * 表格中的列 el-table-column
 * <el-table-column
    prop="yb001"
    label="序号"
    width="90"
    align="center"
   >
 */
public class ElTableColTpl {
	
	public static VueNode getTPL() {
		VueNode div1 = new VueNode("el-table-column","表格列");
		return div1;
	}
	
}
