package me.zoulei.expDesign;
import java.io.*;
import java.sql.*;
import java.util.*;

import lombok.Data;

public class DatabaseDocGenerator {

	@Data
    static class TableInfo {
        private String tableName;
        private String tableComment;
        private List<ColumnInfo> columns = new ArrayList<>();

        // getters and setters
    }
	
	@Data
    static class ColumnInfo {
        private String columnName;
        private String comment;
        private String dataType;
        private int dataLength;
        private String precision;
        private String scale;
        private boolean primaryKey;
        private boolean nullable;
        private String defaultValue;

        // getters and setters
    }

    private static final String JDBC_URL = "jdbc:dm://127.0.0.1:5236";
    private static final String USER = "HY_ZGGL_GWY";
    private static final String PASSWORD = "HY_ZGGL_GWY";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            List<TableInfo> tables = getTables(conn);
            generateHtmlDocument(tables, "E:\\001整理\\002项目\\008安徽\\002025\\DatabaseDesign.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<TableInfo> getTables(Connection conn) throws SQLException {
        List<TableInfo> tables = new ArrayList<>();
        String schema = USER.toUpperCase();
        
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT table_name FROM all_tables WHERE owner = ? limit 10")) {
            pstmt.setString(1, schema);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                TableInfo table = new TableInfo();
                table.setTableName(tableName);
                table.setTableComment(getTableComment(conn, schema, tableName));
                table.setColumns(getColumns(conn, schema, tableName));
                tables.add(table);
            }
        }
        return tables;
    }

    private static String getTableComment(Connection conn, String schema, String tableName) throws SQLException {
        String sql = "SELECT comments FROM all_tab_comments WHERE owner = ? AND table_name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, schema);
            pstmt.setString(2, tableName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getString("comments") : "";
        }
    }

    private static List<ColumnInfo> getColumns(Connection conn, String schema, String tableName) throws SQLException {
        List<ColumnInfo> columns = new ArrayList<>();
        Set<String> primaryKeys = getPrimaryKeys(conn, schema, tableName);

        String sql = "SELECT c.column_name, c.data_type, c.data_length, "
                   + "c.data_precision, c.data_scale, c.nullable, c.data_default, "
                   + "cc.comments FROM all_tab_columns c "
                   + "LEFT JOIN all_col_comments cc ON c.owner = cc.owner "
                   + "AND c.table_name = cc.table_name AND c.column_name = cc.column_name "
                   + "WHERE c.owner = ? AND c.table_name = ? "
                   + "ORDER BY c.column_id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, schema);
            pstmt.setString(2, tableName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ColumnInfo col = new ColumnInfo();
                col.setColumnName(rs.getString("column_name"));
                col.setDataType(rs.getString("data_type"));
                col.setDataLength(rs.getInt("data_length"));
                col.setPrecision(rs.getString("data_precision"));
                col.setScale(rs.getString("data_scale"));
                col.setNullable("Y".equals(rs.getString("nullable")));
                col.setDefaultValue(rs.getString("data_default"));
                col.setComment(rs.getString("comments"));
                col.setPrimaryKey(primaryKeys.contains(col.getColumnName()));
                columns.add(col);
            }
        }
        return columns;
    }

    private static Set<String> getPrimaryKeys(Connection conn, String schema, String tableName) throws SQLException {
        Set<String> keys = new HashSet<>();
        String sql = "SELECT cc.column_name FROM all_constraints c "
                   + "JOIN all_cons_columns cc ON c.constraint_name = cc.constraint_name "
                   + "WHERE c.owner = ? AND c.table_name = ? AND c.constraint_type = 'P'";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, schema);
            pstmt.setString(2, tableName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                keys.add(rs.getString("column_name"));
            }
        }
        return keys;
    }

    private static void generateHtmlDocument(List<TableInfo> tables, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head>");
            writer.println("<meta charset='UTF-8'>");
            writer.println("<title>数据库设计文档</title>");
            writer.println("<style>");
            writer.println("table {border-collapse: collapse; width: 100%; margin-bottom: 25px;}");
            writer.println("th {background: #4CAF50; color: white; padding: 12px; text-align: left;}");
            writer.println("td {padding: 10px; border: 1px solid #ddd;}");
            writer.println("tr:nth-child(even) {background: #f9f9f9;}");
            writer.println(".table-name {font-size: 18px; margin: 25px 0 10px; color: #333;}");
            writer.println(".table-comment {color: #666; margin-bottom: 15px;}");
            writer.println("</style></head><body>");
            writer.println("<h1 style='text-align: center; color: #333;'>数据库设计文档</h1>");

            for (TableInfo table : tables) {
                writer.println("<div class='table-name'>表名称: " + escapeHtml(table.getTableName()) + "</div>");
                if (!table.getTableComment().isEmpty()) {
                    writer.println("<div class='table-comment'>表注释: " + escapeHtml(table.getTableComment()) + "</div>");
                }
                
                writer.println("<table>");
                writer.println("<tr>");
                writer.println("<th>序号</th><th>字段名</th><th>注释</th><th>类型</th>");
                writer.println("<th>精度/标度</th><th>主键</th><th>默认值</th><th>允许空</th>");
                writer.println("</tr>");

                int index = 1;
                for (ColumnInfo col : table.getColumns()) {
                    writer.println("<tr>");
                    writer.println("<td>" + index++ + "</td>");
                    writer.println("<td>" + escapeHtml(col.getColumnName()) + "</td>");
                    writer.println("<td>" + escapeHtml(col.getComment()) + "</td>");
                    writer.println("<td>" + escapeHtml(col.getDataType()) + "</td>");
                    
                    String precisionScale = "";
                    if (col.getPrecision() != null) {
                        precisionScale += col.getPrecision();
                        if (col.getScale() != null && !"0".equals(col.getScale()) ) {
                            precisionScale += ", " + col.getScale();
                        }
                    } else if (col.getDataLength() > 0) {
                        precisionScale = String.valueOf(col.getDataLength());
                    }
                    writer.println("<td>" + precisionScale + "</td>");
                    
                    writer.println("<td>" + (col.isPrimaryKey() ? "√" : "") + "</td>");
                    writer.println("<td>" + escapeHtml(col.getDefaultValue()) + "</td>");
                    writer.println("<td>" + (col.isNullable() ? "是" : "否") + "</td>");
                    writer.println("</tr>");
                }
                writer.println("</table>");
            }
            writer.println("</body></html>");
        }
    }

    private static String escapeHtml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                 .replace("<", "&lt;")
                 .replace(">", "&gt;")
                 .replace("\"", "&quot;")
                 .replace("'", "&#39;");
    }
}