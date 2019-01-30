package com.geo.rcs.common.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2019年01月04日 17:55
 */
public class ArraysUtil {

    /**
     * 合并数组并去重
     * @param arrayList
     * @return
     */
    public static String[] arrayListToArray(List<String> arrayList) {
        String[] aa0 = null;
        List objects = new ArrayList<>();
        for (String s : arrayList) {
            String[] arr = s.replace("[","").replace("]","").split(",");
            objects.add(arr);
        }
        // tyy 每次都是两个数组合并 所以合并的次数为 collect.size() ，第一个是虚拟的数组
        for (int i = 0; i < objects.size(); i++) {
            String[] aa1 = (String[]) objects.get(i);
            String[] newInt = onArrayTogater(aa0, aa1);
            aa0 = newInt;
        }
        //利用hashSet去重
        Set<String> set = new HashSet<>();
        for(int i=0;i<aa0.length;i++){
            set.add(aa0[i]);
        }
        String[] arrayResult = (String[]) set.toArray(new String[set.size()]);
        return arrayResult;
    }

    private static String[] onArrayTogater(String[] aa, String[] bb) {
        // TODO Auto-generated method stub
        if (aa == null) {
            return bb;
        }
        String[] collectionInt = new String[aa.length + bb.length];
        for (int i = 0; i < aa.length; i++) {
            collectionInt[i] = aa[i];
        }
        for (int i = aa.length; i < aa.length + bb.length; i++) {
            collectionInt[i] = bb[i - aa.length];
        }
        return collectionInt;

    }
}
