package me.zoulei.expDesign;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DatabaseDocExporter {

    static class ColumnInfo {
        int seq;
        String columnName;
        String comments; 
        String dataType;
        int dataLength;
        boolean isPrimaryKey;
        String defaultValue;
        String nullable;

        public ColumnInfo(int seq, String columnName, String comments, String dataType, 
                         int dataLength, boolean isPrimaryKey, String defaultValue, String nullable) {
            this.seq = seq;
            this.columnName = columnName;
            this.comments = comments;
            this.dataType = dataType;
            this.dataLength = dataLength;
            this.isPrimaryKey = isPrimaryKey;
            this.defaultValue = defaultValue;
            this.nullable = nullable;
        }
    }

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:dm://127.0.0.1:5236";
        String username = "HY_ZGGL_GWY";
        String password = "HY_ZGGL_GWY";
        String tableName = "CZ01";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            List<ColumnInfo> columns = getTableInfo(conn, tableName);
            exportToExcel(columns, tableName);
            System.out.println("文档生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<ColumnInfo> getTableInfo(Connection conn, String tableName) throws SQLException {
        List<ColumnInfo> columns = new ArrayList<>();
        Set<String> primaryKeys = getPrimaryKeys(conn, tableName);

        String sql = "SELECT c.COLUMN_ID, c.COLUMN_NAME, c.DATA_TYPE, c.DATA_LENGTH, "
                   + "c.NULLABLE, c.DATA_DEFAULT, cc.COMMENTS "
                   + "FROM USER_TAB_COLUMNS c "
                   + "LEFT JOIN USER_COL_COMMENTS cc ON c.TABLE_NAME = cc.TABLE_NAME "
                   + "AND c.COLUMN_NAME = cc.COLUMN_NAME "
                   + "WHERE c.TABLE_NAME = ? ORDER BY c.COLUMN_ID";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tableName.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ColumnInfo col = new ColumnInfo(
                    rs.getInt("COLUMN_ID"),
                    rs.getString("COLUMN_NAME"),
                    rs.getString("COMMENTS"),
                    translateDataType(rs.getString("DATA_TYPE")),
                    rs.getInt("DATA_LENGTH"),
                    primaryKeys.contains(rs.getString("COLUMN_NAME")),
                    rs.getString("DATA_DEFAULT"),
                    rs.getString("NULLABLE").equals("Y") ? "是" : "否"
                );
                columns.add(col);
            }
        }
        return columns;
    }

    private static Set<String> getPrimaryKeys(Connection conn, String tableName) throws SQLException {
        Set<String> pkColumns = new HashSet<>();
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getPrimaryKeys(null, null, tableName.toUpperCase())) {
            while (rs.next()) {
                pkColumns.add(rs.getString("COLUMN_NAME"));
            }
        }
        return pkColumns;
    }

    private static String translateDataType(String oracleType) {
        switch (oracleType) {
            case "VARCHAR2": return "字符串";
            case "NUMBER": return "数字";
            case "DATE": return "日期";
            case "CLOB": return "长文本";
            case "BLOB": return "二进制";
            case "TIMESTAMP": return "时间戳";
            default: return oracleType;
        }
    }

    private static void exportToExcel(List<ColumnInfo> columns, String tableName) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(tableName);

            // 创建标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 创建数据样式
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setWrapText(true);

            // 创建标题行
            String[] headers = {"序号", "字段名称", "字段注释", "字段类型", "字段长度", 
                              "主键", "默认值", "允许为空"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 填充数据
            int rowNum = 1;
            for (ColumnInfo col : columns) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, col.seq, dataStyle);
                createCell(row, 1, col.columnName, dataStyle);
                createCell(row, 2, col.comments, dataStyle);
                createCell(row, 3, col.dataType, dataStyle);
                createCell(row, 4, col.dataLength, dataStyle);
                createCell(row, 5, col.isPrimaryKey ? "是" : "", dataStyle);
                createCell(row, 6, col.defaultValue, dataStyle);
                createCell(row, 7, col.nullable, dataStyle);
            }

            // 设置列宽
            int[] columnWidths = {8, 20, 30, 15, 12, 8, 20, 10};
            for (int i = 0; i < columnWidths.length; i++) {
                sheet.setColumnWidth(i, columnWidths[i] * 256);
            }

            // 设置打印区域
            sheet.setAutobreaks(true);
            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setLandscape(true);
            sheet.setFitToPage(true);

            // 保存文件
            try (FileOutputStream out = new FileOutputStream("E:\\001整理\\002项目\\008安徽\\002025\\数据库设计文档.xlsx")) {
                workbook.write(out);
            }
        }
    }

    private static void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }
}