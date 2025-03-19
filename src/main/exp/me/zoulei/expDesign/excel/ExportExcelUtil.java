package me.zoulei.expDesign.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 */
public class ExportExcelUtil {
   
    /**
	 * 该方法用来将Excel中的ABCD列转换成具体的数据
	 * @param column:ABCD列名称
	 * @return integer：将字母列名称转换成数字
	 * **/
    public static int excelColStrToNum(String column) {
        int num = 0;
        int result = 0;
        int length =column.length(); 
        for(int i = 0; i < length; i++) {
            char ch = column.charAt(length - i - 1);
            num = (int)(ch - 'A' + 1) ;
            num *= Math.pow(26, i);
            result += num;
        }
        return result-1;
    }
 
    /**
	 * 该方法用来将具体的数据转换成Excel中的ABCD列
	 * @param int：需要转换成字母的数字
	 * @return column:ABCD列名称
	 * **/
    public static String excelColIndexToStr(int columnIndex) {
    	columnIndex++;
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }
    
    
    
    
    /**
	 * 插入行
	 * @param sheet
	 * @param rowIndex
	 * @return
	 */
	public static Row insertRow(Sheet sheet, Integer rowIndex) {  
        Row row = null;  
        if (sheet.getRow(rowIndex) != null) {  
            int lastRowNo = sheet.getLastRowNum();  
            sheet.shiftRows(rowIndex, lastRowNo, 1);  
        }  
        row = sheet.createRow(rowIndex);  
        //表头行高
      	row.setHeightInPoints(19.5f);
        return row;  
    }  
	
	
	public static void setCellValue(Cell cell,CellStyle style,Object value){
		
		cell.setCellStyle(style);
		if(value!=null&&!"".equals(value)){
			cell.setCellValue(value.toString());
		}
	}
	
	/**
	 * 带上下左右边框的单元格样式
	 * @param workbook
	 * @return
	 */
	public static CellStyle getCellStyleZhengWen(Workbook workbook,HorizontalAlignment ha,String fname,short size){
		CellStyle style = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));
		style.setBorderLeft(BorderStyle.THIN);//左边框   
		style.setBorderRight(BorderStyle.THIN);//右边框  	
		style.setBorderTop(BorderStyle.THIN);//上边框  	
		style.setBorderBottom(BorderStyle.THIN);//下边框  	
		
		style.setAlignment(ha);
		style.setVerticalAlignment(VerticalAlignment.CENTER); 
		Font font =workbook.createFont();  
		font.setFontName(fname);  
		font.setFontHeightInPoints(size);//字体大小
		style.setFont(font);
		style.setWrapText(true);
		return style;
	}
	
	
	/**
	 * 1234代表上右下左粗边框，带背景颜色
	 * @param workbook
	 * @return
	 */
	public static CellStyle getCellStyleTitle1(Workbook workbook,HorizontalAlignment ha,String fname,short size,String medium,boolean color){
		CellStyle style = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));
		if(medium.contains("1")) {
			style.setBorderTop(BorderStyle.MEDIUM);//上边框  	
		}else {
			style.setBorderTop(BorderStyle.THIN);//上边框  	
		}
		if(medium.contains("2")) {
			style.setBorderRight(BorderStyle.MEDIUM);//右边框  	
		}else {
			style.setBorderRight(BorderStyle.THIN);//右边框  	
		}
		if(medium.contains("3")) {
			style.setBorderBottom(BorderStyle.MEDIUM);//下边框  	
		}else {
			style.setBorderBottom(BorderStyle.THIN);//下边框  	
		}
		if(medium.contains("4")) {
			style.setBorderLeft(BorderStyle.MEDIUM);//左边框   
		}else {
			style.setBorderLeft(BorderStyle.THIN);//左边框   
		}
		if(color) {
			style.setFillForegroundColor(new XSSFColor(new byte[] {(byte) 215, (byte) 215, (byte) 215}, new DefaultIndexedColorMap()));
			// 必须设置填充模式！！！
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
	    
		style.setAlignment(ha);
		style.setVerticalAlignment(VerticalAlignment.CENTER); 
		Font font =workbook.createFont();  
		font.setFontName(fname);  
		font.setFontHeightInPoints(size);//字体大小
		style.setFont(font);
		style.setWrapText(true);
		return style;
	}

	/**
	 * 输出表格数据
	 * @param path 表格路径
	 * @param aa10List 数据
	 * @throws Exception 
	 */
	public void writeSheetData(String path, List<Map<String, Object>> cz04List) throws Exception {
		
		//新建excel工作簿
		Workbook workbook = new XSSFWorkbook();
		//新建sheet页
		Sheet sheet = workbook.createSheet("工资人事信息表;（cz04）".replaceAll("[/\\\\?*\\[\\]'']", ""));
		//表头样式
		CellStyle styleTitle = getCellStyleZhengWen(workbook,HorizontalAlignment.CENTER,"黑体",(short)14);
		//居左样式
		CellStyle styleLEFT = getCellStyleZhengWen(workbook,HorizontalAlignment.LEFT,"仿宋_GB2312",(short)14);
		//居中样式
		CellStyle styleCENTER = getCellStyleZhengWen(workbook,HorizontalAlignment.CENTER,"仿宋_GB2312",(short)14);
		//居左样式
		CellStyle styleRIGHT = getCellStyleZhengWen(workbook,HorizontalAlignment.RIGHT,"仿宋_GB2312",(short)14);
		//数字样式
		CellStyle styleNumber = getCellStyleZhengWen(workbook,HorizontalAlignment.CENTER,"Times New Roman",(short)14);

		
	}
	
}
