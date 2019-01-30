package com.geo.rcs.modules.source.handler;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.constant.ConstantThreadPool;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.engine.entity.Condition;
import com.geo.rcs.modules.engine.entity.Field;
import com.geo.rcs.modules.engine.entity.Rule;
import com.geo.rcs.modules.engine.entity.Rules;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static com.geo.rcs.modules.source.entity.InterStatusCode.ECLKEYS;

public class SourceFactory {

	  private static final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(ConstantThreadPool.TASK_QUEUE_SIZE.getParm());
	  public static ThreadPoolExecutor executor = new ThreadPoolExecutor(ConstantThreadPool.CORE_POOL_SIZE.getParm(),ConstantThreadPool.MAXIMUM_POOL_SIZE.getParm(),ConstantThreadPool.KEEP_ALIVE_TIME.getParm(), ConstantThreadPool.TIME_UNIT.getTimeUtil(), queue);
		private static final Integer MAX_TASK_NUM=ConstantThreadPool.MAX_TASK_NUM.getMaxTask();

    public static String rulesDataPackager(String rulesConfig, Map<String, Map> rulesData ) {

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);

        List<Rule> ruleSet = rules.getRuleList();
        System.out.println("rulesData:"+rulesData);
        System.out.println("rulesConfig:"+rulesConfig);

        if(rulesData != null && !rulesData.isEmpty()){
            try{
                rules.setSourceData(rulesData);
                for (Rule rule: ruleSet) {
                    List<Condition> conSet = rule.getConditionsList();
                    for (Condition con: conSet) {
                        List<Field> _fieldSet = con.getFieldList();
                        for (Field field:_fieldSet) {
                            String fieldName = field.getFieldName();
                            String fieldType = field.getFieldType();
                            System.out.println("FieldName:"+ fieldName);
                            Map fieldValue = rulesData.get(fieldName);
                            //rulesData {onlineTime={field=onlineTime, valueDesc=(24,+), inter=A3, value=3}, province={field=province, valueDesc=, inter=, value=山西}, city={field=city, valueDesc=, inter=, value=长治}, isp={field=isp, valueDesc=, inter=, value=移动}}
                            //fieldValue {field=province, valueDesc=, inter=, value=山西}
                            if(fieldValue != null && !fieldValue.isEmpty()){
                                field.setValue(ObjectUtils.toString(fieldValue.get("value")));
                                // System.out.println(ECLDICT.keySet().toArray()[0]);

                                if(fieldValue.get("value") != null && ECLKEYS.contains(fieldValue.get("value"))){
                                    fieldValue.put("value","");
                                    field.setValue(ObjectUtils.toString(fieldValue.get("value")));
                                    //fieldValue.put("valueDesc",fieldValue.get("valueDesc"));
                                }

                                if(fieldValue.get("value") != null && fieldType.toUpperCase().equals("DATE") ){
                                    field.setValue(FieldFormattor.DateFormattor(fieldValue.get("value").toString()));
                                    fieldValue.put("value", FieldFormattor.DateFormattor(fieldValue.get("value").toString()));
                                }else if(fieldValue.get("value") != null && fieldType.toUpperCase().equals("DATETIME")){
                                    field.setValue(FieldFormattor.DatetimeFormattor(fieldValue.get("value").toString()));
                                    fieldValue.put("value", FieldFormattor.DatetimeFormattor(fieldValue.get("value").toString()));
                                }
                                field.setValueDesc(ObjectUtils.toString(fieldValue.get("valueDesc")));

                            }
                            else{
                                field.setValue(ObjectUtils.toString(null));
                                field.setValueDesc(ObjectUtils.toString("源数据无该字段信息"));
                            }
                          //多线程=====================
//                        	try {
//								while (executor.getActiveCount() + queue.size() == MAX_TASK_NUM) {
//									System.out.println("**********字段赋值最大线程数并且,线程队列已满5个进入等待**********");
//								}
//								  executor.execute(new Thread(new ThreadFieldValueSet(rulesData, field)));
//								
//							} catch (RejectedExecutionException e) {
//								new RejectedExecutionHandler() {
//									@Override
//									public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//										if (!executor.isShutdown()) {
//											try {
//												System.out.println("放入队列====");
//												executor.getQueue().put(r);
//												System.out.println("已放入队列=======");
//											} catch (InterruptedException e) {
//												// should not be interrupted
//											}
//										}
//									}
//								}.rejectedExecution(new Thread(new ThreadFieldValueSet(rulesData, field)), executor);;
//								// TODO Auto-generated catch block
////								e.printStackTrace();
//							}
                        	//=====================
                        }
                        
                    }
                }
              //=====================
//                while (executor.getActiveCount()!=0) {
//                	System.out.println("等待字段线程完毕==========");
//        		}
              //=====================  
            }catch (NullPointerException e){
                e.printStackTrace();
                throw new RcsException(StatusCode.FIELD_ERROR.getMessage(), StatusCode.FIELD_ERROR.getCode());
            }
        }else{
            throw new RcsException(StatusCode.FIELD_ERROR.getMessage(), StatusCode.FIELD_ERROR.getCode());
        }

        rulesConfig = JSON.toJSONString(rules);

        return rulesConfig;
    }
}

