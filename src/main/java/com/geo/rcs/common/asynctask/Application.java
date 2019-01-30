package com.geo.rcs.common.asynctask;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.asynctask
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 上午11:22
 */

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.task.TaskRejectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Application {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //testVoid();

        testReturn();
    }

    // 测试无返回结果
    private static void testVoid() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

        // 创建了20个线程
        for (int i = 1; i <= 20; i++) {
            asyncTaskService.executeAsyncTask(i);
        }

        context.close();
    }

    // 测试有返回结果
    private static void testReturn() throws InterruptedException, ExecutionException {
        long currentTimeMillis = System.currentTimeMillis();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

        List<Future<String>> lstFuture = new ArrayList<Future<String>>();// 存放所有的线程，用于获取结果

        // 创建100个线程
        for (int i = 1; i <= 20; i++) {
            while (true) {
                try {
                    // 线程池超过最大线程数时，会抛出TaskRejectedException，则等待1s，直到不抛出异常为止
                    Future<String> future = asyncTaskService.asyncInvokeReturnFuture(i);
                    lstFuture.add(future);

                    break;
                } catch (TaskRejectedException e) {
                    System.out.println("线程池满，等待1S。");
                    Thread.sleep(1000);
                }
            }
        }

        // 获取值。get是阻塞式，等待当前线程完成才返回值
        for (Future<String> future : lstFuture) {
            System.out.println(future.get());
        }
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("任务总耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
        context.close();
    }
}

