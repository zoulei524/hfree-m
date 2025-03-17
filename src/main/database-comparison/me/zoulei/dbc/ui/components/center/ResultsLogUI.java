package me.zoulei.dbc.ui.components.center;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * 2023年11月24日17:00:18 zoulei
 * 放结果日志的文本域
 */
public class ResultsLogUI extends JPanel{
	
	private static final long serialVersionUID = -7587632831245041902L;

	/**
	 * 日志：
	 */
	public RSyntaxTextArea textAreaLog;

	/**
	 * n库1中多的表和字段
	 */
	public RSyntaxTextArea textAreaDDL2;

	/**
	 * 库2中多的表和字段
	 */
	public RSyntaxTextArea textAreaDDL1;
	
	/**
	 * 滚动条是否继续向下滚动
	 */
	public boolean isDone=false;
	
	public ResultsLogUI() {
		//this.setBackground(Color.red);
//		TitledBorder  blackline = BorderFactory.createTitledBorder("");
//		blackline.setTitleFont(new Font("黑体", Font.PLAIN,18));
//		blackline.setBorder(new LineBorder(new Color(184, 207, 229),2));
//		this.setBorder(blackline);
		
		GridBagConstraints constraints=new GridBagConstraints();
		GridBagLayout gbaglayout=new GridBagLayout(); 
		this.setLayout(gbaglayout);
		//放日志的文本域
		this.textAreaLog = new RSyntaxTextArea("/*\n日志：\n*/\n");
		//textAreaLog.setTextMode(RTextArea.INSERT_MODE);
		this.textAreaLog.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
		RTextScrollPane pLog = new RTextScrollPane(textAreaLog);
		constraints.fill = GridBagConstraints.BOTH;    //组件填充显示区域
        //constraints.weightx=0.0;    //恢复默认值
        //constraints.gridwidth = GridBagConstraints.REMAINDER;    //结束行
		constraints.weightx=0.1;    // 指定组件的分配区域
        constraints.weighty=1;
        constraints.gridwidth=1;
        gbaglayout.setConstraints(pLog, constraints);
		this.add(pLog);
		
		//放库1的文本域
		this.textAreaDDL1 = new RSyntaxTextArea("/*\n库1中多的表和字段：\n*/\n");
		//textAreaDDL1.setTextMode(RTextArea.INSERT_MODE);
		this.textAreaDDL1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		RTextScrollPane pDDL1 = new RTextScrollPane(textAreaDDL1);
		
        gbaglayout.setConstraints(pDDL1, constraints);
		this.add(pDDL1);
		
		
		//放库2的文本域
		this.textAreaDDL2 = new RSyntaxTextArea("/*\n库2中多的表和字段：\n*/\n");
		//textAreaDDL2.setTextMode(RTextArea.INSERT_MODE);
		this.textAreaDDL2.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		RTextScrollPane pDDL2 = new RTextScrollPane(textAreaDDL2);
		gbaglayout.setConstraints(pDDL2, constraints);
		this.add(pDDL2);
		
		//滚动条
		pLog.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
            	if(!isDone) {
            		 e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            	}
            }
        });
		pDDL1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
            	if(!isDone) {
            		e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            	}
                
            }
        });
		pDDL2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
            	if(!isDone) {
            		e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            	}
            }
        });
		
		//显示区域大小调整 2023年11月28日18:55:34
		addListener(pLog,pDDL1,pDDL2,gbaglayout,constraints);
		
	}
	
	
	
	//显示区域大小调整 2023年11月28日18:55:34
	boolean toggle1 = true;
	boolean toggle2 = true;
	boolean toggle3 = true;
	private void addListener(RTextScrollPane pLog, RTextScrollPane pDDL1, RTextScrollPane pDDL2, GridBagLayout gbaglayout, GridBagConstraints constraints) {
		textAreaLog.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==3){
					if(toggle1) {
						constraints.weightx=0.8;
						gbaglayout.setConstraints(pLog, constraints);
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pDDL1, constraints);
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pDDL2, constraints);
						revalidate();
						
					}else {
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pLog, constraints);
						gbaglayout.setConstraints(pDDL1, constraints);
						gbaglayout.setConstraints(pDDL2, constraints);
						revalidate();
					}
					toggle1 = !toggle1;
					toggle2 = true;
					toggle3 = true;
				}
	    	}
	    });
		textAreaDDL1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){//重写mouseClicked方法
				if(e.getClickCount()==3){
					if(toggle2) {
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pLog, constraints);
						constraints.weightx=0.8;
						gbaglayout.setConstraints(pDDL1, constraints);
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pDDL2, constraints);
						revalidate();
					}else{
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pLog, constraints);
						gbaglayout.setConstraints(pDDL1, constraints);
						gbaglayout.setConstraints(pDDL2, constraints);
						revalidate();
					}
					toggle1 = true;
					toggle2 = !toggle2;
					toggle3 = true;
				}
	    	}
	    });
		textAreaDDL2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){//重写mouseClicked方法
				if(e.getClickCount()==3){
					if(toggle3) {
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pLog, constraints);
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pDDL1, constraints);
						constraints.weightx=0.8;
						gbaglayout.setConstraints(pDDL2, constraints);
						revalidate();
					}else{
						constraints.weightx=0.1;
						gbaglayout.setConstraints(pLog, constraints);
						gbaglayout.setConstraints(pDDL1, constraints);
						gbaglayout.setConstraints(pDDL2, constraints);
						revalidate();
					}
					toggle1 = true;
					toggle2 = true;
					toggle3 = !toggle3;
				}
	    	}
	    });
		
	}
	
	
}
