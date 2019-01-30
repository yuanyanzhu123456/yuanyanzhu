package com.geo.rcs.modules.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:23 2018/11/7
 */
@Component
public class KafkaConsumer implements Runnable {
    public String title;
    public KafkaStream<byte[], byte[]> stream;
    private HdfsClient hdfsClient = new HdfsClient();
    private ExecutorService executor = Executors.newFixedThreadPool(20);
    @Value("${spring.kafka.zkListenIpPortList}")
    private String zkListenIpPortList;

    private String topic;

    public KafkaConsumer(String title, KafkaStream<byte[], byte[]> stream, String topic) {
        this.title = title;
        this.stream = stream;
        this.topic = topic;
    }

    public KafkaConsumer() {
    }

    @Override
    public void run() {
        System.out.println("开始运行 " + title);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        /**
         * 不停地从stream读取新到来的消息，在等待新的消息时，hasNext()会阻塞
         * 如果调用 `ConsumerConnector#shutdown`，那么`hasNext`会返回false
         * */
        while (it.hasNext()) {
            MessageAndMetadata<byte[], byte[]> data = it.next();
            String topic = (String) data.topic();
            int partition = data.partition();
            long offset = data.offset();
            String msg = new String(data.message());
            try {
                if (HadoopConsts.TABLES_RCS2_SERVER.contains(topic)) {
                    hdfsClient.appendLine(topic, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(String.format(
                    "Consumer: [%s],  Topic: [%s],  PartitionId: [%d], Offset: [%d], msg: [%s]",
                    title, topic, partition, offset, msg));
        }
        System.out.println(String.format("Consumer: [%s] exiting ...", title));
    }

    public void createConsumers(String topic, int consumerNums) {
        Properties props = new Properties();
        props.put("group.id", "test-consumer-group");
//        props.put("zookeeper.connect", "zk01:2181,zk02:2181,zk03:2181");
//        zkIpPort="hadoop2.rcs.com:2181";
        props.put("zookeeper.connect", zkListenIpPortList);
        props.put("auto.offset.reset", "largest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("partition.assignment.strategy", "roundrobin");
        ConsumerConfig config = new ConsumerConfig(props);
//        String topic2 = "paymentMq";
        //只要ConsumerConnector还在的话，consumer会一直等待新消息，不会自己退出
        ConsumerConnector consumerConn = Consumer.createJavaConsumerConnector(config);
        //定义一个map
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, consumerNums);
        //Map<String, List<KafkaStream<byte[], byte[]>> 中String是topic， List<KafkaStream<byte[], byte[]>是对应的流
        Map<String, List<KafkaStream<byte[], byte[]>>> topicStreamsMap = consumerConn.createMessageStreams(topicCountMap);
        //取出 `kafkaTest` 对应的 streams
        List<KafkaStream<byte[], byte[]>> streams = topicStreamsMap.get(topic);
        //创建一个容量为4的线程池
        //创建20个consumer threads
        for (int i = 0; i < streams.size(); i++) {
//           KafkaConsumerSimple kafkaConsumerSimple = new KafkaConsumerSimple("消费者" + (i + 1), streams.get(i));
//            kafkaConsumerSimple.consume();
            executor.execute(new KafkaConsumer("消费者" + (i + 1), streams.get(i), topic));
        }
    }

    public static void main(String[] args) throws Exception {
        KafkaConsumer kafkaConsumer = new KafkaConsumer();
        kafkaConsumer.createConsumers(HadoopConsts.TOPIC_API_EVENT_ENTRY, 3);
    }
}