package com.geo.rcs.modules.rule.test.dao;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.test.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月24日 下午12:01
 */
public class Test {

    public static void main(String[] args) {
        String str = "1001&&1002";
        int i;
        str = str.replaceAll("[\\d]","");
        str = "1"+str+"1";
        for(i =0; i<=2;i++){
            str = ReplaceFirst(str);
            System.out.println(str);
        }
        /*String src = "1、1、1";
        src = src.replaceFirst("1","a").replaceFirst("1","b").replaceFirst("1","c");
        System.out.printf(""+src);*/

    }


    public static String ReplaceFirst(String str) {
       return  str.replaceFirst("1","22");
    }
}
