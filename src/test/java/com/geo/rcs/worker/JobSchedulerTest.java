/*

package com.geo.rcs.worker;


import com.geo.rcs.GeotmtApplicationTests;

import com.geo.rcs.modules.jobs.entity.JobScheduler;
import com.geo.rcs.modules.jobs.handler.JobCliHandler;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.rabbitmq.constant.MqConstant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

*/
/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 17:46 2018/8/10
 *//*

public class JobSchedulerTest extends GeotmtApplicationTests {


    @Autowired
    private JobRegisterService jobRegisterService;
    @Autowired
    private JobCliHandler jobCliHandler;

    @Test
    public void testJobCli() throws InterruptedException {

        JobScheduler worker2 = new JobScheduler();
        worker2.initSchedulerInfo("127.0.0.1");
        jobRegisterService.register(worker2);
        int x = 5;
        new Thread("jobCli") {

            worker2 =(JobScheduler)jobRegisterService.keepAlive(worker2);

            displayWorker(worker2);


        }.start();
    }




    */
/**
     * 控制台输出心跳JobWorker
     *
     * @param register
     *//*

    public void displayWorker(JobScheduler register) {
        String msgDemo = "[SCHEDULER]  {0} {1} {2} {3} {4}";
        int waitStatuse = MqConstant.WorkStatus.WAIT.getCode();
        int busyStatuse = MqConstant.WorkStatus.BUSY.getCode();
        int deadStatus = MqConstant.WorkStatus.WAIT.getCode();
        String msg = MessageFormat.format(msgDemo,
                register.getType(),
                register.getId(),
                register.getIp(),
                register.getRegistTimeString(),
                register.getUpdateTimeString()
        );

        System.out.println(ANSI_GREEN + msg + ANSI_RESET);


        System.out.println();
        System.out.println();
        System.out.println();
    }

}
*/
