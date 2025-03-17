package me.zoulei.wkds.ui.components.north;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import me.zoulei.ui.components.codeEditor.CodeDocument;
import me.zoulei.wkds.ui.components.north.createSQL.GenCSQL;


public class NorthUI extends JPanel{
	
	private static final long serialVersionUID = -8056442308975048131L;

	
	public NorthUI() {
		//数据库连接放在上方 上方位置
		this.setLayout(new FlowLayout());
		
		//添加标题边框
		TitledBorder  blackline = BorderFactory.createTitledBorder("区域一");
		blackline.setTitleFont(new Font("黑体", Font.PLAIN,18));
		blackline.setBorder(new LineBorder(new Color(184, 207, 229),2));
		this.setBorder(blackline);
		//设置高度
		this.setPreferredSize(new Dimension(-1, 160));
		//*****************************************************************/
		
		
        
        //第1个连接之后显示模式选择的面板
        JPanel schemaSelectPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel  namelabel= new JLabel("表名: ", JLabel.RIGHT);
		JTextField textField = new JTextField("",6);
		schemaSelectPanel1.add(namelabel);
		schemaSelectPanel1.add(textField);
		JLabel  namelabel2= new JLabel("备注: ", JLabel.RIGHT);
		JTextField textField2 = new JTextField("",22);
		schemaSelectPanel1.add(namelabel2);
		schemaSelectPanel1.add(textField2);
        
        //第一个连接之后显示模式选择的面板
        this.add(schemaSelectPanel1);
        
        
      //第2个连接之后显示模式选择的面板
        JPanel schemaSelectPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel  namelabel3= new JLabel("表名: ", JLabel.RIGHT);
		JTextField textField3 = new JTextField("",6);
		schemaSelectPanel2.add(namelabel3);
		schemaSelectPanel2.add(textField3);
		JLabel  namelabel4= new JLabel("备注: ", JLabel.RIGHT);
		JTextField textField4 = new JTextField("",22);
		schemaSelectPanel2.add(namelabel4);
		schemaSelectPanel2.add(textField4);
        
        //第2个连接之后显示模式选择的面板
        this.add(schemaSelectPanel2);
        
        
        //第三个放按钮
        JPanel schemaSelectPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT,200,0));
        schemaSelectPanel3.setBounds(1, 1, 100, 200);
        JButton execButton = new JButton("执行");
        schemaSelectPanel3.add(execButton);
        this.add(schemaSelectPanel3);
        execButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 try {
	        		 HashMap<String, Object> params = new HashMap<String, Object>();
	        		 params.put("tablename", textField.getText().toUpperCase());
	        		 params.put("comment", textField2.getText());
	        		 params.put("tablename2", textField3.getText().toUpperCase());
	        		 params.put("comment2", textField4.getText());
	        		 GenCSQL genCSQL = new GenCSQL(params);
	        		 Map<String,String[]> codemap = new LinkedHashMap<String, String[]>();
		        	 codemap.put("    SQL     ", new String[] {genCSQL.getCode(), SyntaxConstants.SYNTAX_STYLE_SQL});
		        	 new CodeDocument(codemap);
	        	 } catch (Exception e1) {
	        		 e1.printStackTrace();
	        	 }
	        	 
	         }
        });
        
	}

}
