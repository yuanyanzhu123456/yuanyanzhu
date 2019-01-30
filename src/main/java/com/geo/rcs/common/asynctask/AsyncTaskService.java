package com.geo.rcs.common.asynctask;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.asynctask
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 上午11:19
 */

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Future;

@Service
// 线程执行任务类
public class AsyncTaskService {

    Random random = new Random();// 默认构造方法

    @Async
    // 表明是异步方法
    // 无返回值
    public void executeAsyncTask(Integer i) {
        System.out.println("执行异步任务：" + i);
    }

    /**
     * 异常调用返回Future
     *
     * @param i
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<String> asyncInvokeReturnFuture(int i) throws InterruptedException {
        System.out.println("input is " + i);
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(1000 * random.nextInt(i));
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println(i+"任务耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
        Future<String> future = new AsyncResult<String>("success:" + i);// Future接收返回值，这里是String类型，可以指明其他类型

        return future;
    }
}
