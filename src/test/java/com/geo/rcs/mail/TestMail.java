/*
package com.geo.rcs.mail;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.GeotmtApplicationTests;
import com.geo.rcs.common.jedis.RabbitmqConfig;
import com.geo.rcs.common.util.mail.MailService;
import com.geo.rcs.modules.rabbitmq.message.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

*/
/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:25 2018/7/25
 *//*

public class TestMail extends GeotmtApplicationTests {

    @Autowired
    private MailService mailService;
    private static final String TASK_QUEUE_NAME1 = "Mail-high";

    @Autowired
    private RabbitmqConfig rabbitmqConfig;

    @Test
    public void sendMail() {
        Map<String, Object> parmMap = new HashedMap();
        //收件人
        String[] to = {"13720071850@163.com"};
        //抄送人
        String[] cc = {"m15711312374@163.com"};
        //密送人
        String[] bcc = {"qiaoshenglong@geotmt.com"};
        parmMap.put("to", to);
        parmMap.put("bcc", bcc);
        parmMap.put("cc", cc);
        parmMap.put("subject", "测试主题");
        parmMap.put("content", "测试内容");
        try {
            mailService.sendMail("text", parmMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendMailByQueue() throws Exception {

        Message message = null;

        Channel channel = rabbitmqConfig.getChannel();

        Map<String, Object> parmMap = new HashMap<>();
        // 指定队列持久化
        channel.queueDeclare(TASK_QUEUE_NAME1, true, false, false, null);

        HashMap<String, String> hashMap = new HashMap<>();
        HashMap<String, Object> parms = new HashMap<>();
        //收件人
        String[] to = {"13720071850@163.com"};
        //抄送人
        String[] cc = {"m15711312374@163.com"};
        //密送人
        String[] bcc = {"qiaoshenglong@geotmt.com"};
        for (long i = 4; i < 5; i++) {
            if (i == 2) {
                hashMap.put("name", "小明");
            } else {
                hashMap.put("name", "小红");

            }
            parms.put("to", to);
            parms.put("cc", cc);
            parms.put("bcc", bcc);
            parms.put("content", "测试内容");

            if (i == 0) {
                //文本邮件
                parms.put("subject", "集智雷达系统邮件");
                hashMap.put("type", "text");
            } else if (i == 1) {
                //html邮件
                parms.put("subject", "html邮件");
                hashMap.put("type", "html");

            } else if (i == 2) {

                //附件邮件
                parms.put("subject", "附件邮件");
                parms.put("filePath", "E:\\tmp\\high_tatras2.jpg");
                hashMap.put("type", "attachment");
            } else if (i == 3) {

                //静态邮件
                parms.put("subject", "静态邮件");
                parms.put("imgPath", "E:\\tmp\\high_tatras2.jpg");
                hashMap.put("type", "static");
            } else if (i == 4) {

                //模板邮件 fileName:邮件模板文件名,parmName:模板文件参数名,value:模板文件参数值
                parms.put("subject", "模板邮件");
                parms.put("fileName", "emailTemplate");
                parms.put("parmName", "id");
                parms.put("value", "2");
                hashMap.put("type", "template");
            }

            String s = JSON.toJSONString(parms);
            hashMap.put("parms", s);
            message = new Message(i, hashMap);
            String beanToJson = JSON.toJSONString(message);
            // 指定消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME1, MessageProperties.PERSISTENT_TEXT_PLAIN, beanToJson.getBytes());
            System.out.println(" [x] Sent '" + beanToJson + "'");
        }
        long messageCount = channel.messageCount(TASK_QUEUE_NAME1);
        System.out.println("队列大小:" + messageCount);
        channel.close();
    }
}
*/
