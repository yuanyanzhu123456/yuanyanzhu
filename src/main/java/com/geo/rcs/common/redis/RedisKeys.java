package com.geo.rcs.common.redis;

/**
 * Redis所有Keys
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/20 16:52
 */
public class RedisKeys {
    /**
     * 根节点
     */
    private static final String RCS = "rcs";
    private static final String SPLIT_CHAR = ":";

    /**
     * 系统
     */
    public static class SYS {
        private static final String SYS = "sys";
        private static final String SESSION_ID = "sessionid";

        /**
         * session缓存
         */
        public static String sessionKey(String key){
            return RCS + SPLIT_CHAR + SESSION_ID + SPLIT_CHAR + key;
        }
    }

    /**
     * 事件
     */
    public static class EVENT {
        private static final String EVENT = "event";
        private static final String PRODUCT_FILED = "productfield";

        public static String productFieldKey(){
            return RCS + SPLIT_CHAR + EVENT + SPLIT_CHAR + PRODUCT_FILED;
        }
    }

    /**
     * 验真平台认证信息
     */
    public static class YANZHEN {
        private static final String YANZHEN = "yanzhen";
        private static final String OAUTH_INFO = "oauth_info";
        private static final String USER_ID = "user_id_";

        public static String getOauthInfoKey(Object userId) {
            return RCS + SPLIT_CHAR + YANZHEN + SPLIT_CHAR + OAUTH_INFO + SPLIT_CHAR + USER_ID + userId;
        }
    }
}
