package com.geo.rcs.modules.datapool.util;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.MD5Util;

public class RidGenerator {

    /**
     * DataPool Rid Generator
     * @param cid
     * @param realName
     * @param idNumber
     * @return
     */
    public static  String generate(String realName, String idNumber, String cid){

        if(realName == null || realName.length()<2 || cid == null ||
                cid.length()<11 || idNumber == null || idNumber.length() < 18){
            throw  new RcsException(StatusCode.PARAMS_ERROR.getMessage(), StatusCode.PARAMS_ERROR.getCode());
        }

        String _id = String.valueOf(Integer.valueOf(idNumber.substring(2,6)) % 100);
        String ridHeader = _id.length()<2 ? "0"+_id : _id;
        String rid =  MD5Util.encode(realName+cid+idNumber);
        return ridHeader+rid.substring(2);

    }

    public static void  main (String[] args){

        System.out.println(generate("许国强", "433023197710040058", "13405972289" ));
        System.out.println(generate( "许国强", "433003197710040058", "13405972289"));

    }
}
