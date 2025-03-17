package me.zoulei;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import me.zoulei.ui.components.DataSourceComponent;
import me.zoulei.ui.components.south.FlowComponentCenter;
import me.zoulei.ui.menu.MainMenuBar;

/**
 * 2023年9月12日14:59:17 zoulei
 * ui启动
 */
public class MainApp {
	
	public static JFrame mainFrame;
	//边框颜色
	public static Border lineBorder = new LineBorder(Color.gray,0);
	//public static Border debugbBorder = new LineBorder(Color.red,2);
	public static Border debugbBorder = null;
	public static JPanel north;
	public static JPanel south;
	public static int MAIN_WIDTH = 1720;
	public static int MAIN_HEIGHT = 545;
	
	public static void main(String[] args) {
		new Config();
		
		
		mainFrame = new JFrame("odin7c开发辅助工具");
		mainFrame.setSize(MainApp.MAIN_WIDTH, MainApp.MAIN_HEIGHT);
		//mainFrame.setLocation(20, 20);
		mainFrame.setLocationRelativeTo(null);
        //setLayOut是设置布局的意思，当我们传入null，那后面新建的组件的位置就完全由我们所定义(即我想怎么摆就怎么摆），不受Frame约束。
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//上边布局 NORTH组件
		north = new JPanel(new BorderLayout());
		TitledBorder  blackline = BorderFactory.createTitledBorder("主表表格维护数据库配置");
		blackline.setTitleFont(new Font("黑体", 0, 16));
		north.setBorder(blackline);
		north.setPreferredSize(new Dimension(-1, 110));
		mainFrame.add(north,BorderLayout.NORTH);
		
        //数据库连接
        DataSourceComponent controlPanel = new DataSourceComponent();
        //数据库连接设置
        controlPanel.setBorder(MainApp.lineBorder);
	    north.add(controlPanel,BorderLayout.NORTH);
	    
	    
	    //流程副表配置2024年8月8日16:44:21
	    //addSouth();
	    //在SearchComponent.java中勾上流程配置显示
		
	    
	    //菜单
	    JMenuBar menuBar = new MainMenuBar();
	    
		mainFrame.setJMenuBar(menuBar);
	    
	    //logo
		java.net.URL imgUrl = MainApp.class.getResource("logo.png");
		ImageIcon imageIcon= new ImageIcon(imgUrl);
		mainFrame.setIconImage(imageIcon.getImage());
		
		
        mainFrame.setVisible(true);
	}
	
	public static void addSouth() {
		//流程副表配置2024年8月8日16:44:21
	    south = new JPanel(new BorderLayout());
	    mainFrame.add(south,BorderLayout.SOUTH);
		TitledBorder  sblackline = BorderFactory.createTitledBorder("流程项配置（未开发）");
		sblackline.setTitleFont(new Font("黑体", 0, 16));
		if(MainApp.debugbBorder!=null) {
			south.setBorder(MainApp.debugbBorder);
		}else {
			south.setBorder(sblackline);
		}
		south.setBackground(null);
		south.setPreferredSize(new Dimension(-1, 475));
	}

	public static JFrame getMainFrame() {
		return mainFrame;
	}

	
	
}
