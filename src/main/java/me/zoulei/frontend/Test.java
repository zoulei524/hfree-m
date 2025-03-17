package me.zoulei.frontend;

import java.util.HashMap;
import java.util.List;

import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.backend.templete.grid.entity.GenEntity;
import me.zoulei.frontend.node.css.CSSAttr;
import me.zoulei.frontend.node.css.CSSClassNameNode;
import me.zoulei.frontend.node.vue.VueAttr;
import me.zoulei.frontend.node.vue.VueNode;
import me.zoulei.frontend.templete.grid.ElTableColTpl;
import me.zoulei.frontend.templete.grid.ElTableTpl;

public class Test {
	

	public static void main(String[] args) throws Exception {
		ElTableTpl gt = new ElTableTpl(null);
		VueNode gridTable = gt.getPage();
		VueNode eltableCONST = gt.getEl_table();
		
		TableMetaDataConfig config = new TableMetaDataConfig("y339","HY_GBGL_ZZGB","");
		
		List<HashMap<String, String>> list = config.getTableMetaData();
		list.forEach(column->{
			VueNode col = ElTableColTpl.getTPL();
			col.setComments(col.getComments()+":"+column.get("comments"));
			//prop="yb001"
		    //label="序号"
			col.addAttr(new VueAttr("prop", column.get("column_name").toLowerCase()))
			   .addAttr(new VueAttr("label", column.get("comments")))
			;
			eltableCONST.append(col);   
		});
		
		
		
		System.out.println(gridTable.toString());
		
		
		//css测试
		CSSClassNameNode cssnode = new CSSClassNameNode("page","页面");
		CSSClassNameNode cssnode2 = new CSSClassNameNode("tablebox","表格样式");
		cssnode2.addAttr(new CSSAttr("width", "100px", "测试宽"));
		cssnode2.addAttr(new CSSAttr("display", "inline-block", "不换行"));
		CSSClassNameNode cssnode3 = new CSSClassNameNode("tablediv","表格");
		cssnode3.addAttr(new CSSAttr("width", "100%", "100%宽"));
		CSSClassNameNode cssnode4 = new CSSClassNameNode("tablediv2","表格2");
		cssnode4.addAttr(new CSSAttr("width", "100%", "100%宽"));
		cssnode.append(cssnode4).appendChild(cssnode2).appendChild(cssnode3);
		System.out.println(cssnode.toString());
		
		
		GenEntity genEntity = new GenEntity(config);  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
        
        genEntity = new GenEntity(new TableMetaDataConfig("a01","select * from code_value"));  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
	}

}
