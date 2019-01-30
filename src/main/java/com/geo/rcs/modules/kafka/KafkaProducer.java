package com.geo.rcs.modules.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Random;

/**
 * 这是一个简单的Kafka producer代码
 * 包含两个功能:
 * 1、数据发送
 * 2、数据按照自定义的partition策略进行发送
 * <p>
 * <p>
 * KafkaSpout的类
 */
/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:23 2018/11/7
 */
@Component
public class KafkaProducer {


    private Producer<String, String> producer;
    private Random random = new Random();
    @Value("${spring.kafka.zkSendIpPortList}")
    private String kafkaIPPort;
    public Producer getProducer() {
        if (producer == null) {
            Properties props = new Properties();

            props.put("serializer.class", "kafka.serializer.StringEncoder");

            props.put("metadata.broker.list", kafkaIPPort);

            props.put("request.required.acks", "1");

            this.producer = new Producer<String, String>(new ProducerConfig(props));
        }
        return producer;

    }

    public void sendMessage(String topic, String messageJson) {
        getProducer();
        producer.send(new KeyedMessage<String, String>(topic, random.nextInt() + "", messageJson));
    }

    public static void main(String[] args) {
//        KafkaProducer kafkaProducer = new KafkaProducer();
//        String eventLog = "{'activeStatus':1,'businessId':10003,'channel':'api','createTime':'2018-10-22 10:01:52','creater':'北京集奥集合','decisionFlow':[{'name':'开始','index':'0','to':'1540173873055','position':{'top':110,'left':300},'type':'status','flow':[{'to':'1540173873055','type':'status','value':1,'operator':'=='}]},{'name':'手机行为及运营商数据查验','index':1540173873055,'position':{'top':210,'left':250},'rulesId':3,'flow':[{'to':'1540173877172','type':'status','value':1,'operator':'=='}]},{'name':'多平台借贷行为甄别','index':1540173877172,'position':{'top':328,'left':209},'rulesId':25,'flow':[{'to':'1540173881244','type':'status','value':1,'operator':'=='}]},{'name':'测试规则引擎模版','index':1540173881244,'position':{'top':410,'left':410},'rulesId':91,'flow':[{'to':'1540173885532','type':'status','value':1,'operator':'=='}]},{'name':'test003','index':1540173885532,'position':{'top':550,'left':300},'rulesId':93,'flow':[]}],'eventId':5928,'excuteFlow':[{'eventId':217652,'flow':[{'operator':'==','to':1540173877172,'type':'status','value':1}],'index':1540173873055,'position':'{\\'top\\':210,\\'left\\':250}','rulesId':91,'score':100,'status':2}],'id':17,'name':'test','parameters':'{\\'realName\\':\\'赵玉柏\\',\\'liveLongitude\\':\\'北京\\',\\'idNumber\\':\\'370404196212262212\\',\\'cycle\\':\\'724\\',\\'cid\\':\\'13306328903\\'}','sceneId':10026,'score':100,'sysStatus':2,'updateTime':'2018-10-30 12:15:14','usedRulesIds':'[3,25,91,93]'}";
//
//        for (int i = 0; i < 50; i++) {
//            kafkaProducer.sendMessage("orderMq", eventLog);
//        }

    }
}
