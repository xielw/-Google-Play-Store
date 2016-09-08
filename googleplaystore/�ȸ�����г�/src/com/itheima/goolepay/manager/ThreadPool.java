package com.itheima.goolepay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class ThreadPool {

	private static ThreadPool mThreadPool;
	private static int corePoolSize = 10;
	private static int maximumPoolSize = 10;
	private static long keepAliveTime = 1l;
	private ThreadPoolExecutor executor;
	
	public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		
		ThreadPool.corePoolSize = corePoolSize;
		ThreadPool.maximumPoolSize = maximumPoolSize;
		ThreadPool.keepAliveTime = keepAliveTime;
	}

	public static ThreadPool getThreadPool(){
		if(mThreadPool == null){
			synchronized (ThreadPool.class) {
				if(mThreadPool == null){
					mThreadPool = new ThreadPool(corePoolSize,maximumPoolSize,keepAliveTime);
				}
			}
		}
		
		return mThreadPool;
		
	}
	
	public void execute(Runnable r){
		
		executor = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				Executors.defaultThreadFactory(), new AbortPolicy());
		executor.execute(r);
	}
	
	
	//从线程池移除线程
	public void cancle(Runnable r){
		if(executor != null)
		executor.getQueue().remove(r);
	}
	
	

}
