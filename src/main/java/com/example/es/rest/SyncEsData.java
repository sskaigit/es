package com.example.es.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class SyncEsData {
    private static final int MAXNUMBER=1;
    private static final int SLEEPTIME=100;

    @Autowired
    private SyncEsTask syncEsTask;

    public void init(){
        //创建线程池
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,8,5, TimeUnit.HOURS,new SynchronousQueue<Runnable>());

        for (int i=0;i<MAXNUMBER ;i++){
            try {
                //产生一个任务,并加入到线程池
                String task = "task@ "+i;
                System.out.println("put "+task);

                threadPool.execute(syncEsTask);

                //便于观察,等待一段时间
                Thread.sleep(SLEEPTIME);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
