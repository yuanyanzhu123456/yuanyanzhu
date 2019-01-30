package com.geo.rcs.modules.engine.drools;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.Collection;
import java.util.Iterator;


public class DroolsRunner {

    // todo： 已废弃，使用DroolsService替代
    public static Object runStaticRules(String ruleFile, Object runner) {

        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource(ruleFile), ResourceType.DRL);
        System.out.println("规则启动：");
        if (kbuilder.hasErrors()){
            System.out.println("规则错误：");
            Iterator<KnowledgeBuilderError> it = kbuilder.getErrors().iterator();
            while (it.hasNext()) System.out.println(it.next());
            throw new RcsException(StatusCode.RULE_BUILD_ERROR.getMessage(), StatusCode.RULE_BUILD_ERROR.getCode());
        }
        final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
        final KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase();
        kb.addKnowledgePackages(pkgs);
        final StatefulKnowledgeSession s = kb.newStatefulKnowledgeSession();

        s.insert(runner);
        s.fireAllRules();
        s.dispose();
        System.out.println("规则结束");

        return runner;
    }


    /***
     * Automatic
     * Refer: https://docs.jboss.org/jbpm/v6.0.Beta2/javadocs/org/kie/api/builder/KieFileSystem.html#write(java.lang.String, byte[])
     * @ write method
     */
    // todo： 已废弃，使用DroolsService替代
    public static Object runDynamicRules(String ruleContent, String rulesFileId, Object runner){

        // TODO:从配置读取路径信息
        //String fileName = String.format("/Users/mingming/gitroom/rcs/rules/%s.drl", rulesFileId);

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();

        kfs.write( String.format("src/main/resources/rules/%s", rulesFileId),ruleContent.getBytes());
        //kfs.write( String.format("src/main/resources/rules/%s.drl", rulesFileId), ruleContent);
        KieBuilder kieBuilder = kieServices.newKieBuilder( kfs ).buildAll();
        Results results = kieBuilder.getResults();
        if( results.hasMessages( org.kie.api.builder.Message.Level.ERROR ) ){
            System.out.println( results.getMessages() );
            throw new IllegalStateException( String.format("语法错误：\n%s",results.getMessages() ) );
        }
        KieContainer kieContainer = kieServices.newKieContainer( kieServices.getRepository().getDefaultReleaseId() );
        KieBase kieBase = kieContainer.getKieBase();
        KieSession ksession = kieBase.newKieSession();

        ksession.insert(runner);
        ksession.fireAllRules();
        ksession.dispose();
        System.out.println("规则结束");

        kfs.delete(String.format("src/main/resources/rules/%s.drl", rulesFileId));

        return runner;
    }
}
