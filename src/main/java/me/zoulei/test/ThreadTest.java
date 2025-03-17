package me.zoulei.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

	public ThreadTest() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void main(String[] args) {
		int threadCount = Runtime.getRuntime().availableProcessors();
		System.out.println("CPU线程数：" + threadCount);
		// 创建一个固定大小为3的线程池
		
		ExceptionHandlingExecutorService executorService = new ExceptionHandlingExecutorService(threadCount);
        for (int i = 0; i < 50; i++) {
            final int taskId = i;
            executorService.execute(() -> {
            	 System.out.println("Executing task " + taskId);
            	 if (taskId == 5) {
                     //throw new RuntimeException("Task " + taskId + " failed!");
                 }
            	// 模拟任务执行时间
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
               
                
            });
        }
		
 
        // 关闭线程池，并等待任务完成
        try {
            executorService.shutdown();
            executorService.awaitTermination(100, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (!executorService.isTerminated()) {
                System.out.println("Shutting down now");
                executorService.shutdownNow();
            }
        }
 
        System.out.println("All tasks completed");
        System.out.println(111);
	}

}
