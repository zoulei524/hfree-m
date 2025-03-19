//package me.zoulei.expDesign.PDF2Word;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.pdfbox.Loader;
//import org.apache.pdfbox.cos.COSName;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDResources;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.graphics.PDXObject;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.pdfbox.text.TextPosition;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.util.Units;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//
//public class PdfToWordConverter {
//
//    static class TextBlock {
//        String text;
//        boolean isBold;
//        boolean isItalic;
//        float fontSize;
//        String fontColor;
//
//        TextBlock(String text, boolean isBold, boolean isItalic, float fontSize) {
//            this.text = text;
//            this.isBold = isBold;
//            this.isItalic = isItalic;
//            this.fontSize = fontSize;
//        }
//    }
//
//    public static void convertPdfToWord(String pdfPath, String wordPath) throws IOException, InvalidFormatException {
//    	
//        try (
//        		PDDocument pdfDoc = Loader.loadPDF(new File(pdfPath));
//        		XWPFDocument wordDoc = new XWPFDocument()
//        	) {
//
//            List<TextBlock> textBlocks = new ArrayList<>();
//            List<BufferedImage> images = new ArrayList<>();
//
//            // 提取文本和图片
//            extractContent(pdfDoc, textBlocks, images);
//
//            // 构建Word文档
//            buildWordDocument(wordDoc, textBlocks, images);
//
//            // 保存Word文件
//            try (FileOutputStream out = new FileOutputStream(wordPath)) {
//                wordDoc.write(out);
//            }
//        }
//    }
//
//    private static void extractContent(PDDocument doc, List<TextBlock> textBlocks, List<BufferedImage> images) throws IOException {
//        PDFTextStripper stripper = new PDFTextStripper() {
//            @Override
//            protected void writeString(String text, List<TextPosition> textPositions) {
//                for (TextPosition textPos : textPositions) {
//                    PDFont font = textPos.getFont();
//                    String fontName = font.getName().toLowerCase();
//                    boolean isBold = fontName.contains("bold");
//                    boolean isItalic = fontName.contains("italic") || fontName.contains("oblique");
//                    float fontSize = textPos.getFontSize();
//
//                    textBlocks.add(new TextBlock(
//                            textPos.getUnicode(),
//                            isBold,
//                            isItalic,
//                            fontSize
//                    ));
//                }
//            }
//        };
//
//        // 提取文本
//        stripper.getText(doc);
//
//        // 提取图片
//        for (PDPage page : doc.getPages()) {
//            PDResources resources = page.getResources();
//            for (COSName name : resources.getXObjectNames()) {
//                PDXObject xobj = resources.getXObject(name);
//                if (xobj instanceof PDImageXObject) {
//                    images.add(((PDImageXObject) xobj).getImage());
//                }
//            }
//        }
//    }
//
//    private static void buildWordDocument(XWPFDocument doc, List<TextBlock> textBlocks, List<BufferedImage> images) throws IOException, InvalidFormatException {
//        XWPFParagraph paragraph = doc.createParagraph();
//        XWPFRun currentRun = null;
//        boolean needsNewRun = true;
//
//        for (TextBlock block : textBlocks) {
//            if (needsNewRun || currentRun == null) {
//                currentRun = paragraph.createRun();
//                currentRun.setBold(block.isBold);
//                currentRun.setItalic(block.isItalic);
//                currentRun.setFontSize((int) block.fontSize);
//                needsNewRun = false;
//            }
//
//            currentRun.setText(block.text);
//
//            // 简单换行逻辑（根据实际需求改进）
//            if (block.text.endsWith("\n")) {
//                paragraph = doc.createParagraph();
//                needsNewRun = true;
//            }
//        }
//
//        // 插入图片
//        for (BufferedImage image : images) {
//            XWPFParagraph imgPara = doc.createParagraph();
//            XWPFRun run = imgPara.createRun();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            javax.imageio.ImageIO.write(image, "PNG", baos);
//            run.addPicture(new ByteArrayInputStream(baos.toByteArray()),
//                    XWPFDocument.PICTURE_TYPE_PNG, "image.png", Units.toEMU(200), Units.toEMU(200));
//        }
//    }
//
//    public static void main(String[] args) throws InvalidFormatException {
//        try {
//            convertPdfToWord("E:\\001整理\\002项目\\008安徽\\滁州\\sql\\HY_ZGGL_GWY_表设计文档_20250317161002.pdf", "E:\\001整理\\002项目\\008安徽\\滁州\\sql\\output.docx");
//            System.out.println("转换完成！");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}