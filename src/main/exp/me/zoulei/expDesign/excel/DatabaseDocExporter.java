package me.zoulei.expDesign.excel;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PageMargin;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import dm.jdbc.util.StringUtil;
import lombok.extern.log4j.Log4j;
import me.zoulei.backend.jdbc.utils.CommQuery;

@Log4j
public class DatabaseDocExporter {

    

    private static String translateDataType(String oracleType) {
        switch (oracleType) {
            case "VARCHAR2": return "C";
            case "VARCHAR": return "C";
            case "NUMBER": return "N";
            case "DATE": return "D";
            case "DATETIME": return "D";
            case "CLOB": return "CB";
            case "BLOB": return "BB";
            case "TIMESTAMP": return "T";
            default: return oracleType;
        }
    }
    private static String translateCodeType(String comment, String cs, String column_name) {
    	if((cs+",").contains(column_name+",")) {
    		return "P";
    	}
        if(comment!=null) {
        	if(comment.contains("代码")) {
        		return "Y";
        	}else if(comment.contains("编码")) {
        		return "B";
        	}
        }
        return "";
    }

   

    
	public static String export(String selectedItem) throws Exception {
		CommQuery cq = new CommQuery();
		String tableSQL = String.format(SQLAdapterUtil.getTableSQL(), selectedItem, selectedItem, selectedItem);
		log.info("获取表信息--\n"+tableSQL);
		List<HashMap<String, String>> tables = cq.getListBySQL2(tableSQL);
		
		String columnSQL = String.format(SQLAdapterUtil.getColumnSQL(), selectedItem, selectedItem, selectedItem);
		log.info("获取字段信息--\n"+columnSQL);
		List<HashMap<String, String>> columns = cq.getListBySQL2(columnSQL);
		
		//组装数据结构
		Map<String, Object[]> source = new LinkedHashMap<String, Object[]>();
		tables.forEach(tableinfo->{
			String tablename = tableinfo.get("table_name");
			source.put(tablename, new Object[] {tableinfo, new ArrayList<HashMap<String, String>>()});
		});
		
		columns.forEach(columninfo->{
			String tablename = columninfo.get("table_name");
			Object[] o = source.get(tablename);
			//log.info(tablename);
			if("B01BLOCK".equals(tablename)) {
				//log.info(o);
			}
			List<HashMap<String, String>> ts = (List<HashMap<String, String>>)o[1];
			ts.add(columninfo);
		});
		
		//生成文档
		return exportToExcel(source,selectedItem);
	}

	/**
	 * 生成excel文档
	 * ====================================================================================================
	 * 方法创建日期: 2025年3月18日  16:34:33<br>
	 * 方法创建人员: zoulei<br>
	 * 方法最后修改日期: <br>
	 * 方法最后修改人员: <br>
	 * 方法功能描述: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 * @param source
	 * @return 
	 * @throws Exception 
	 */
	private static String exportToExcel(Map<String, Object[]> source, String selectedItem) throws Exception {
		//新建excel工作簿
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		//合并项
		List<CellRangeAddress> merges = new ArrayList<>();
		//新建sheet页
		SXSSFSheet sheet = workbook.createSheet(selectedItem.replaceAll("[/\\\\?*\\[\\]'']", ""));
		//居中样式
		CellStyle styleCENTER = ExportExcelUtil.getCellStyleZhengWen(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9);
		//居左样式
		CellStyle styleLEFT = ExportExcelUtil.getCellStyleZhengWen(workbook,HorizontalAlignment.LEFT,"仿宋",(short)9);
		//表头样式
		CellStyle styleTitle14 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"14",true);
		CellStyle styleTitle1 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"1",true);
		CellStyle styleTitle12 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"12",true);
		CellStyle styleTitle2 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"2",true);
		CellStyle styleTitle0 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"0",true);
		CellStyle styleTitle4 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"4",true);
		//正文样式
		CellStyle styleZWL1 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.LEFT,"仿宋",(short)9,"1",false);
		CellStyle styleZWL2 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.LEFT,"仿宋",(short)9,"2",false);
		CellStyle styleZWL3 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.LEFT,"仿宋",(short)9,"3",false);
		CellStyle styleZWC12 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"12",false);
		CellStyle styleZWC4 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"4",false);
		CellStyle styleZWC34 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"34",false);
		CellStyle styleZWC3 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"3",false);
		CellStyle styleZWC23 = ExportExcelUtil.getCellStyleTitle1(workbook,HorizontalAlignment.CENTER,"仿宋",(short)9,"23",false);
		int rowIndex = 0;//行序号
		int cellIndex = 0;//列序号
		
		Row row = null;
		
		//单元格
		Cell cell = null;
		int table_i = 0;
		for (Map.Entry<String, Object[]> entry : source.entrySet()) {
			table_i++;
			String key = entry.getKey();
			Object[] val = entry.getValue();
			HashMap<String, String> tableinfo = (HashMap<String, String>) val[0];
			String table_name = tableinfo.get("table_name");
			String table_comment = tableinfo.get("table_comment");
			String cs = tableinfo.get("cs");//主键信息
			String constraint_name = tableinfo.get("constraint_name");
			List<HashMap<String, String>> colinfo = (List<HashMap<String, String>>)val[1];
			//新增一个空行
			row = ExportExcelUtil.insertRow(sheet,rowIndex++);
			row.setHeightInPoints(15.5f);
			
			setRow(sheet, rowIndex++, styleTitle14, styleZWL1, row, cell, "指标表编码", table_name, merges,styleTitle1,styleZWC12);
			setRow(sheet, rowIndex++, styleTitle4, styleLEFT, row, cell, "指标表名称", table_comment, merges,styleTitle0,styleZWL2);
			setRow(sheet, rowIndex++, styleTitle4, styleLEFT, row, cell, "指标表备注", table_comment, merges,styleTitle0,styleZWL2);
			
			//索引 行
			row = ExportExcelUtil.insertRow(sheet,rowIndex++);
			cell = row.createCell(1);
			ExportExcelUtil.setCellValue(cell, styleTitle4, "索引");
			//合并列的样式
			for (int i = 2; i <= 9; i++) {
				cell = row.createCell(i);
				if(i==9) {
					cell.setCellStyle(styleTitle2);
				}else {
					cell.setCellStyle(styleTitle0);
				}
				
			}
			//合并单元格
	        CellRangeAddress address = new CellRangeAddress(rowIndex-1, rowIndex-1, 1, 9);
	        merges.add(address);
	        
			//索引行表头
	        row = ExportExcelUtil.insertRow(sheet,rowIndex++);
			cellIndex = 1;//列序号
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle4, "序号");
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle0, "索引名称");
	        
	        cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle0, "索引字段");
			//合并列的样式
			for (int i = cellIndex; i <= cellIndex+3; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(styleTitle0);
			}
			//合并单元格
			address = new CellRangeAddress(rowIndex-1, rowIndex-1, cellIndex-1, cellIndex=cellIndex+3);
	        merges.add(address);
	        cellIndex++;
	        
	        cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle0, "索引备注");
			//合并单元格的列
			cell = row.createCell(cellIndex);
			cell.setCellStyle(styleTitle2);
			//合并单元格
			address = new CellRangeAddress(rowIndex-1, rowIndex-1, cellIndex-1, cellIndex);
	        merges.add(address);
	        cellIndex++;
	        
	        //索引行数据
	        row = ExportExcelUtil.insertRow(sheet,rowIndex++);
			cellIndex = 1;//列序号
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleZWC34, "1");
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleZWC3, constraint_name);
	        
	        cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleZWC3, cs);
			//合并列的样式
			for (int i = cellIndex; i <= cellIndex+3; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(styleZWC3);
			}
			//合并单元格
			address = new CellRangeAddress(rowIndex-1, rowIndex-1, cellIndex-1, cellIndex=cellIndex+3);
	        merges.add(address);
	        cellIndex++;
	        
	        cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleZWC3, "主键");
			//合并单元格的列样式
			cell = row.createCell(cellIndex);
			cell.setCellStyle(styleZWC23);
			//合并单元格
			address = new CellRangeAddress(rowIndex-1, rowIndex-1, cellIndex-1, cellIndex);
	        merges.add(address);
	        cellIndex++;
	        
	        //*************表信息结束**********************************************/
	        //新增一个空行
			row = ExportExcelUtil.insertRow(sheet,rowIndex++);
			
			//新增一行 表头1
			row = ExportExcelUtil.insertRow(sheet,rowIndex++);
			row.setHeightInPoints(22f);
			cellIndex = 1;//列序号
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle14, "序号");
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle1, "指标名称");
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle1, "指标编码");
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle1, "指标");
			//合并列的样式
			for (int i = cellIndex; i <= cellIndex+1; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(styleTitle1);
			}
			//合并单元格
			address = new CellRangeAddress(rowIndex-1, rowIndex-1, cellIndex-1, cellIndex=cellIndex+1);
	        merges.add(address);
	        cellIndex++;
	        cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle1, "代码标识");
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle1, "指标释义");
			//合并列的样式
			cell = row.createCell(cellIndex);
			cell.setCellStyle(styleTitle12);

	        
	        //新增一行 表头2
	        cellIndex = 4;//列序号
			row = ExportExcelUtil.insertRow(sheet,rowIndex++);
			row.setHeightInPoints(28.25f);
			//合并列的样式
			cell = row.createCell(1);
			cell.setCellStyle(styleTitle4);
			cell = row.createCell(2);
			cell.setCellStyle(styleTitle0);
			cell = row.createCell(3);
			cell.setCellStyle(styleTitle0);
			
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle0, "类型");
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle0, "长度");
			cell = row.createCell(cellIndex++);
			ExportExcelUtil.setCellValue(cell, styleTitle0, "非空");
			//合并列的样式
			cell = row.createCell(cellIndex++);
			cell.setCellStyle(styleTitle0);
			//合并列的样式
			cell = row.createCell(cellIndex++);
			cell.setCellStyle(styleTitle0);
			cell = row.createCell(cellIndex++);
			cell.setCellStyle(styleTitle2);
			
			//合并单元格
			address = new CellRangeAddress(rowIndex-2, rowIndex-1, 1, 1);
	        merges.add(address);
	        address = new CellRangeAddress(rowIndex-2, rowIndex-1, 2, 2);
	        merges.add(address);
	        address = new CellRangeAddress(rowIndex-2, rowIndex-1, 3, 3);
	        merges.add(address);
	        address = new CellRangeAddress(rowIndex-2, rowIndex-1, 7, 7);
	        merges.add(address);
	        address = new CellRangeAddress(rowIndex-2, rowIndex-1, 8, 9);
	        merges.add(address);
	        log.info("生成表设计文档："+ table_i + " "+ table_name);
			for (int i = 0; i < colinfo.size(); i++) {
				HashMap<String, String> rowinfo = colinfo.get(i);
				String column_comment = rowinfo.get("column_comment");
				String column_name = rowinfo.get("column_name");
				String data_type = rowinfo.get("data_type");
				String data_length = rowinfo.get("data_length");
				String nullable = rowinfo.get("nullable");
				String data_type_ = translateDataType(data_type);
				String nullable_ = "Y".equals(nullable)?"":"√";
				String code_ = translateCodeType(column_comment,cs,column_name);
				
				//新增一行 表数据
				row = ExportExcelUtil.insertRow(sheet,rowIndex++);
				cellIndex = 1;//列序号
				cell = row.createCell(cellIndex++);//序号
				CellStyle csLocal = null;
				if(i==colinfo.size()-1) {
					ExportExcelUtil.setCellValue(cell, styleZWC34, i+1);
					csLocal = styleZWC3;
				}else {
					ExportExcelUtil.setCellValue(cell, styleZWC4, i+1);
					csLocal = styleCENTER;
				}
				ExportExcelUtil.setCellValue(cell, csLocal, i+1);
				
				
				cell = row.createCell(cellIndex++);//指标名称
				ExportExcelUtil.setCellValue(cell, csLocal, column_comment);
				cell = row.createCell(cellIndex++);//指标编码
				ExportExcelUtil.setCellValue(cell, csLocal, column_name);
				cell = row.createCell(cellIndex++);//类型
				ExportExcelUtil.setCellValue(cell, csLocal, data_type_);
				cell = row.createCell(cellIndex++);//长度
				ExportExcelUtil.setCellValue(cell, csLocal, data_length);
				cell = row.createCell(cellIndex++);//非空
				ExportExcelUtil.setCellValue(cell, csLocal, nullable_);
		        cell = row.createCell(cellIndex++);//代码标识
				ExportExcelUtil.setCellValue(cell, csLocal, code_);
				cell = row.createCell(cellIndex++);//指标释义
				if(i==colinfo.size()-1) {
					ExportExcelUtil.setCellValue(cell, styleZWL3, StringUtil.isEmpty(column_comment)?"":column_comment+"。");
					//合并单元格的列样式
					cell = row.createCell(cellIndex);
					cell.setCellStyle(styleZWC23);
				}else {
					ExportExcelUtil.setCellValue(cell, styleLEFT, StringUtil.isEmpty(column_comment)?"":column_comment+"。");
					//合并单元格的列样式
					cell = row.createCell(cellIndex);
					cell.setCellStyle(styleZWL2);
				}
				
				//合并单元格
				address = new CellRangeAddress(rowIndex-1, rowIndex-1, cellIndex-1, cellIndex);
		        merges.add(address);
			}
			
		}
		
		log.info("数据生成后统一添加合并单元格");
		// 数据生成后统一添加合并单元格
		for (CellRangeAddress region : merges) {
		    sheet.addMergedRegionUnsafe(region);
		}
		log.info("合并单元格结束");
		
		
		log.info("设置列宽");
		// 设置列宽  3.75 11.5  (columnWidths[i] * 256) / 9 - 0.702 * 256   Double.valueOf(width)/8*255.86+184.27
		//columnWidths[i] * 256
		//3.75  11.5  9.25 3.25  4.5  3.63 3.88 11.75  13.5
		//400
        double[] columnWidths = {0, 1125, 3100, 2525, 1000, 1300, 1100, 1150, 3170,3600};
        for (int i = 0; i < columnWidths.length; i++) {
        	sheet.setColumnWidth(i, ((Double)columnWidths[i]).intValue());
        }

        // 设置打印区域
//        sheet.setAutobreaks(true);
//        PrintSetup printSetup = sheet.getPrintSetup();
//        printSetup.setLandscape(true);
//        sheet.setFitToPage(true);
        // 获取打印设置并设置打印方向、纸张大小和缩放比例
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // 设置为A4纸张
        printSetup.setScale((short)135);
        //sheet.setZoom(135); // 设置缩放比例为75%

		sheet.setMargin(PageMargin.BOTTOM,( double ) 0.5 );// 页边距（下）
		sheet.setMargin(PageMargin.LEFT,( double ) 0.1 );// 页边距（左）
		sheet.setMargin(PageMargin.RIGHT,( double ) 0.1 );// 页边距（右）
		sheet.setMargin(PageMargin.TOP,( double ) 0.5 );// 页边距（上）
		sheet.setHorizontallyCenter(true);//设置打印页面为水平居中
		sheet.setVerticallyCenter(true);//设置打印页面为垂直居中使用POI输出Excel时打印页面的设置
		
		//获取跟目录
		String baseDir = System.getProperty("user.dir");
		String expdir = "文档导出";
		//生成目录
		String gendir = baseDir + "/" + expdir;
		
		File f_gendir = new File(gendir);
		if(f_gendir.isDirectory()) {
			
			//FileUtils.deleteDirectory(f_gendir);
		}else {
			f_gendir.mkdir();
		}
		
		
		String exppath = gendir+"/"+selectedItem+"("+(new SimpleDateFormat("yyyyMMdd").format(new Date()))+").xlsx";
		
		FileOutputStream out = new FileOutputStream(exppath);
		
		workbook.write(out);
		out.close();
		workbook.close();
		return gendir;
	}
	
	
	private static void setRow(Sheet sheet,int rowIndex, CellStyle styleCENTER, CellStyle styleLEFT, Row row, Cell cell, String info1, String info2, List<CellRangeAddress> merges, CellStyle styleTitle, CellStyle styleZW) {
		int cellIndex = 1;
		//新增指标表编码 行
		row = ExportExcelUtil.insertRow(sheet,rowIndex);
		//列1
		cell = row.createCell(cellIndex++);
		ExportExcelUtil.setCellValue(cell, styleCENTER, info1);
		//合并单元格
        CellRangeAddress address = new CellRangeAddress(rowIndex, rowIndex, cellIndex-1, cellIndex);
        merges.add(address);
        //合并的列
        cell = row.createCell(cellIndex++);
        cell.setCellStyle(styleTitle);
        
        //列2
        cell = row.createCell(cellIndex);
        ExportExcelUtil.setCellValue(cell, styleLEFT, info2);
        //合并列的样式
		for (int i = 4; i <= 9; i++) {
			cell = row.createCell(i);
			if(i==9) {
				cell.setCellStyle(styleZW);
			}else {
				cell.setCellStyle(styleLEFT);
			}
			
		}
        //合并单元格
        address = new CellRangeAddress(rowIndex, rowIndex, cellIndex, cellIndex+6);
        merges.add(address);
	}
	
	
	
	
	
	
	
}


