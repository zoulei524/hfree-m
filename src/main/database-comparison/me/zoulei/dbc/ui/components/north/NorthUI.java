package me.zoulei.dbc.ui.components.north;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import me.zoulei.MainApp;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.dbc.ui.components.center.ExecCP;
import me.zoulei.dbc.ui.components.center.ResultsLogUI;
import me.zoulei.dbc.ui.components.orthers.VersionDialog;

/**
 * 2023年11月27日10:27:49
 * @author zoulei
 * 主面板BorderLayout 北面ui
 */
public class NorthUI extends JPanel{
	
	private static final long serialVersionUID = -8056442308975048131L;

	/**
	 * 2个数据库连接配置，用于比对
	 * 连接后展示选择数据库模式的下拉框控件
	 * @param resultsLog  比对后放日志的三个文本域
	 */
	public NorthUI(ResultsLogUI resultsLog) {
		//数据库连接放在上方 上方位置
		this.setLayout(new FlowLayout());
		
		//添加标题边框
		TitledBorder  blackline = BorderFactory.createTitledBorder("数据库配置");
		blackline.setTitleFont(new Font("黑体", Font.PLAIN,18));
		blackline.setBorder(new LineBorder(new Color(184, 207, 229),2));
		this.setBorder(blackline);
		//设置高度
		this.setPreferredSize(new Dimension(-1, 160));
		//*****************************************************************/
		
		
        
        //第1个连接之后显示模式选择的面板
        JPanel schemaSelectPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        schemaSelectPanel1.setPreferredSize(new Dimension(700, 45));
        //第2个连接之后显示模式选择的面板
        JPanel schemaSelectPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        schemaSelectPanel2.setPreferredSize(new Dimension(600, 45));
        
        //数据库配置的界面
        DataConnComponent dcc = new DataConnComponent(schemaSelectPanel1,"选择第1个的数据库模式：");
        //第1个数据库连接
        this.add(dcc);
        //第2个数据库连接
        DataConnComponent dcc2 = new DataConnComponent(schemaSelectPanel2,"选择第2个的数据库模式：");
        this.add(dcc2);
        dcc2.other=dcc;
        dcc.other=dcc2;
        
        
        //第一个连接之后显示模式选择的面板
        this.add(schemaSelectPanel1);
        //第二个连接之后显示模式选择的面板
        this.add(schemaSelectPanel2);
        //第三个放按钮
        JButton execButton = new JButton("执行表结构比对");
        this.add(execButton);
        execButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 ExecCP execCP = new ExecCP(dcc.schemaSelectComponent,dcc2.schemaSelectComponent,resultsLog);
	        	 execCP.start();
	         }
        });
        //放一个版本信息的按钮
        JPanel jPanel = new JPanel();
        //jPanel.setPreferredSize(new Dimension(100, 40));
        JButton about = new JButton("关于");
        jPanel.add(about);
        this.add(jPanel);
        about.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 Map<String,String> caipinMap = new LinkedHashMap<String,String>();
	        	 caipinMap.put("版本","测试1.0.0");
	        	 caipinMap.put("日期","2023年12月1日");
	        	 caipinMap.put("作者","邹磊");
	        	 caipinMap.put("wx","18042307016");        
	        	 String desc = "数据库表结构比对并生成相关日志。";
	        	 VersionDialog d =new VersionDialog(MainPanel.mainFrame, true,caipinMap,360,320,desc);
	        	 d.setVisible(true);
	         }
        });
        
        
        JButton configButton = new JButton("选择表");
        this.add(configButton);
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					JDialog tableSelectUI = new TableSelectUI(dcc.schemaSelectComponent,dcc2.schemaSelectComponent);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainPanel.mainFrame, "设置失败："+e1.getMessage());
					e1.printStackTrace();
					
				}
            }
        });
	}

}
