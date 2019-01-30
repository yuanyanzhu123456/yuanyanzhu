package com.geo.rcs.modules.event.handler;

public class EventRunnerHandler {

    /**
     * java多线程同步锁的使用
     * 示例：三个售票窗口同时出售10张票
     * */
    public static void main(String[] args) {

        //实例化站台对象，并为每一个测试定义名称
        EventTestHandler client1=new EventTestHandler("1号测试节点");
        EventTestHandler client2=new EventTestHandler("2号测试节点");
        EventTestHandler client3=new EventTestHandler("3号测试节点");
        EventTestHandler client4=new EventTestHandler("4号测试节点");
        EventTestHandler client5=new EventTestHandler("5号测试节点");
        EventTestHandler client6=new EventTestHandler("6号测试节点");
        EventTestHandler client7=new EventTestHandler("7号测试节点");
        EventTestHandler client8=new EventTestHandler("8号测试节点");
        EventTestHandler client9=new EventTestHandler("9号测试节点");
        EventTestHandler client10=new EventTestHandler("10号测试节点");

        // 让测试员自开始工作
        client1.start();
//        client2.start();
//        client3.start();
//        client4.start();
//        client5.start();
//        client6.start();
//        client7.start();
//        client8.start();
//        client9.start();
//        client10.start();
    }
}

