package me.zoulei.frontend.templete.grid;

import lombok.Data;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.frontend.node.Node;
import me.zoulei.frontend.node.css.CSSAttr;
import me.zoulei.frontend.node.css.CSSClassNameNode;
import me.zoulei.frontend.node.vue.VueAttr;
import me.zoulei.frontend.node.vue.VueNode;

/**
 * 
* @author zoulei 
* @date 2023年9月28日 下午3:26:19 
* @description 生成表格相关的样式
 */

@Data
public class ElTableCssTpl {
	
	
	private CSSClassNameNode info_page;
	private CSSClassNameNode titles;
	private CSSClassNameNode info_box;
	private Node styleTag;

	public ElTableCssTpl(TableMetaDataConfig config){
		this.createNode(config);
	}
	
	public void createNode(TableMetaDataConfig config) {
		VueNode styleTag = (VueNode) new VueNode("style").addAttr(new VueAttr("lang","less")).addAttr(new VueAttr("scoped"));
		styleTag.setAttrNotNewLine(true);
		this.styleTag = styleTag;
		CSSClassNameNode info_page = new CSSClassNameNode("info-page");
		styleTag.append(info_page);
		info_page.addAttr(new CSSAttr("padding", "0px 20px"));
		info_page.addAttr(new CSSAttr("overflow", "hidden"));
		info_page.addAttr(new CSSAttr("width", "100%"));
		this.info_page = info_page;
		
		CSSClassNameNode titles = new CSSClassNameNode("titles");
		info_page.append(titles);
		titles.addAttr(new CSSAttr("height", "50px"));
		titles.addAttr(new CSSAttr("line-height", "50px"));
		titles.addAttr(new CSSAttr("color", "#4098ff"));
		titles.addAttr(new CSSAttr("border-bottom", "1px solid #4098ff"));
		titles.addAttr(new CSSAttr("font-size", "16px"));
		titles.addAttr(new CSSAttr("display", "flex","”弹性布局”，用来为盒状模型提供最大的灵活性"));
		titles.addAttr(new CSSAttr("justify-content", "space-between","两端对齐，项目之间的间隔都相等。"));
		titles.addAttr(new CSSAttr("align-items", "center","垂直中点对齐"));
		this.titles = titles;
		
		CSSClassNameNode p = new CSSClassNameNode("p");
		p.setPrefix("");//前缀不要加“.”了
		titles.append(p);
		p.addAttr(new CSSAttr("display", "flex"));
		p.addAttr(new CSSAttr("align-items", "center"));
		
		CSSClassNameNode before = new CSSClassNameNode("before");
		before.setPrefix("&::");//前缀不要加“.”了
		p.append(before);
		before.addAttr(new CSSAttr("content", "''"));
		before.addAttr(new CSSAttr("display", "block"));
		before.addAttr(new CSSAttr("width", "5px"));
		before.addAttr(new CSSAttr("height", "18px"));
		before.addAttr(new CSSAttr("border-radius", "3px"));
		before.addAttr(new CSSAttr("background", "#4098ff"));
		before.addAttr(new CSSAttr("margin-right", "10px"));
		
		CSSClassNameNode info_box = new CSSClassNameNode("info-box");
		this.info_box = info_box;
		info_page.append(info_box);
		CSSClassNameNode headerCell = new CSSClassNameNode("headerCell","表头样式");
		headerCell.setPrefix("/deep/.");//前缀不要加“.”了
		info_box.append(headerCell);
		headerCell.addAttr(new CSSAttr("padding", "0 0 0 0 !important"));
		headerCell.addAttr(new CSSAttr("height", "40px"));
		headerCell.addAttr(new CSSAttr("color", "#555"));
		headerCell.addAttr(new CSSAttr("font-weight", "normal"));
		headerCell.addAttr(new CSSAttr("background-image", "linear-gradient(180deg, #ffffff 0%, #ededed 100% ) !important"));
		/*
		 /deep/.el-table__body .el-button--text span {
	        margin: 0 10px;
	        display: inline-block;
	      }
	      /deep/.el-table td {
	          padding: 2px 0px;
	      }
		 */
		CSSClassNameNode eltablebtn = new CSSClassNameNode("el-table__body .el-button--text span","表格单元格内按钮样式");
		eltablebtn.setPrefix("/deep/.");//前缀不要加“.”了
		//eltablebtn.addAttr(new CSSAttr("margin", "0 10px"));
		eltablebtn.addAttr(new CSSAttr("display", "inline-block"));
		info_box.append(eltablebtn);
		
		CSSClassNameNode eltabletd = new CSSClassNameNode("el-table td");
		eltabletd.setPrefix("/deep/.");//前缀不要加“.”了
		eltabletd.addAttr(new CSSAttr("padding", "2px 0px"));
		info_box.append(eltabletd);
		
		
		/*
		 * 2023年10月18日15:44:14
		 // 分页
			.pagination {
				float: none; 
				margin-top: 0px;
				height: 40px;
			}
			// 分页
			/deep/.pagination .el-pager li:not(.disabled).active {
				background-color: #409EFF;
				color: #FFF;
				
			}
		 */
		if(config.isPagination()) {
			Node pagination = new CSSClassNameNode("pagination")
					.addAttr(new CSSAttr("float", "none"))
					.addAttr(new CSSAttr("margin-top", "0px"))
					.addAttr(new CSSAttr("height", "40px"))
					;
			info_box.append(pagination);
			CSSClassNameNode pagination2 = new CSSClassNameNode("pagination .el-pager li:not(.disabled).active");
			pagination2.setPrefix("/deep/.");
			pagination2.addAttr(new CSSAttr("background-color", "#409EFF"))
					  .addAttr(new CSSAttr("color", "#FFF"))
					  ;
			info_box.append(pagination2);
		}
		
		
		
		
		
		
		//增删改查的内容的样式 加在page下
		/*
		 * .elform{ margin-top: 10px; overflow-y: auto; overflow-x: hidden; }
		 */
		if(config.isIscrud()) {
			Node elform = new CSSClassNameNode("elform","编辑区域的滚动条，高度在mounted里js计算")
					.addAttr(new CSSAttr("margin-top", "10px"))
					.addAttr(new CSSAttr("overflow-y", "auto"))
					.addAttr(new CSSAttr("overflow-x", "hidden"))
					.addAttr(new CSSAttr("background", "#f4f4f4", "表单背景颜色"))
					.addAttr(new CSSAttr("padding-top", "15px"))
					;
			info_page.append(elform);
			
			Node elrow = new CSSClassNameNode("el-row","表单控件的行样式")
					.addAttr(new CSSAttr("display", "flex"))
					.addAttr(new CSSAttr("align-items", "center"))
					;
			((CSSClassNameNode)elrow).setPrefix("/deep/.");
			elform.append(elrow);
			
			Node elformitem = new CSSClassNameNode("el-form-item","标签及input的父级")
					.addAttr(new CSSAttr("display", "flex"))
					.addAttr(new CSSAttr("align-items", "center"))
					;
			//((CSSClassNameNode)elformitem).setPrefix("/deep/.");
			elrow.append(elformitem);
			
			Node elformitem__label = new CSSClassNameNode("el-form-item__label","标签")
					.addAttr(new CSSAttr("line-height", "unset"))
					;
			//((CSSClassNameNode)elformitem__label).setPrefix("/deep/.");
			elformitem.append(elformitem__label);
			
			Node elformitem__content = new CSSClassNameNode("el-form-item__content","输入框")
					.addAttr(new CSSAttr("width", "calc(100% - 90px)"))
					.addAttr(new CSSAttr("margin-left", "0px !important"))
					;
			//((CSSClassNameNode)elformitem__content).setPrefix("/deep/.");
			elformitem.append(elformitem__content);
			
			Node elselect = new CSSClassNameNode("el-select","下拉选类型的输入框")
					.addAttr(new CSSAttr("width", "100%"))
					;
			//((CSSClassNameNode)elselect).setPrefix("/deep/.");
			elformitem__content.append(elselect);
		}
		/*
		 /deep/.el-row{
	        display: flex;
	        align-items: center;
	        /deep/.el-form-item{
	          display: flex;
	          align-items: center;
	          /deep/.el-form-item__label{
	            line-height: unset;
	          }
	          /deep/.el-form-item__content{
	            width: calc(100% - 90px);
	            margin-left: 0px !important;
	            /deep/.el-select{
	              width: 100%;
	            }
	          }
	        }
	      }
		 */
		
	}
}
/*
<style lang="less" scoped>

.info-page {
  padding: 0px 20px;
  overflow: hidden;
  width: 100%;
  .titles {
    height: 50px;
    line-height: 50px;
    color: #4098ff;
    border-bottom: 1px solid #4098ff;
    font-size: 16px;
    //”弹性布局”，用来为盒状模型提供最大的灵活性
    display: flex;
    //两端对齐，项目之间的间隔都相等。
    justify-content: space-between;
    // 垂直中点对齐
    align-items: center;
    p {
      display: flex;
      align-items: center;
      &::before {
        content: "";
        display: block;
        width: 5px;
        height: 18px;
        border-radius: 3px;
        background: #4098ff;
        margin-right: 10px;
      }
    }
  }
  .info-box{
  	//表头样式
	/deep/.headerCell {
	  padding: 0 0 0 0 !important;
	  height: 40px;
	  color: #555;
	  font-weight: normal;
	  background-image: linear-gradient(
	    180deg,
	    #ffffff 0%,
	    #ededed 100%
	  ) !important;
	}
  }
  
}
</style>



*/