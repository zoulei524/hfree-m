package me.zoulei.dbc.ui.components.orthers;

import javax.swing.*;

import lombok.Data;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.dbc.ui.components.center.ExecCP;

/**
 * 2023年12月1日14:57:05 zoulei
 * 执行比对数据库时的进度条
 */
public class Progress {

	public SimulatorActivity simulaterActivity;
	
	public Progress() {
		this.init();
	}
	
    //任务线程
	@Data
    public static class SimulatorActivity {
        //进度
        private volatile int current = 0;
        private volatile String note = "";
        private Timer timer;
        ExecCP taskThread;
        public SimulatorActivity() {
        }
        public void setStatus(int current,String note) {
        	this.current = current;
        	this.note = note;
        }


    }

    private Timer initTimer(ProgressMonitor monitor) {
        //设置定时任务
        return new Timer(100, e -> {
        	monitor.setNote(simulaterActivity.note);
            //读取当前任务量,修改进度
            int current = simulaterActivity.getCurrent();
            monitor.setProgress(current);
            //判断用户是否点击了取消按钮,停止定时任务,关闭对话框,退出程序
            if (monitor.isCanceled()) {
                monitor.close();
                simulaterActivity.timer.stop();
                
            }
            if (current == 100) {
            	//monitor.close();
            	simulaterActivity.timer.stop();
            }
        });
    }

    public void init() {
        //创建view
        ProgressMonitor monitor = new ProgressMonitor(MainPanel.mainFrame, "", "已完成", 0, 100);
       
        //创建任务model
        this.simulaterActivity = new SimulatorActivity();
        
        //开启监听controller
        Timer timer = initTimer(monitor);
        timer.start();
        this.simulaterActivity.setTimer(timer);
        
    }
    
   

    public static void main(String[] args) {
        new Progress();
    }
}