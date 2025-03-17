package me.zoulei.wkds.ui.components;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import me.zoulei.Config;
import me.zoulei.MainApp;
import me.zoulei.wkds.ui.components.north.NorthUI;

/**
 * 
 * @ClassName: WDPanel
 * @Desc: TODO
 * @author zoulei
 * @date 2024年7月30日 下午4:49:14
 */
public class WDPanel {

	
	public static JFrame mainFrame = null;

	public static void main(String[] args) {
		if(args!=null&&args.length==1) {
			
		}else {
			new Config();
		}
		mainFrame = new JFrame("测试开发中...");
		mainFrame.setSize(720, 350);
		//mainFrame.setLocation(20, 20);
		mainFrame.setLocationRelativeTo(null);
        //setLayOut是设置布局的意思，当我们传入null，那后面新建的组件的位置就完全由我们所定义(即我想怎么摆就怎么摆），不受Frame约束。
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//加入结果日志面板
		//ResultsLogUI resultsLog = new ResultsLogUI();
       
		
		//主面板BorderLayout 北面ui
		//数据库连接放在上方 上方位置
		NorthUI northUI = new NorthUI();
		
		
		mainFrame.add(northUI,BorderLayout.NORTH);
		//mainFrame.add(resultsLog,BorderLayout.CENTER);
		
       
        
		//logo
		java.net.URL imgUrl = MainApp.class.getResource("logo.png");
		ImageIcon imageIcon= new ImageIcon(imgUrl);
		mainFrame.setIconImage(imageIcon.getImage());
		
        mainFrame.setVisible(true);
        
        

	}

}
