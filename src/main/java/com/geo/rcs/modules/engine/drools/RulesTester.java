package com.geo.rcs.modules.engine.drools;

import com.geo.rcs.modules.engine.entity.Message;
import com.geo.rcs.modules.engine.entity.User;

public class RulesTester {

    public static void main(String[] args) throws Exception{

        /**
         * Start Demo
         */
        String ruleFile2 = "startDemo.drl";

        final Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        DroolsRunner.runStaticRules(ruleFile2, message);

        /**
         * calculateDemo
         */
        String ruleFile = "calculateDemo.drl";
        int money = 33;

        User user=new User();
        user.setMoney(money);

        DroolsRunner.runStaticRules(ruleFile, user);
        System.out.println("总瓶数："+ user.getTotals());
    }


}
