package me.zoulei.dbc.ui.components.orthers;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/***
 *2023年12月1日14:57:43
 *@author zoulei
 *版本提示
 */
public  class VersionDialog extends JDialog implements ActionListener  {
    private static final long serialVersionUID = 1L;
    
    public static void main(String[] args) {
        Map<String,String> caipinMap = new LinkedHashMap<String,String>();
        caipinMap.put("版本","测试1.0.0");
        caipinMap.put("日期","2023年12月1日");
        caipinMap.put("作者","邹磊");
        caipinMap.put("联系方式","钉钉");        
        
        JFrame alertFrame = new JFrame();
        String desc = "数据库表反向生成odin7c的前后端代码。";
        VersionDialog d =new VersionDialog(alertFrame, true,caipinMap,360,320,desc);
        d.setVisible(true);
    }
    
    JButton okBtn = new JButton("确定");
    JButton cancelBtn = new JButton("关闭");
   
    int x = 10;
    int y = 30;
   
    /***
     * 自定义 Dialog
     * @param parent
     *             父Frame
     * @param modal
     *             是否模式窗体
     * @param caipinMap
     *             数据Map
     * @param windowWidth
     *             宽度 需根据数据计算高度
     * @param windowHeight
     *             高度  默认320即可
     */
    public VersionDialog(JFrame parent, boolean modal,Map<String,String> caipinMap,int windowWidth,int windowHeight,String desc) {
       super(parent,modal);
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸 
       int screenWidth = screenSize.width;
       int screenHeight = screenSize.height;
       setTitle("关于");
       setSize(windowWidth,windowHeight);
       setLayout(null);
       setResizable(false);
//       this.setUndecorated(true);
//       setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
       setLocation((screenWidth - this.getWidth()) / 2, (screenHeight - this.getHeight())/2);
       
       JLabel descLbl = new JLabel(desc);
       descLbl.setHorizontalAlignment(JLabel.LEFT);
       add(descLbl);
       descLbl.setBounds(x+70,15,300,25);
       y = y + 30;
       for (String strKey : caipinMap.keySet()) {
            JLabel cpNameLBL = new JLabel(strKey+":");
            add(cpNameLBL);
            cpNameLBL.setHorizontalAlignment(JLabel.RIGHT);
            cpNameLBL.setBounds(x,y,100,25);
            cpNameLBL.setName("lbl"+strKey);
            
            if("日志".equals(strKey)) {
            	
            	y = y + 30;
            }else {
            	JLabel noticeLbl = new JLabel(caipinMap.get(strKey));
                noticeLbl.setHorizontalAlignment(JLabel.LEFT);
                add(noticeLbl);
                noticeLbl.setName("lbl"+strKey);
                noticeLbl.setBounds(x+120,y,100,25);
                y = y + 30;
            }
            
        }
       
        add(okBtn);
        //add(cancelBtn);
        okBtn.setBounds(windowWidth - 212, windowHeight - 80, 60, 25);
        okBtn.setName("ok");
        //cancelBtn.setBounds(windowWidth - 120, windowHeight - 80, 60, 25);
        //cancelBtn.setName("cancel");
        okBtn.addActionListener(this);
        //cancelBtn.addActionListener(this);
   }

   public void actionPerformed(ActionEvent e) {
     
       if(e.getSource()==okBtn){
           System.out.println("OK");
           dispose();
           return;   
       }
//       if(e.getSource()==cancelBtn){
//           System.out.println("cancel");
//           dispose();
//           return;   
//       }
   }
 
}