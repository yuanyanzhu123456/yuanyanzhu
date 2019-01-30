package com.geo.rcs.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断访问设备为Web、Android、ios--工具类
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/1/15 18:27
 */
public class DeviceUtils {

    /** 浏览器中的请求头User-Agent关键词 **/
    private static final String[] IOS_SYS = { "iPhone", "iPad", "iPod" };
    private static final String ANDROID_SYS = "Android";
    private static final String WECHAT = "micromessenger";

    /** 系统常量名 **/
    private static final String DEFAULT_NAME = "Other";
    private static final String WEB_NAME = "Web";
    private static final String IOS_NAME = "ios";
    private static final String ANDROID_NAME = "Android";
    private static final String WECHAT_NAME = "WeChat";

    /**
     * 判断是否为IOS系统访问
     * @param request
     * @return
     */
    public static boolean isIOS(HttpServletRequest request) {
        boolean isIOS = false;
        String userAgent = request.getHeader("user-agent");
        for (int i = 0; !isIOS && userAgent != null && !"".equals(userAgent.trim()) && i < IOS_SYS.length; i++) {
            if (userAgent.contains(IOS_SYS[i])) {
                isIOS = true;
                break;
            }
        }
        return isIOS;
    }

    /**
     * 判断是否为android系统访问
     * @param request
     * @return
     */
    public static boolean isAndroid(HttpServletRequest request) {
        boolean isAndroid = false;
        String userAgent = request.getHeader("user-agent");
        if(userAgent != null && !"".equals(userAgent.trim()) && userAgent.indexOf(ANDROID_SYS) != -1) {
            isAndroid = true;
        }
        return isAndroid;
    }

    /**
     * 判断是否为微信系统访问
     * @param request
     * @return
     */
    public static boolean isWeChat(HttpServletRequest request) {
        boolean isWeChat = false;
        String userAgent = request.getHeader("user-agent");
        if(userAgent != null && !"".equals(userAgent.trim()) && userAgent.indexOf(WECHAT) != -1) {
            isWeChat = true;
        }
        return isWeChat;
    }

    /**
     * 判断是否为Web系统访问
     * @param request
     * @return
     */
    public static boolean isWeb(HttpServletRequest request) {
        boolean isWeb = false;
        String userAgent = request.getHeader("user-agent");
        if(userAgent != null && !"".equals(userAgent.trim()) && userAgent.indexOf(WECHAT) == -1 &&
                userAgent.indexOf(ANDROID_SYS) == -1 && isIOS(request) == false) {
            isWeb = true;
        }
        return isWeb;
    }

    /**
     * 获取访问系统的名称
     * @param request
     * @return
     */
    public static String getDeviceSystem(HttpServletRequest request) {
        if(isIOS(request)) { return IOS_NAME; }
        if(isAndroid(request)) { return ANDROID_NAME; }
        if(isWeChat(request)) { return WECHAT_NAME; }
        if(isWeb(request)) { return WEB_NAME; }
        return DEFAULT_NAME;
    }

}
