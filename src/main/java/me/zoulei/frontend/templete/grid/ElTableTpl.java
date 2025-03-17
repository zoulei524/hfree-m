package me.zoulei.frontend.templete.grid;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.frontend.node.Node;
import me.zoulei.frontend.node.vue.VueAttr;
import me.zoulei.frontend.node.vue.VueNode;
import me.zoulei.ui.components.ItemEnum;

/**
 * 2023年9月6日18:40:33 zoulei
 * 表格模板 el-table
 * 
<template>
  <div class="info-page">
    <div class="titles">
      <p>参会领导信息库</p>
    </div>
    <div class="info-box">
      <template>
            <el-table
              @row-click="rowDblClick"
              :data="tableData"
              header-cell-class-name="headerCell"
              border
              height="450"
              stripe
            >
 */
@Data
public class ElTableTpl {
	/**页面对象template，用于生产页面*/
	VueNode page;
	/**表格对象*/
	VueNode el_table;
	/**页面元素对象*/
	Node info_page;
	/**页面元素对象:里面放表格*/
	Node info_box;
	/**标题内容对象 标题 可往里加按钮*/
	Node title;
	
	//增删改查功能的对象，配置判断是否加入
	private Node title2;
	private Node elform;
	private Node editorelbutton;
	private Node eplwindow;//提示框
	
	//分页对象，配置判断是否加入
	private Node pagination;
	//表头放按钮的div
	private Node divbtn;
	
	public ElTableTpl(TableMetaDataConfig config){
		this.createNode(config);
		
		/*
		 * 2023年10月10日17:47:34
		 * 增删改查的内容，根据配置是否加到page中
		 */
		if(config.isIscrud()) {
			this.createCrudNodes(config);
		}
		/*
		 * 2023年10月18日13:58:40
		 *  分页内容，根据配置是否加到infobox中
		 */
		if(config.isPagination()) {
			this.createPaginationNodes(config);
		}
		
		/*
		 * 2023年10月19日17:34:53
		 *  导出按钮，根据配置判断，按钮是否加到表头
		 */
		if(config.isExportExcel()) {
			this.createExportExcelNodes(config);
		}
	}
	
	

	public void createNode(TableMetaDataConfig config) {
		String tablenameL = config.getTablename().toLowerCase();
		String tablenameCap = Constants.initcap(tablenameL);
		VueNode page = new VueNode("template");
		Node info_page = new VueNode("div").addAttr(new VueAttr("class","info-page"));
		
		Node titles = new VueNode("div").addAttr(new VueAttr("class","titles"));
		VueNode titlesP = new VueNode("p","标题名称","");
		//表头放按钮的div
		Node divbtn = new VueNode("div", "这里放操作按钮");
		this.divbtn = divbtn;
		
		page.appendChild(info_page)
			.appendChild(titles)
			.append(titlesP)
			.append(divbtn);
		
		Node info_box = new VueNode("div").addAttr(new VueAttr("class","info-box"));
		this.info_box = info_box;
		VueNode el_table = new VueNode("el-table");
		el_table.addAttr(new VueAttr("@row-click","row"+tablenameCap+"Click"))
		.addAttr(new VueAttr("@row-dblclick","row"+tablenameCap+"DblClick"))
		.addAttr(new VueAttr(":data",tablenameL + "TableData"))
		.addAttr(new VueAttr("height","450"))
		.addAttr(new VueAttr("header-cell-class-name","headerCell"))
		.addAttr(new VueAttr("border"))
		.addAttr(new VueAttr("stripe"));
		info_box.append(el_table);
		
		info_page.append(info_box);
		
		
		this.page = page;
		this.el_table = el_table;
		this.info_page = info_page;
		this.title = titles;
		
	}

	/**
	 * 2023年10月10日17:47:34
	 * 增删改查的内容，加到page中
	 */
	private void createCrudNodes(TableMetaDataConfig config) {
		String tablename = config.getTablename();
		String tablenameL = config.getTablename().toLowerCase();
		String tablenameCap = Constants.initcap(tablenameL);
		
		
		/*
		 <el-button size="mini" @click="addInfo" ref="add"  style="float:right;">
	        <svg class="icon">
	          <use xlink:href="#el-icon-gwy-add" /></svg>
	          &nbsp;新增
	          
	          <img src="@/assets/imgs/zzgb/rygl/icon-bc.png" alt="" class="icon" />
	                &nbsp;添加
	      </el-button>
	      
	      
		 */
		
		Node elbuttonadd = new VueNode("el-button","&nbsp;添加","")
				.addAttr(new VueAttr("@click","add"+tablenameCap+"Info"))
				;
		this.divbtn.append(elbuttonadd);
		
//		Node svg = new VueNode("svg").addAttr(new VueAttr("class","icon"));
//		elbuttonadd.append(svg);
//		
//		Node use = new VueNode("use").addAttr(new VueAttr("xlink:href","#el-icon-gwy-add"));
//		svg.append(use);
		//增加图标 2024年1月15日14:25:40
		Node img = new VueNode("img").addAttr(new VueAttr("class","icon"))
				.addAttr(new VueAttr("src", "@/assets/imgs/zzgb/rygl/icon-bc.png"));
		elbuttonadd.append(img);
		
		
		/* 
		 *<el-table-column
            label="操作"
            width="220">
            <template slot-scope="scope">
              <el-button @click="handleEdit(scope.row)" type="text" icon="el-icon-edit" size="small">编辑</el-button>
              <el-button @click="dialog.fgld00=scope.row.fgld00;dialog.visible=true;dialog.msg = '确定删除，是否继续？';" type="text" size="small" icon="el-icon-delete" style="color: red;">删除</el-button>
            </template>
          </el-table-column>
		 */
		//el_table加上一列 编辑列
		VueNode col = ElTableColTpl.getTPL();
		//this.el_table.append(col);  
		this.editorelbutton = col;
		col.addAttr(new VueAttr("label", "操作"))
			//.addAttr(new VueAttr("width", "220"))
			//.addAttr(new VueAttr("fixed","right"))
			.addAttr(new VueAttr("align","center"))
			;
		Node template = new VueNode("template")
				.addAttr(new VueAttr("slot-scope", "scope"))
				;
		col.append(template);
		//按钮居中 2024年1月15日14:25:23
		Node templateDiv = new VueNode("div")
				.addAttr(new VueAttr("style", "text-align: center;"))
				;
		template.append(templateDiv);
		
		Node elbutton = new VueNode("el-button","编辑","")
				.addAttr(new VueAttr("@click.stop", "handle"+tablenameCap+"Edit(scope.row)"))
				.addAttr(new VueAttr("type", "text"))
				//.addAttr(new VueAttr("icon", "el-icon-edit"))
				.addAttr(new VueAttr("size", "small"))
				;
		//2024年1月15日14:51:54
		img = new VueNode("img").addAttr(new VueAttr("class","icon"))
				.addAttr(new VueAttr("src", "@/assets/imgs/zzgb/grid_icon/edit.svg"));
		elbutton.append(img);
		
		templateDiv.append(elbutton);
		elbutton = new VueNode("el-button","删除","")
				.addAttr(new VueAttr("@click.stop", "dialog."+config.getPk()+"=scope.row."+config.getPk()+";dialog.visible=true;dialog.msg = '确定删除，是否继续？';dialog.wintitle='系统提示';"))
				.addAttr(new VueAttr("type", "text"))
				//.addAttr(new VueAttr("icon", "el-icon-delete"))
				.addAttr(new VueAttr("size", "small"))
				//.addAttr(new VueAttr("style", "color: red;"))
				;
		//2024年1月15日14:51:38
		img = new VueNode("img").addAttr(new VueAttr("class","icon"))
				.addAttr(new VueAttr("src", "@/assets/imgs/zzgb/grid_icon/del.svg"));
		elbutton.append(img);
		
		templateDiv.append(elbutton);
		
		
		
		
		
		
		
		Node titles = new VueNode("div").addAttr(new VueAttr("class","titles")).addAttr(new VueAttr("v-if","booleanObj.isShow"+tablenameCap+"Form"));
		VueNode titlesP = new VueNode("p","详情","");
		titles.append(titlesP);
		
		this.title2 = titles;
		
		Node elform = new VueNode("el-form")
				.addAttr(new VueAttr(":rules","rules"))
				.addAttr(new VueAttr("ref",tablenameL + "Form"))
				.addAttr(new VueAttr(":model",tablenameL+"EntityData"))
				.addAttr(new VueAttr("label-width","100px"))
				.addAttr(new VueAttr("class","elform"))
				.addAttr(new VueAttr(":disabled","booleanObj.is"+tablenameCap+"EditDisabled"))
				.addAttr(new VueAttr("v-if","booleanObj.isShow"+tablenameCap+"Form"))
				;
		this.info_page.append(titles).append(elform);
		this.elform = elform;
		/*
	<div class="titles">
      <p>详情</p>
    </div>
    <el-form
      :rules="rules"
      ref="form"
      :model="y34042"
      label-width="100px"
      class="elform"
      :disabled="booleanObj.isY34042EditDisabled"-----后面加
    >
    三个一行
      <el-row>
        <el-col :span="8">
          <el-form-item label="姓名" prop="fgld01" >
              <el-input v-model="y34042.fgld01" placeholder="请输入姓名"></el-input>
          </el-form-item> 
        </el-col>   
      </el-row>
      <el-row>
        <el-col :push="10">
            <el-button @click="saveInfoValidate" v-if="!booleanObj.isY34042EditDisabled">
              <svg class="icon" >
                    <use xlink:href="#el-icon-gwy-save" />
              </svg>&nbsp;保存
            </el-button>
            <el-button @click="booleanObj.isY34042EditDisabled=true;$refs['form'].resetFields()" style="" v-if="!booleanObj.isY34042EditDisabled">
                <svg class="icon" >
                    <use xlink:href="#el-icon-gwy-close" />
                </svg>&nbsp;取消
            </el-button>
        </el-col>
      </el-row>
    </el-form>
    */
		//加上维护字段，三个一行
		/*
		  <el-row>
	        <el-col :span="8">
	          <el-form-item label="姓名" prop="fgld01" >
	              <el-input v-model="y34042.fgld01" placeholder="请输入姓名"></el-input>
	          </el-form-item> 
	        </el-col>   
	      </el-row>
	    */
		
		int colIndex = 0;
		//维护的表单form节点  里面有个保存和取消按钮 先删除
		Node elrow = null;
		List<HashMap<String, String>> list = config.getTableMetaData();
		StringBuilder rules = new StringBuilder();
		for(int i=0; i<list.size(); i++) {
			HashMap<String, String> column = list.get(i);
			//表格列
			if(ItemEnum.A.toString().equals(column.get("visible"))||ItemEnum.D.toString().equals(column.get("visible"))) {
				if(colIndex++%3==0) {//三个一行
					elrow = new VueNode("el-row");
					elform.append(elrow);
				}
				
				
				//判断下拉选或弹出框
				if("文本".equals(column.get("editortype"))){//
					Node elcol = new VueNode("el-col").addAttr(new VueAttr(":span", "8"));
					elrow.append(elcol);
					Node elformitem = new VueNode("el-form-item")
							.addAttr(new VueAttr("label", column.get("comments")))
							.addAttr(new VueAttr("prop", column.get("column_name").toLowerCase()))
							;
					elcol.append(elformitem);
					
					Node elinput = new VueNode("el-input")
							.addAttr(new VueAttr("v-model", tablenameL+"EntityData."+column.get("column_name").toLowerCase()))
							.addAttr(new VueAttr("placeholder", "请输入"+column.get("comments")))
							;
					elformitem.append(elinput);
				}else if("弹出框".equals(column.get("editortype"))) {
					Node eplpublicwindowedit = new VueNode("epl-public-window-edit")
							.addAttr(new VueAttr("colspan", "8"))
							.addAttr(new VueAttr("labelWidth", "100", "与上面label-width保持一致"))
							.addAttr(new VueAttr("label", column.get("comments")))
							.addAttr(new VueAttr("name", column.get("column_name").toLowerCase()))//这个相当于上面的prop跟校验的key相对应
							//.addAttr(new VueAttr("style", "width: 100%"))//宽度 2024年1月15日15:15:41
							.addAttr(new VueAttr(":property", tablenameL+"EntityData."+column.get("column_name").toLowerCase()))
							//.addAttr(new VueAttr(":hideLabel", "true"))
							.addAttr(new VueAttr(":codetype", tablenameL+"EntityData."+column.get("column_name").toLowerCase()+".codetype"))
							//.addAttr(new VueAttr(":p", tablenameL+"EntityData."+column.get("column_name").toLowerCase()+".p"))//在property里面设置
							.addAttr(new VueAttr("placeholder", "请选择"+column.get("comments")))
							;
					elrow.append(eplpublicwindowedit);
					((VueNode)eplpublicwindowedit).setAttrNotNewLine(false);
				}else if("下拉选".equals(column.get("editortype"))) {
					Node epselect = new VueNode("ep-select")
							.addAttr(new VueAttr("colspan", "8"))
							.addAttr(new VueAttr("isHideKey", "true"))
							.addAttr(new VueAttr("labelWidth", "100", "与上面label-width保持一致"))
							.addAttr(new VueAttr(":custom-path", "dataUrl"))
							//.addAttr(new VueAttr("labelWidth", "0"))
							.addAttr(new VueAttr("label", column.get("comments")))
							.addAttr(new VueAttr("name", column.get("column_name").toLowerCase()))//这个相当于上面的prop跟校验的key相对应
							.addAttr(new VueAttr(":property", tablenameL+"EntityData."+column.get("column_name").toLowerCase()))
							.addAttr(new VueAttr(":codetype", tablenameL+"EntityData."+column.get("column_name").toLowerCase()+".codetype"))
							.addAttr(new VueAttr("placeholder", "请选择"+column.get("comments")))
							;
					elrow.append(epselect);
					((VueNode)epselect).setAttrNotNewLine(false);
				}else if("公务员常用时间控件".equals(column.get("editortype"))) {//2024年1月15日19:25:02 时间控件
					Node epselect = new VueNode("epl-timeinput")
							.addAttr(new VueAttr("colspan", "8"))
							.addAttr(new VueAttr("labelWidth", "100", "与上面label-width保持一致"))
							.addAttr(new VueAttr("label", column.get("comments")))
							.addAttr(new VueAttr("name", column.get("column_name").toLowerCase()))//这个相当于上面的prop跟校验的key相对应
							.addAttr(new VueAttr(":property", tablenameL+"EntityData."+column.get("column_name").toLowerCase()))
							.addAttr(new VueAttr("placeholder", "示例：202301或20230101"))
							;
					elrow.append(epselect);
					((VueNode)epselect).setAttrNotNewLine(false);
				}
				
				
				//生成校验规则 非空
				
				if("必填".equals(column.get("validate"))) {
					rules.append("\t\t\t\t").append(column.get("column_name").toLowerCase()).append(": [\n");
					rules.append("\t\t\t\t\t").append("{ required: true, message: '请输入"+column.get("comments")+"', trigger: 'blur' }").append(",\n");
					rules.append("\t\t\t\t").append("],\n");
				}
				
			}
			
		}
		config.setRules(rules.toString());
		
		//以下注释 ；按钮放到form外面
//		elrow = new VueNode("el-row");
//		Node elcol = new VueNode("el-col").addAttr(new VueAttr(":push","10"));
//		elrow.append(elcol);
//		
//		elbutton = new VueNode("el-button","&nbsp;保存","")
//				.addAttr(new VueAttr("@click","save"+tablenameCap+"InfoValidate"))
//				.addAttr(new VueAttr("v-if","!booleanObj.is"+tablenameCap+"EditDisabled"))
//				;
//		elcol.append(elbutton);
//		
//		Node svg = new VueNode("svg").addAttr(new VueAttr("class","icon"));
//		elbutton.append(svg);
//		
//		Node use = new VueNode("use").addAttr(new VueAttr("xlink:href","#el-icon-gwy-save"));
//		svg.append(use);
//		
//		
//		elbutton = new VueNode("el-button","&nbsp;取消","")
//				.addAttr(new VueAttr("@click","booleanObj.is"+tablenameCap+"EditDisabled=true;reset"+tablenameCap+"Fields()"))
//				.addAttr(new VueAttr("v-if","!booleanObj.is"+tablenameCap+"EditDisabled"))
//				;
//		elcol.append(elbutton);
//		
//		svg = new VueNode("svg").addAttr(new VueAttr("class","icon"));
//		elbutton.append(svg);
//		
//		use = new VueNode("use").addAttr(new VueAttr("xlink:href","#el-icon-gwy-close"));
//		svg.append(use);
//		
//		//加上保存，取消按钮
//		elform.append(elrow);
		//2024年1月15日16:08:00 按钮放到form外面
		
		Node btnDiv = new VueNode("div").addAttr(new VueAttr("style","text-align: center; margin: 10px 0px;"));
		
		elbutton = new VueNode("el-button","&nbsp;保存","")
				.addAttr(new VueAttr("@click","save"+tablenameCap+"InfoValidate"))
				.addAttr(new VueAttr("v-if","!booleanObj.is"+tablenameCap+"EditDisabled"))
				;
		img = new VueNode("img").addAttr(new VueAttr("class","icon"))
				.addAttr(new VueAttr("src", "@/assets/imgs/zzgb/rygl/bc.png"));
		elbutton.append(img);
		btnDiv.append(elbutton);
		
		elbutton = new VueNode("el-button","&nbsp;关闭","")
				.addAttr(new VueAttr("@click","booleanObj.isShow"+tablenameCap+"Form=false;reset"+tablenameCap+"Fields()"))
				.addAttr(new VueAttr("v-if","booleanObj.is"+tablenameCap+"EditDisabled&&booleanObj.isShow"+tablenameCap+"Form"))
				;
		img = new VueNode("img").addAttr(new VueAttr("class","icon"))
				.addAttr(new VueAttr("src", "@/assets/imgs/zzgb/rygl/qx.png"));
		elbutton.append(img);
		btnDiv.append(elbutton);
		
		elbutton = new VueNode("el-button","&nbsp;取消","")
				.addAttr(new VueAttr("@click","booleanObj.is"+tablenameCap+"EditDisabled=true;booleanObj.isShow"+tablenameCap+"Form=false;reset"+tablenameCap+"Fields()"))
				.addAttr(new VueAttr("v-if","!booleanObj.is"+tablenameCap+"EditDisabled"))
				;
		img = new VueNode("img").addAttr(new VueAttr("class","icon"))
				.addAttr(new VueAttr("src", "@/assets/imgs/zzgb/rygl/qx.png"));
		elbutton.append(img);
		btnDiv.append(elbutton);
		
		
		this.info_page.append(btnDiv);
    
    /*
    <!-- 弹框提示 -->
    <epl-window
      :hasHelp="false"
      :title="dialog.wintitle"
      width="400"
      height="250"
      append-to-body
      :visible.sync="dialog.visible"
      center
    >
      <hy-dialog
        :show-dialog="dialog.visible"
        :msg="dialog.msg"
        @closeStMsgwin="dialog.visible = false"
        @confirmMsg="handleDelete"
      ></hy-dialog>
    </epl-window>
		 */		
		Node eplwindow = new VueNode("epl-window","弹框提示")
				.addAttr(new VueAttr(":hasHelp","false"))
				.addAttr(new VueAttr(":title","dialog.wintitle"))
				.addAttr(new VueAttr("width","400"))
				.addAttr(new VueAttr("height","250"))
				.addAttr(new VueAttr("append-to-body"))
				.addAttr(new VueAttr(":visible.sync","dialog.visible"))
				.addAttr(new VueAttr("center"))
				;
		
		Node hydialog = new VueNode("hy-dialog")
				.addAttr(new VueAttr(":show-dialog","dialog.visible"))
				.addAttr(new VueAttr(":msg","dialog.msg"))
				.addAttr(new VueAttr("@closeStMsgwin","dialog.visible = false"))
				.addAttr(new VueAttr("@confirmMsg","handle"+tablenameCap+"Delete"))
				;
		
		eplwindow.append(hydialog);
		
		//加入详情信息及弹出提示框
		this.info_page.append(eplwindow);
		
		this.eplwindow = eplwindow;
		
	}
	
	/**
	 * 2023年10月18日13:58:40
	 *  分页内容,加到infobox中
	 */
	private void createPaginationNodes(TableMetaDataConfig config) {
		String tablename = config.getTablename();
		String tablenameL = tablename.toLowerCase();
		String tablenameCap = Constants.initcap(tablenameL);
		/*
			<div style="display: flex; border: 1px solid rgb(220, 223, 230);height: 40px;align-items:center;">
				<div>
					<el-pagination
					@size-change="queryAa10List"
					@current-change="queryAa10List"
					:current-page.sync="pageInfo.currentPage"
					:page-sizes="[50, 100, 200]"
					class="pagination"
					:page-size.sync="pageInfo.pageSize"
					layout="total, sizes, prev, pager, next, jumper"
					:total.sync="pageInfo.total">
					</el-pagination>
				</div>
				<div style="margin-left: 20px;">
					<el-button type="primary" @click="queryAa10List" icon="el-icon-refresh">刷新</el-button>
				</div>
			</div>
		*/
		Node paginationdiv = new VueNode("div","分页bar")
				.addAttr(new VueAttr("style","display: flex; border: 1px solid rgb(220, 223, 230);height: 40px;align-items:center;"))
				;
		this.pagination = paginationdiv;
		paginationdiv.append(new VueNode("div"));
		Node elpagination = new VueNode("el-pagination")
				.addAttr(new VueAttr("@size-change","query"+tablenameCap+"List"))
				.addAttr(new VueAttr("@current-change","query"+tablenameCap+"List"))
				.addAttr(new VueAttr(":current-page.sync",tablenameL+"PageInfo.currentPage"))
				.addAttr(new VueAttr(":page-sizes","[20, 50, 100, 200]"))
				.addAttr(new VueAttr("class","pagination"))
				.addAttr(new VueAttr(":page-size.sync",tablenameL+"PageInfo.pageSize"))
				.addAttr(new VueAttr("layout","total, sizes, prev, pager, next, jumper"))
				.addAttr(new VueAttr(":total.sync",tablenameL+"PageInfo.total"))
				;
		paginationdiv.getNode(0).append(elpagination);
		
		paginationdiv.append(new VueNode("div").addAttr(new VueAttr("style","margin-left: 20px;")));
		
		Node elbutton = new VueNode("el-button","刷新","")
				.addAttr(new VueAttr("@click","query"+tablenameCap+"List"))
				.addAttr(new VueAttr("type","primary"))
				.addAttr(new VueAttr("icon","el-icon-refresh"))
				;
		paginationdiv.getNode(1).append(elbutton);
		
		this.info_box.append(paginationdiv);
	}
	
	
	private void createExportExcelNodes(TableMetaDataConfig config) {
		String tablename = config.getTablename();
		String tablenameL = tablename.toLowerCase();
		String tablenameCap = Constants.initcap(tablenameL);
		/*
		 <el-button size="mini" @click="addInfo" ref="add"  style="float:right;">
	        <svg class="icon">
	          <use xlink:href="#el-icon-gwy-add" /></svg>
	          &nbsp;新增
	      </el-button>
		 */
		
		Node elbuttonadd = new VueNode("el-button","&nbsp;导出Excel","")
				.addAttr(new VueAttr("@click","export"+tablenameCap+"Excel"))
				;
		this.divbtn.append(elbuttonadd);
		
//		Node svg = new VueNode("svg").addAttr(new VueAttr("class","icon"));
//		elbuttonadd.append(svg);
//		
//		Node use = new VueNode("use").addAttr(new VueAttr("xlink:href","#el-icon-gwy-excelmbdy"));
//		svg.append(use);
		
		Node img = new VueNode("img").addAttr(new VueAttr("class","icon"))
				.addAttr(new VueAttr("src", "@/assets/imgs/zzgb/grid_icon/dcExcel.svg"));
		elbuttonadd.append(img);
		
	}
	
}









