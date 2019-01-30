package com.geo.rcs.common.util;


import com.geo.rcs.common.log.LogFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月24日 下午2:57
 */
public class LogUtil {

    private  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** 标准日志格式及方法 V2.1.X **/

    /**
     * 调试日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void info(String activeName,String actionName,String updateUser) {
        LoggerFactory.getLogger(getClassName()).info(infoMessage(activeName, actionName, updateUser, TimeUtil.dqsj(),""));
    }

    /**
     * 调试日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void info(String activeName,String actionName,String updateUser,String status) {
        LoggerFactory.getLogger(getClassName()).info(infoMessage(activeName, actionName, updateUser, TimeUtil.dqsj(),status));
    }

    /**
     * 失败日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void error(String activeName,String actionName,String updateUser,Exception e) {
        LoggerFactory.getLogger(getClassName()).error(errorMessage(activeName, actionName, updateUser, TimeUtil.dqsj(), "错误", getStackTraceInfo(e)));
    }

    /**
     * 失败日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void error(String activeName,String actionName,String updateUser,String status,Exception e) {
        LoggerFactory.getLogger(getClassName()).error(errorMessage(activeName, actionName, updateUser, TimeUtil.dqsj(), status, getStackTraceInfo(e)));
    }

    /**
     * 警告日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void warn(String activeName,String actionName,String updateUser) {
        LoggerFactory.getLogger(getClassName()).warn(warnMessage(activeName, actionName, updateUser, TimeUtil.dqsj(),""));
    }

    /**
     * 警告日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void warn(String activeName,String actionName,String updateUser,String status) {
        LoggerFactory.getLogger(getClassName()).warn(warnMessage(activeName, actionName, updateUser, TimeUtil.dqsj(),status));
    }

    /**
     * 调试日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void debug(String activeName,String actionName,String updateUser) {
        LoggerFactory.getLogger(getClassName()).debug(debugMessage(activeName, actionName, updateUser, TimeUtil.dqsj(),""));
    }
    /**
     * 调试日志方法
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static void debug(String activeName,String actionName,String updateUser,String status) {
        LoggerFactory.getLogger(getClassName()).debug(debugMessage(activeName, actionName, updateUser, TimeUtil.dqsj(),status));
    }


    // 获取调用 error,info,debug静态类的类名
    private static String getClassName() {
        return new SecurityManager() {
            public String getClassName() {
                return getClassContext()[3].getName();
            }
        }.getClassName();
    }

    /**
     * 获取e.printStackTrace() 的具体信息，赋值给String 变量，并返回
     *
     * @param e
     *            Exception
     * @return e.printStackTrace() 中 的信息
     */
    public static String getStackTraceInfo(Exception e) {

        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);//将出错的栈信息输出到printWriter中
            pw.flush();
            sw.flush();

            return sw.toString();
        } catch (Exception ex) {

            return "发生错误";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

    }

    /**
     * 信息日志格式化
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static String infoMessage(String activeName,String actionName,String updateUser,String dateString, String status) {

        return MessageFormat.format("[操作]:{0} :: [对象]:{1} :: [操作人]:{2} :: [操作时间]:{3} :: [状态]:{4}",
                activeName, actionName, updateUser, dateString, status);
    }


    /**
     * 失败日志格式化
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static String errorMessage(String activeName,String actionName,String updateUser,String dateString, String status, String e) {

        return MessageFormat.format("[操作]:{0} :: [对象]:{1} :: [操作人]:{2} :: [操作时间]:{3} :: [状态]:{4} :: [异常]:{5}",
                activeName, actionName, updateUser, dateString, status, e);
    }

    /**
     * 调试日志格式化
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static String debugMessage(String activeName,String actionName,String updateUser,String dateString, String status) {

        return MessageFormat.format("[操作]:{0} :: [对象]:{1} :: [操作人]:{2} :: [操作时间]:{3} :: [状态]:{4}",
                activeName, actionName, updateUser, dateString,status);
    }

    /**
     * 警告日志格式化
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static String warnMessage(String activeName,String actionName,String updateUser,String dateString, String status) {

        return MessageFormat.format("[操作]:{0} :: [对象]:{1} :: [操作人]:{2} :: [操作时间]:{3} :: [状态]:{4}",
                activeName, actionName, updateUser, dateString, status);
    }


    /** V2.0.X及以下 **/

    /**
     * 注销日志
     * @dropped on v2.1.0
     * @param activeName
     * @param
     * @param updateUser
     */
    public static String loginOut(String activeName,String status ,String dateString,String updateUser) {

        return MessageFormat.format("[操作]:{0} :: [状态]:{1}  ::  [用户名]:{2} :: [操作时间]:{3}",
                activeName, status, updateUser, dateString);
    }

    /**
     * 提交审批日志
     * @dropped on v2.1.0
     * @param activeName
     * @param actionName
     * @param subUser
     */
    public static String addApproval(String activeName,String actionName,String dateString, String subUser) {

        return MessageFormat.format("[操作]:{0} :: [对象名称]:{1} :: [提交人]:{2} :: [提交时间]:{3}",
                activeName, actionName, subUser, dateString);
    }

    /**
     * 审批日志
     * @dropped on v2.1.0
     * @param activeName
     * @param actionName
     * @param appUser
     */
    public static String approval(String activeName,String actionName,String appUser,String dateString) {

        return MessageFormat.format("[操作]:{0} :: [对象名称]:{1} :: [审批人]:{2} :: [审批时间}:{3}",
                activeName, actionName, appUser, dateString);
    }

    /**
     * 登录日志
     * @dropped on v2.1.0
     * @param activeName
     * @param loginUser
     * @param loginStatus
     */
    public static String login(String activeName,String loginUser,String loginStatus,String dateString) {

        return MessageFormat.format("[操作]:{0} :: [用户名]:{1} :: [状态]:{2}  :: [登录时间]:{3}",
                activeName,loginUser,loginStatus, dateString);
    }

    /**
     * 登录失败日志
     * @dropped on v2.1.0
     * @param activeName
     * @param loginUser
     * @param loginStatus
     */
    public static String loginError(String activeName,String loginUser,String loginStatus,String dateString) {

        return MessageFormat.format("[操作]:{0} :: [用户名]:{1} :: [状态]:{2}  :: [登录时间]:{3}",
                activeName,loginUser,loginStatus, dateString);
    }

    /**
     * 添加操作日志
     * @dropped on v2.1.0
     * @param activeName
     * @param actionName
     * @param addUser
     */
    public static String add(String activeName,String actionName,String addUser,String dateString) {

        return MessageFormat.format("[操作]:{0} :: [对象名称]:{1} :: [添加人]:{2} :: [添加时间]:{3}",
                activeName, actionName, addUser, dateString);
    }

    /**
     * 修改操作日志
     * @dropped on v2.1.0
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static String update(String activeName,String actionName,String updateUser,String dateString) {

        return MessageFormat.format("[操作]:{0} :: [对象名称]:{1} :: [修改人]:{2} :: [修改时间]:{3}",
                activeName, actionName, updateUser, dateString);
    }


    /**
     * 操作日志
     * @dropped on v2.1.0
     * @param activeName
     * @param
     * @param updateUser
     */
    public static String operation(String activeName,String actionName,String updateUser,String dateString,String status) {

        return MessageFormat.format("[操作]:{0} :: [操作对象]:{1} :: [操作人]:{2} :: [操作时间]:{3} :: [状态]:{4}",
                activeName, actionName, updateUser, dateString,status);
    }

    /**
     * 失败日志格式化
     * @dropped on v2.1.0
     * @param activeName
     * @param actionName
     * @param updateUser
     */
    public static String errorLog(String activeName,String actionName,String updateUser,String dateString, String e) {

        return MessageFormat.format("[操作]:{0} :: [对象]:{1} :: [操作人]:{2} :: [操作时间]:{3} :: [异常]:{4}",
                activeName, actionName, updateUser, dateString,e);
    }

    /**
     * 打印到指定的文件下
     *
     * @param desc 日志文件名称
     * @return
     */
    public static Logger logger(LogFileName desc) {

        return LoggerFactory.getLogger(desc.getLogFileName()
        );
    }


}

