package me.zoulei.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import me.zoulei.MainApp;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.dbc.ui.components.orthers.VersionDialog;
import me.zoulei.ui.toolSamples.DocumentTool;

public class MainMenuBar extends JMenuBar{
	private static final long serialVersionUID = 4986776746050350761L;

	public MainMenuBar() {
		//菜单
	    JMenu view = new JMenu("查看");
		this.add(view);
		JMenuItem showstatus = new JMenuItem("一些常用代码");
		showstatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DocumentTool("123");
			}
		});
		view.add(showstatus);
		
		
		JMenuItem dc = new JMenuItem("数据库表结构比对");
		view.add(dc);
		dc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.main(new String[] {"1"});
			}
		});
		
		
		
		JMenuItem about = new JMenuItem("关于");
		view.add(about);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String,String> caipinMap = new LinkedHashMap<String,String>();
		        //caipinMap.put("版本","测试1.0.0");
		        //caipinMap.put("日期","2023年12月1日");
		        caipinMap.put("版本","测试1.0.1");
		        caipinMap.put("日期","2024年6月27日");
		        caipinMap.put("日志","2024年5月10日 Excel导出加代码转换和时间转换，实体类达梦库number为bigdecimal类型,integer转为long类型。\n"
		        		+ "2024年5月10日 分页查询代码和部分代码初始化提到公共类中\n"
		        		+ "2024年5月 输出文件增加异常处理类\n"
		        		+ "2024年5月22日 输出文件实体类添加备注信息\n"
		        		+ "2024年5月22日 输出文件增加常量类\n"
		        		+ "2024年6月7日 实体类加计算机名和用户信息\n"
		        		+ "2024年6月12日 枚举和常量生成的报错\n"
		        		+ "2024年6月13日 js分页参数名字更改\n"
		        		+ "2024年6月20日 实体类保存，前端对象转实体类使用hybean\n"
		        		+ "2024年6月27日 没有选择分页代码注释掉\n"
		        		+ "2024年6月27日 接口名称生成，从包名中获取\n");
		        
		        caipinMap.put("作者","邹磊");
		        caipinMap.put("联系方式","18042307016");        
		        String desc = "数据库表反向生成odin7c的前后端代码。";
		        VersionDialog d =new VersionDialog(MainApp.getMainFrame(), true,caipinMap,360,320,desc);
		        d.setVisible(true);
			}
		});
	}

}
