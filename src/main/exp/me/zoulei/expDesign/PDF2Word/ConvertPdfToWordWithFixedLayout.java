//package me.zoulei.expDesign.PDF2Word;
//import com.spire.pdf.FileFormat;
//import com.spire.pdf.PdfDocument;
// 
//public class ConvertPdfToWordWithFixedLayout {
// 
//    public static void main(String[] args) {
// 
//        //创建一个 PdfDocument 对象
//        PdfDocument doc = new PdfDocument();
// 
//        //加载 PDF 文件
//        doc.loadFromFile("E:\\001整理\\002项目\\008安徽\\滁州\\sql\\HY_ZGGL_GWY_表设计文档_20250317161002.pdf");
// 
//        //将PDF转换为Doc格式文件并保存
//        doc.saveToFile("E:\\001整理\\002项目\\008安徽\\滁州\\sql\\output.doc", FileFormat.DOC);
// 
//        //将PDF转换为Docx格式文件并保存
//        doc.saveToFile("E:\\001整理\\002项目\\008安徽\\滁州\\sql\\output.docx", FileFormat.DOCX);
//        doc.close();
//    }
//}    