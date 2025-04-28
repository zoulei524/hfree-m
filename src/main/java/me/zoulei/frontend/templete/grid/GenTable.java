package me.zoulei.frontend.templete.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.Data;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.backend.templete.grid.entity.GenEntity;
import me.zoulei.frontend.node.Node;
import me.zoulei.frontend.node.vue.VueAttr;
import me.zoulei.frontend.node.vue.VueNode;
import me.zoulei.ui.components.ItemEnum;

/**
* @author zoulei 
* @date 2023年9月28日 下午3:03:30 
* @description 生成表格vue及css代码
 */
@Data
public class GenTable {
	
	/**输出的vue代码*/
	private String code;
	
	public GenTable(TableMetaDataConfig config) throws Exception {
		//表格模板
		ElTableTpl gt = new ElTableTpl(config);
		//表格vue
		VueNode gridPage = gt.getPage();
		VueNode el_table = gt.getEl_table();
		
		List<HashMap<String, String>> list = config.getTableMetaData();
		
		
		//序号
		/*
		 * <el-table-column type="index" label="行号" align="center" fixed width="55" >
		 * </el-table-column>
		 */
		VueNode 序号 = ElTableColTpl.getTPL();
		序号.addAttr(new VueAttr("type", "index"))
		   .addAttr(new VueAttr("label", "序号"))
		   .addAttr(new VueAttr("width", "55"))
		   .addAttr(new VueAttr("align", "center"));
		;
		
		el_table.append(序号);  
		//导出excel的配置信息  在生成vue代码的时候生成该信息
		List<Object> excelCFG = new ArrayList<Object>();
		List<Object> excelCFGPK = new ArrayList<Object>();
		list.forEach(column->{
			//表格列
			if(ItemEnum.A.toString().equals(column.get("visible"))||ItemEnum.C.toString().equals(column.get("visible"))) {
				VueNode col = ElTableColTpl.getTPL();
				col.setComments(col.getComments()+":"+(column.get("comments2")==null?column.get("comments"):column.get("comments2")));
				//prop="yb001"
			    //label="序号"
				//System.out.println(column);
				if("文本".equals(column.get("editortype"))) {
					col.addAttr(new VueAttr("prop", column.get("column_name").toLowerCase()));
				}else if("公务员常用时间控件".equals(column.get("editortype"))){//2024年1月15日19:36:04  时间控件
					col.addAttr(new VueAttr("prop", column.get("column_name").toLowerCase()+".time"));
				}else{
					col.addAttr(new VueAttr("prop", column.get("column_name").toLowerCase()+".value"));
				}
				col.addAttr(new VueAttr("label", column.get("comments")))
				   .addAttr(new VueAttr("width", column.get("width")))
				   .addAttr(new VueAttr("align", column.get("align")))
				   .addAttr(new VueAttr("show-overflow-tooltip"))
				;
				
				
				el_table.append(col);  
				
				//导出excel的参数
				//表格列参数  宽度  是否数字 水平位置  列名  列名描述 
				HashMap<String, String> m = new LinkedHashMap<String, String>();
				m.put("width", column.get("width"));
				m.put("excelwidth", Double.valueOf(column.get("width"))/10+"");
				m.put("align", column.get("align"));
				m.put("label", column.get("comments"));
				m.put("codetype", column.get("codetype"));
				m.put("editortype", column.get("editortype"));
				m.put("colname", column.get("column_name").toLowerCase());
				m.put("type", GenEntity.sqlType2JavaType(column.get("data_type")));
				excelCFG.add(m);
			}else {
				//导出excel的参数
				//表格列参数  宽度  是否数字 水平位置  列名  列名描述 
				HashMap<String, String> m = new LinkedHashMap<String, String>();
				m.put("width", column.get("width"));
				m.put("excelwidth", Double.valueOf(column.get("width"))/10+"");
				m.put("align", column.get("align"));
				m.put("label", column.get("comments"));
				m.put("codetype", column.get("codetype"));
				m.put("editortype", column.get("editortype"));
				m.put("colname", column.get("column_name").toLowerCase());
				m.put("type", GenEntity.sqlType2JavaType(column.get("data_type")));
				excelCFGPK.add(m);
			}
			 
		});
		config.setExcelCFG(JSON.toJSONString(excelCFG,SerializerFeature.UseSingleQuotes));
		//excel参数升级
		JSONObject jo = new JSONObject(true);
		jo.put("title", config.getTablecomment());
		jo.put("tablename", config.getTablename().toLowerCase());
		jo.put("pk", config.getPk());
		jo.put("templete_type", "table");//"templete_type": "form/table"//form纯表单，table表单+列表
		jo.put("rowStart", 2);
		jo.put("where", "where 1=1 limit 10");
		jo.put("row_height", Arrays.asList(40));
		List header = new ArrayList<>();
		header.add(excelCFG);
		jo.put("header", header);
		jo.put("hidden", excelCFGPK);
		config.setExcelCFG2(JSON.toJSONString(jo,true));
		
		//编辑删除按钮
		if(gt.getEditorelbutton()!=null)
			el_table.append(gt.getEditorelbutton());
		
		//title
		String tablecomment = config.getTablecomment();
		String tablename = config.getTablename();
		//第一个P标签里放标题文字
		VueNode title = (VueNode) gt.getTitle();
		((VueNode) title.getNode(0)).setText(tablecomment+"("+tablename+")");
		
		
		this.code = gridPage.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenTable genCTL = new GenTable(null);
		System.out.println(genCTL.code);
	}
	
	
}
