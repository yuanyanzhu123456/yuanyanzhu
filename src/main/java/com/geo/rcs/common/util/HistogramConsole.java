package com.geo.rcs.common.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yy on 2017/11/1.
 * 柱状图的控制台显示工具类
 * 1、获取Y轴的显示数据
 * 2、比较对应的数据获取显示的柱状
 * 3、计算出最佳的每一行显示的数值
 * 4、将数组合的数据拼接后输出到控制台
 */
public class HistogramConsole {

    private static final int LINE_NUMBER=20; //显示的列的行数
    private static final String ALL_PRINT="█";//满的柱状
    private static final String LINE_PRINT="─";//横线
    private static final String LINE_NUMBER_PRINT="┬";//横坐标
    private static final String ROW_NUMBER_PRINT="┤";//纵坐标
    private static final String WRAP="\n";//换行
    private static final String SPACE=" ";//空格
    /**
     * 接收一个数字类型数组，根据穿进来的数组内容，将数据以柱状图的形式显示到控制台
     * 获取当前数组的最大值，然后根据显示
     * @param array x轴坐标  YArr y轴值
     * @throws Exception 抛出对应的处理异常
     */
    public static  void echo(Comparable[] array, Comparable[] YArr)throws Exception{
        StringBuilder print=new StringBuilder();
        StringBuilder printX=new StringBuilder();
        StringBuilder printN=new StringBuilder();
        Boolean check=true;
        Boolean checkA=true;
        //获取y轴的数据
        Comparable[] YAxis=getArrayYAxis(YArr);
        //获取最大的显示长度
        int lengthY=getArrayDataLength(YAxis)+2;
        int lengthX=getArrayDataLength(array)+2;
        //获取显示的数据
        for(int i=0;i<YAxis.length;i++){
            //获取y坐标
            String YInfo=YAxis[i].toString()+ROW_NUMBER_PRINT;
            print.append(getStringRight(YInfo,lengthY));
            for(int k=0;k<YArr.length;k++){
                //获取柱状
                if (Double.valueOf(YAxis[i].toString())
                        .compareTo(Double.valueOf(YArr[k].toString()))<=0) {
                    print.append(getStringMiddle(ALL_PRINT, lengthX));
                }else{
                    print.append(getStringMiddle(LINE_PRINT, lengthX));
                }
                //获取显示的数字
                if(checkA) {
                    printX.append(getStringMiddle(array[k].toString(), lengthX));
                }
            }
            print.append(WRAP);
            //获取显示的x轴
            if(check){
                for(int k=0;k<(print.length()-lengthY)/2+(array.length)/2;k++){
                    printN.append(LINE_NUMBER_PRINT);
                }
                printN.append(WRAP);
                //获得一次数据后不再循环
                check=false;
                checkA=false;
            }
        }
        //拼接x轴和数字
        print.append(getStringRight("",lengthY)).append(printN)
                .append(getStringRight("",lengthY)).append(printX);
        System.out.println(print);
    }

    /**
     * 获取数组中最长的值的长度
     * @param array 传入数组长度
     * @return 返回最大长度
     */
    private static int getArrayDataLength(Object[] array){
        int length=0;
        for(int i=0;i<array.length;i++){
            length=length<array[i].toString().length()?array[i].toString().length():length;
        }
        return length;
    }
    /**
     * 获取Y轴循环使用的数组
     * 1、将传进来的数据转成Double，比较
     * 2、将获取最大值和最小区间，然后截取显示行数的数据（使用BigDecimal控制精度）
     * @param array 传入的数组
     * @return 返回Y轴循环使用的数组,降序
     */
    private static Comparable[] getArrayYAxis(Comparable[] array){
        //获取最大值和最小值
        Map<String,Comparable> MaxAndMin=getMaxAndMin(array);
        Double[] back=new Double[LINE_NUMBER];
        String max=MaxAndMin.get("max").toString();
        String min=MaxAndMin.get("min").toString();
        //判断最小值是否小于零,小于零区间会更大
        Boolean negative=Double.valueOf(min).compareTo(Double.valueOf(1))<0;
        Double all=negative?(Double.valueOf(max)-Double.valueOf(min)):Double.valueOf(max);
        //每行代表的数值.保留两位小数
        Double line=all/LINE_NUMBER;
        for (int i=0;i<LINE_NUMBER;i++){
            back[i]=Double.valueOf(max)-(line*i);
            BigDecimal   b   =   new BigDecimal(back[i]);
            back[i]=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return back;
    }
    /**
     * 获取传去数组的最大值和最小值
     * @param array 传入数组
     * @return 最大值的key是max，最小值的key是min
     */
    private static Map<String,Comparable> getMaxAndMin(Comparable[] array){
        Map<String,Comparable> data=new HashMap<String, Comparable>();
        data.put("max",array[0]);
        data.put("min",array[0]);
        for(int i=0;i<array.length;i++){
            if(data.get("max").compareTo(array[i])<0){
                data.put("max",array[i]);
            }
            if(data.get("min").compareTo(array[i])>0){
                data.put("min",array[i]);
            }
        }
        return data;
    }
    /**
     * 获取传入长度的字符，根据传入的信息，左对齐
     * @param info 需要显示的内容
     * @param length 需要显示的长度，不够长度使用空格补充
     * @return 左对齐的字符信息
     */
    private static String getStringRight(String info,int length){
        StringBuilder data=new StringBuilder();
        for(int i=0;i<length-info.length();i++){
            data.append(SPACE);
        }
        data.append(info);
        return data.toString();
    }

    /**
     * 居中显示传入的字符信息，获取真实的长度，不够则补全，返回给定长度的字符串
     * @param info 传入的需要显示的字符
     * @param length 需要显示的长度
     * @return 返回居中显示的字符
     */
    private static String getStringMiddle(String info,int length){
        //获取字符串的真真实长度
        int trueLength=info.replaceAll("[^\\x00-\\xff]", "**").length();
        int lengthX=(length-trueLength)/2;
        StringBuilder data=new StringBuilder();
        StringBuilder dataX=new StringBuilder();
        for(int i=0;i<lengthX;i++){
            data.append(SPACE);
        }
        data.append(new StringBuffer(info).append(data));
        //补全不够的长度
        for (int k=0;k<length-data.toString().replaceAll("[^\\x00-\\xff]", "**").length();k++){
            dataX.append(SPACE);
        }
        return dataX.append(data).toString();
    }

}
