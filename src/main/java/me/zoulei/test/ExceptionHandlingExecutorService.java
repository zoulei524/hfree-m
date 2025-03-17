package me.zoulei.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ExceptionHandlingExecutorService {
    private final ExecutorService executorService;
 
    public ExceptionHandlingExecutorService(int nThreads) {
        ThreadFactory threadFactory = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println("Thread " + t.getName() + " threw an Exception: " + e);
                        executorService.shutdownNow();
                    }
                });
                return t;
            }
        };
 
        executorService = Executors.newFixedThreadPool(nThreads, threadFactory);
    }
 
    public void execute(Runnable command) {
        executorService.execute(command);
    }
    public void submit(Runnable command) {
    	executorService.submit(command);
    }
 
    public void shutdown() {
        executorService.shutdown();
    }
 
    public static void main(String[] args) {
        ExceptionHandlingExecutorService executorService = new ExceptionHandlingExecutorService(5);
        for (int i = 0; i < 50; i++) {
            final int taskId = i;
            executorService.execute(() -> {
                if (taskId == 5) {
                    throw new RuntimeException("Task " + taskId + " failed!");
                }
                System.out.println("Executing task " + taskId);
            });
        }
        executorService.shutdown();
    }

	public void awaitTermination(int i, TimeUnit seconds) throws InterruptedException {
		executorService.awaitTermination(i, seconds);
		
	}

	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	public void shutdownNow() {
		executorService.shutdownNow();
		
	}
}