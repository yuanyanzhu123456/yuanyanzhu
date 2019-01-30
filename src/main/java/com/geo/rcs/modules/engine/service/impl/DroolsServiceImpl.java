package com.geo.rcs.modules.engine.service.impl;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.engine.service.DroolsService;
import com.geo.rcs.modules.jobs.service.impl.ExpiryMapCache;
import com.geo.rcs.modules.jobs.service.impl.RedisServiceImpl;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class DroolsServiceImpl implements DroolsService {
    @Autowired
    private RedisServiceImpl redisService;
    //规则文件缓存时效3天
    private Map<String, Map<String, Object>> kieBaseMapCache = new ExpiryMapCache(1000 * 60 * 60 * 24 * 3);
    private static int RulesDrlCacheTime = 60 * 60 * 24 * 3;
    private static final String RULES_DRL_ = "RULES_DRL_";
    private static final String KIEBASE_KEY = "KIEBASE_KEY";
    private static final String RULES_CONTENT_KEY = "RULES_CONTENT_KEY";
    //缓存类型redis/map
    private static final String CACHE_TYPE = "map";
    //Map缓存规则文件容量
    private static final int MAPCACHE_CAPACITY = 50;

    @Override
    public Object runStaticRules(String ruleFile, Object runner) {

        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource(ruleFile), ResourceType.DRL);
        System.out.println("规则启动：");
        if (kbuilder.hasErrors()){
            System.out.println("规则错误：");
            Iterator<KnowledgeBuilderError> it = kbuilder.getErrors().iterator();
            while (it.hasNext()) System.out.println(it.next());
//                return runner;
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
    @Override
    public Object runDynamicRules(String ruleContent, String rulesFileId, Object runner){

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

    public Object runDynamicCacheRules(String ruleContent, String rulesFileId, Object runner) {
        rulesFileId = RULES_DRL_ + rulesFileId;
        KieBase kieBase = getKieBaseByCache(rulesFileId, ruleContent);
        KieSession ksession = kieBase.newKieSession();
        ksession.insert(runner);
        ksession.fireAllRules();
        ksession.dispose();
        System.out.println("规则结束");
        return runner;
    }

    private KieBase getKieBaseByCache(String rulesKey, String ruleContent) {
        KieBase kieBase = null;
        if ("redis".equalsIgnoreCase(CACHE_TYPE)) {
            try {
                List<byte[]> kieBaseByteList = redisService.getJedis().hmget(rulesKey.getBytes(), KIEBASE_KEY.getBytes());
                if (kieBaseByteList == null || !(kieBaseByteList.get(0) instanceof byte[])) {
                    kieBase = putKieBaseToRedis(rulesKey, ruleContent);
                } else {
                    List<byte[]> rulesByteList = redisService.getJedis().hmget(rulesKey.getBytes(), RULES_CONTENT_KEY.getBytes());
                    byte[] oldRulesBytes = rulesByteList.get(0);
                    byte[] oldKieBaseBytes = kieBaseByteList.get(0);
                    String oldRulesContent = new String(oldRulesBytes);
                    if (ruleContent.equals(oldRulesContent)) {
                        toUpdateKieBase(rulesKey, oldRulesBytes, oldKieBaseBytes);
                        kieBase = (KieBase) unserizlize(oldKieBaseBytes);
                    } else {
                        kieBase = putKieBaseToRedis(rulesKey, ruleContent);
                    }
                }
                return kieBase;
            } catch (Exception e) {
                LogUtil.error("规则引擎KieBase从redis缓存获取失败!开始从Map缓存获取!", "", "", e);
                try {
                    kieBase = getKieBaseByMap(rulesKey, ruleContent);
                } catch (Exception e1) {
                    LogUtil.error("规则引擎KieBase从Map缓存获取失败!开始直接获取KieBase!", "", "", e1);
                    try {
                        kieBase = getKieBase(rulesKey, ruleContent);
                    } catch (Exception e2) {
                        LogUtil.error("规则引擎直接获取KieBase失败!", "", "", e2);
                    }
                }
                return kieBase;
            }
        } else {
            try {
                kieBase = getKieBaseByMap(rulesKey, ruleContent);
            } catch (Exception e1) {
                LogUtil.error("规则引擎KieBase从Map缓存获取失败!开始直接获取KieBase!", "", "", e1);
                try {
                    kieBase = getKieBase(rulesKey, ruleContent);
                } catch (Exception e2) {
                    LogUtil.error("规则引擎直接获取KieBase失败!", "", "", e2);
                }
            }
        }
        return kieBase;
    }

    private void toUpdateKieBase(String rulesKey, byte[] oldRulesBytes, byte[] oldKieBaseBytes) {
        HashMap<byte[], byte[]> hashMap = new HashMap<>();
        hashMap.put(KIEBASE_KEY.getBytes(), oldKieBaseBytes);
        hashMap.put(RULES_CONTENT_KEY.getBytes(), oldRulesBytes);
        redisService.getJedis().hmset(rulesKey.getBytes(), hashMap);
        redisService.getJedis().expire(rulesKey.getBytes(), RulesDrlCacheTime);
    }

    private KieBase putKieBaseToRedis(String rulesKey, String ruleContent) {
        byte[] KieBaseBytes = null;
        KieBase kieBase = getKieBase(rulesKey, ruleContent);
        KieBaseBytes = serialize(kieBase);
        toUpdateKieBase(rulesKey, ruleContent.getBytes(), KieBaseBytes);
        return kieBase;
    }

    private KieBase getKieBaseByMap(String rulesFileId, String ruleContent) {
        Map<String, Object> valueMaP = kieBaseMapCache.get(rulesFileId);
        if (valueMaP == null) {
            KieBase kieBase = getKieBase(rulesFileId, ruleContent);
            if (kieBaseMapCache.size() <= MAPCACHE_CAPACITY) {
                HashMap<String, Object> valueMap = new HashMap<>();
                valueMap.put(KIEBASE_KEY, kieBase);
                valueMap.put(RULES_CONTENT_KEY, ruleContent);
                kieBaseMapCache.put(rulesFileId, valueMap);
            }
            return kieBase;
        } else {
            if (ruleContent.equals((String) valueMaP.get(RULES_CONTENT_KEY))) {
                //更新过期时间
                if (kieBaseMapCache.size() <= MAPCACHE_CAPACITY) {
                    kieBaseMapCache.put(rulesFileId, valueMaP);
                }
                return (KieBase) valueMaP.get(KIEBASE_KEY);
            } else {
                KieBase kieBase = getKieBase(rulesFileId, ruleContent);
                if (kieBaseMapCache.size() <= MAPCACHE_CAPACITY) {
                    HashMap<String, Object> valueMap = new HashMap<>();
                    valueMap.put(KIEBASE_KEY, kieBase);
                    valueMap.put(RULES_CONTENT_KEY, ruleContent);
                    kieBaseMapCache.put(rulesFileId, valueMap);
                }
                return kieBase;
            }
        }
    }

    private KieBase getKieBase(String rulesFileId, String ruleContent) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write(String.format("src/main/resources/rules/%s", rulesFileId), ruleContent.getBytes());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException(String.format("语法错误：\n%s", results.getMessages()));
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        return kieContainer.getKieBase();
    }

    //序列化
    public static byte[] serialize(Object obj) {
        ObjectOutputStream obi = null;
        ByteArrayOutputStream bai = null;
        try {
            bai = new ByteArrayOutputStream();
            obi = new ObjectOutputStream(bai);
            obi.writeObject(obj);
            byte[] byt = bai.toByteArray();
            return byt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //反序列化
    public static Object unserizlize(byte[] byt) {
        ObjectInputStream oii = null;
        ByteArrayInputStream bis = null;
        bis = new ByteArrayInputStream(byt);
        try {
            oii = new ObjectInputStream(bis);
            Object obj = oii.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
