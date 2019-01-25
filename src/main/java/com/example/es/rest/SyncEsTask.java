package com.example.es.rest;

import com.example.es.utils.EsRestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncEsTask implements Runnable {

    @Autowired
    private EsRestUtils esUtils;

    @Override
    public void run() {
        boolean isNext = true;
        while (isNext){
            //开始时间
            long esStartTime = System.currentTimeMillis();
//            esUtils.pushproduct();
            //结束时间
            long esEndTime = System.currentTimeMillis();
            System.out.println("ES推送单次时间------------------------->"+(esEndTime-esStartTime)+"s");
        }
    }
}
