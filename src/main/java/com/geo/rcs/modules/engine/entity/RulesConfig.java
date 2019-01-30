package com.geo.rcs.modules.engine.entity;

public class RulesConfig {


    private  static  String decisionConfig = "[{\n" +
            "    \"index\": 1,\n" +
            "    \"rulesId\": 137,\n" +
            "    \"flow\": [{\n" +
            "        \"type\": \"status\",\n" +
            "        \"operator\": \"==\",\n" +
            "        \"value\": 2,\n" +
            "        \"to\": 2\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"score\",\n" +
            "        \"operator\": \">\",\n" +
            "        \"value\": 50,\n" +
            "        \"to\": 3\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"index\": 2,\n" +
            "    \"rulesId\": 138,\n" +
            "    \"flow\": [{\n" +
            "        \"type\": \"score\",\n" +
            "        \"operator\": \"<\",\n" +
            "        \"value\": 1,\n" +
            "        \"to\": 2\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"status\",\n" +
            "        \"operator\": \"==\",\n" +
            "        \"value\": 3,\n" +
            "        \"to\": 3\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    private static String rulesConfig = "{\n" +
            "    \"id\": \"10001\",\n" +
            "    \"name\": \"rules001\",\n" +
            "    \"ruleList\": [\n" +
            "        {\n" +
            "            \"name\": \"rule001\",\n" +
            "            \"id\": \"10001\", \n" +
            "            \"threshold\": 100, \n" +
            "            \"score\": 100,\n" +
            "            \"level\": 1,\n" +
            "            \"conditionRelationShip\":\"10001||10002\", \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con001\", \"id\": \"10001\",\"fieldRelationShip\":\"10001||10002\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi001\", \"fieldName\": \"fi001\", \"id\": \"10001\",  \"fieldType\": \"string\", \"value\": \"0\", \"parameter\": \"0\", \"operator\":\"!=\", \"result\": 1},\n" +
            "                        {\"showName\": \"fi002\", \"fieldName\": \"fi002\", \"id\": \"10002\",  \"fieldType\": \"int\", \"value\": \"4\", \"parameter\": 50, \"operator\":\">=\", \"result\": 0}\n" +
            "                    ] \n" +
            "                } ,\n" +
            "\n" +
            "                {\n" +
            "                    \"name\": \"con002\", \"id\": \"10002\", \"fieldRelationShip\":\"10003||10004\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi003\", \"fieldName\": \"fi003\",\"id\": \"10003\", \"fieldType\": \"String\", \"value\": \"你好\", \"parameter\": \"你好\", \"operator\":\"==\", \"result\": 1},\n" +
            "                        {\"showName\": \"fi004\", \"fieldName\": \"fi004\",\"id\": \"10004\", \"fieldType\": \"int\", \"value\": \"30.1\", \"parameter\": \"30.1\", \"operator\":\"==\", \"result\": 0}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]    \n" +
            "        } ,\n" +
            "        {\n" +
            "            \"name\": \"rule002\", \n" +
            "            \"id\": \"10002\", \n" +
            "            \"threshold\": 100,\n" +
            "            \"score\": 100,\n" +
            "            \"level\": 3,\n" +
            "            \"conditionRelationShip\":\"10003||10004||10005||10006\",     \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con003\", \"id\": \"10003\",  \"fieldRelationShip\":\"10005\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi005\", \"fieldName\": \"fi005\", \"id\": \"10005\", \"fieldType\": \"Date\", \"value\": \"2019-01-01\", \"parameter\": \"2018-01-01\", \"operator\":\">\", \"result\": 0}\n" +
            "                    ]\n" +
            "                } , \n" +
            "                {\n" +
            "                    \"name\": \"con004\", \"id\": \"10004\",  \"fieldRelationShip\":\"10006\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi006\", \"fieldName\": \"fi006\", \"id\": \"10006\", \"fieldType\": \"Datetime\", \"value\": \"2017-11-13 13:43:48\", \"parameter\": \"100\", \"operator\":\"->=\", \"result\": 1}\n" +
            "                    ]\n" +
            "                }\n" +
            "                {\n" +
            "                    \"name\": \"con005\", \"id\": \"10005\",  \"fieldRelationShip\":\"10007\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi007\", \"fieldName\": \"fi007\", \"id\": \"10007\", \"fieldType\": \"Date\", \"value\": \"2018-01-01\", \"parameter\": \"9\", \"operator\":\"->=\", \"result\": 1}\n" +
            "                    ]\n" +
            "                }\n" +
            "                {\n" +
            "                    \"name\": \"con006\", \"id\": \"10006\",  \"fieldRelationShip\":\"10008\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi008\", \"fieldName\": \"fi008\", \"id\": \"10008\", \"fieldType\": \"Date\", \"value\": \"2018-01-01\", \"parameter\": \"9\", \"operator\":\"-<=\", \"result\": 1}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ] \n" +
            "        }\n" +
            "    \n" +
            "\n" +
            "    ],\n" +
            "\n" +
            "    \"match_type\": 1,\n" +
            "    \"threshold_min\": 300, \n" +
            "    \"threshold_max\": 600,\n" +
            "    \"status\": 0,\n" +
            "    \"parameters\": \n" +
            "      {\n" +
            "        \"cid\": \"13306328903\",     \n" +
            "        \"idNumber\": \"370404196212262212\",  \n" +
            "        \"realName\": \"赵玉柏\"      \n" +
            "      } \n" +
            "}\n";


    private static String rulesConfig2 = "{\n" +
            "    \"id\": \"10001\",\n" +
            "    \"name\": \"rules001\",\n" +
            "    \"ruleList\": [\n" +
            "        {\n" +
            "            \"name\": \"rule001\",\n" +
            "            \"id\": \"10001\", \n" +
            "            \"threshold\": 500, \n" +
            "            \"score\": 0,\n" +
            "            \"level\": 1,\n" +
            "            \"conditionRelationShip\":\"10001|10002\", \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con001\", \"id\": \"10001\",\"fieldRelationShip\":\"10001|10002\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"省份\", \"fieldName\": \"province\", \"id\": \"10001\",  \"fieldType\": \"String\", \"value\": \"\",\"describe\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 1},\n" +
            "                        {\"showName\": \"城市\", \"fieldName\": \"city\", \"id\": \"10002\",  \"fieldType\": \"String\", \"value\": \"\",\"describe\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ] \n" +
            "                } ,\n" +
            "\n" +
            "                {\n" +
            "                    \"name\": \"con002\", \"id\": \"10002\", \"fieldRelationShip\":\"10003|10005\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"运营商\", \"fieldName\": \"isp\",  \"id\": \"10003\", \"fieldType\": \"String\", \"value\": \"\", \"describe\": \"\",\"parameter\": \"你好\", \"operator\":\"==\", \"result\": 1},\n" +
            "                        {\"showName\": \"在网状态\", \"fieldName\": \"onlineStatus\", \"id\": \"10005\", \"fieldType\": \"String\", \"value\": \"\", \"describe\": \"\", \"parameter\": \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]    \n" +
            "        } ,\n" +
            "    \n" +
            "    ],\n" +
            "\n" +
            "    \"match_type\": 1,\n" +
            "    \"describe\": \"\",\n" +
            "    \"threshold_min\": 300, \n" +
            "    \"threshold_max\": 600,\n" +
            "    \"status\": 0,\n" +
            "    \"parameters\": \n" +
            "      {\n" +
            "        \"cid\": \"13306328903\",     \n" +
            "        \"idNumber\": \"370404196212262212\",  \n" +
            "        \"realName\": \"赵玉柏\"      \n" +
            "      } \n" +
            "}\n";

    private static String errorRulesConfig = "{\n" +
            "    \"id\": \"10001\",\n" +
            "    \"name\": \"rules001\",\n" +
            "    \"ruleList\": [\n" +
            "        {\n" +
            "            \"name\": \"rule001\",\n" +
            "            \"id\": \"10001\", \n" +
            "            \"threshold\": 500, \n" +
            "            \"score\": 0,\n" +
            "            \"level\": 1,\n" +
            "            \"conditionRelationShip\":\"10001|10002\", \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con001\", \"id\": \"10001\",\"fieldRelationShip\":\"10001|10002\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"省份\", \"fieldName\": \"province\",  \"id\": \"10001\",  \"fieldType\": \"String\", \"value\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 1},\n" +
            "                        {\"showName\": \"城市\", \"fieldName\": \"city\",  \"id\": \"10002\",  \"fieldType\": \"String\", \"value\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ] \n" +
            "                } ,\n" +
            "\n" +
            "                {\n" +
            "                    \"name\": \"con002\", \"id\": \"10002\", \"fieldRelationShip\":\"10003|10005\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"运营商\", \"fieldName\": \"isp\", \"id\": \"10003\", \"fieldType\": \"String\", \"value\": \"\", \"parameter\": \"你好\", \"operator\":\"==\", \"result\": 1},\n" +
            "                        {\"showName\": \"在网状态\", \"fieldName\": \"onlineStatus\", \"id\": \"10005\", \"fieldType\": \"String\", \"value\": \"\", \"parameter\": \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]    \n" +
            "        } ,\n" +
            "    \n" +
            "    ],\n" +
            "\n" +
            "    \"match_type\": 1,\n" +
            "    \"threshold_min\": 300, \n" +
            "    \"threshold_max\": 600,\n" +
            "    \"status\": 0,\n" +
            "    \"parameters\": \n" +
            "      {\n" +
            "        \"cid\": \"13306328903\",     \n" +
            "        \"idNumber\": \"370404196212262212\",  \n" +
            "        \"realName\": \"赵玉柏\"      \n" +
            "      } \n" +
            "}\n";


    /**
     * 测试规则引擎配置RulesConfig，处理数据源出错的配置==>直接不计算，返回结果
     */
    private static String errorRulesConfigForEngine = "{\n" +
            "    \"id\": \"10001\",\n" +
            "    \"name\": \"rules001\",\n" +
            "    \"ruleList\": [\n" +
            "        {\n" +
            "            \"name\": \"rule001\",\n" +
            "            \"id\": \"10001\", \n" +
            "            \"threshold\": 500, \n" +
            "            \"score\": 0,\n" +
            "            \"level\": 1,\n" +
            "            \"conditionRelationShip\":\"10001|10002\", \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con001\", \"id\": \"10001\",\"fieldRelationShip\":\"10001|10002\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"省份\", \"fieldName\": \"province\",  \"id\": \"10001\",  \"fieldType\": \"String\", \"value\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 1},\n" +
            "                        {\"showName\": \"城市\", \"fieldName\": \"city\",  \"id\": \"10002\",  \"fieldType\": \"String\", \"value\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ] \n" +
            "                } ,\n" +
            "\n" +
            "                {\n" +
            "                    \"name\": \"con002\", \"id\": \"10002\", \"fieldRelationShip\":\"10003|10005\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"运营商\", \"fieldName\": \"isp\", \"id\": \"10003\", \"fieldType\": \"String\", \"value\": \"\", \"parameter\": \"你好\", \"operator\":\"==\", \"result\": 1},\n" +
            "                        {\"showName\": \"在网状态\", \"fieldName\": \"onlineStatus\", \"id\": \"10005\", \"fieldType\": \"String\", \"value\": \"\", \"parameter\": \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]    \n" +
            "        } ,\n" +
            "    \n" +
            "    ],\n" +
            "\n" +
            "    \"match_type\": 1,\n" +
            "    \"threshold_min\": 300, \n" +
            "    \"threshold_max\": 600,\n" +
            "    \"status\": 2,\n" +
            "    \"parameters\": \n" +
            "      {\n" +
            "        \"cid\": \"13306328903\",     \n" +
            "        \"idNumber\": \"370404196212262212\",  \n" +
            "        \"realName\": \"赵玉柏\"      \n" +
            "      } \n" +
            "}\n";

    /**
     * 测试规则引擎，处理数据源语法错误的配置 ==> 抛出异常
     *
     * @error： 语法错误
     * @error： 数据错误
     */
    private static String errorRulesConfigForEngine2 = "{\n" +
            "    \"id\": \"10001\",\n" +
            "    \"name\": \"rules001\",\n" +
            "    \"ruleList\": [\n" +
            "        {\n" +
            "            \"name\": \"rule001\",\n" +
            "            \"id\": \"10001\", \n" +
            "            \"threshold\": 500, \n" +
            "            \"score\": 0,\n" +
            "            \"level\": 1,\n" +
            "            \"conditionRelationShip\":\"10001|10002\", \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con001\", \"id\": \"10001\",\"fieldRelationShip\":\"10001|10002\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"省份\", \"fieldName\": \"province\",  \"id\": \"10001\",  \"fieldType\": \"String\", \"value\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 1},\n" +
            "                        {\"showName\": \"城市\", \"fieldName\": \"city\",  \"id\": \"10002\",  \"fieldType\": \"String\", \"value\": \"\", \"parameter\":  \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ] \n" +
            "                } ,\n" +
            "\n" +
            "                {\n" +
            "                    \"name\": \"con002\", \"id\": \"10002\", \"fieldRelationShip\":\"10003|10005\", \"result\": 0, \"fieldList\": [\n" +
            "                        {\"showName\": \"运营商\", \"fieldName\": \"isp\", \"id\": \"10003\", \"fieldType\": \"String\", \"value\": \"\", \"parameter\": \"你好\", \"operator\":\"==\", \"result\": 1},\n" +
            "                        {\"showName\": \"在网状态\", \"fieldName\": \"onlineStatus\", \"id\": \"10005\", \"fieldType\": \"String\", \"value\": \"\", \"parameter\": \"你好\", \"operator\":\"!=\", \"result\": 0}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]    \n" +
            "        } ,\n" +
            "    \n" +
            "    ],\n" +
            "\n" +
            "    \"match_type\": 1,\n" +
            "    \"threshold_min\": 300, \n" +
            "    \"threshold_max\": 600,\n" +
            "    \"status\": 0,\n" +
            "    \"parameters\": \n" +
            "      {\n" +
            "        \"cid\": \"13306328903\",     \n" +
            "        \"idNumber\": \"370404196212262212\",  \n" +
            "        \"realName\": \"赵玉柏\"      \n" +
            "      } \n" +
            "}\n";

    /**
     * V2版本标准RulesConfig配置
     */

    private static String rulesConfig_v2 = "{\n" +
            "    \"id\": \"10001\",\n" +
            "    \"name\": \"rules001\",\n" +
            "    \"ruleList\": [\n" +
            "        {\n" +
            "            \"name\": \"rule001\",\n" +
            "            \"id\": \"10001\", \n" +
            "            \"threshold\": 100, \n" +
            "            \"score\": 100,\n" +
            "            \"level\": 1,\n" +
            "            \"conditionRelationShip\":\"(10001&&10002)\", \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con001\", \"id\": \"10001\",\"fieldRelationShip\":\"10001||10002\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi001\", \"fieldName\": \"fi001\", \"id\": \"10001\",  \"fieldType\": \"int\", \"value\": 100, \"parameter\": 300, \"operator\":\"isnull\", \"result\": true},\n" +
            "                        {\"showName\": \"fi002\", \"fieldName\": \"fi002\", \"id\": \"10002\",  \"fieldType\": \"int\", \"value\": 100, \"parameter\": 300, \"operator\":\"<\", \"result\": true}\n" +
            "                    ] \n" +
            "                } ,\n" +
            "\n" +
            "                {\n" +
            "                    \"name\": \"con002\", \"id\": \"10002\", \"fieldRelationShip\":\"10003||10004\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi003\", \"fieldName\": \"fi003\",\"id\": \"10003\", \"fieldType\": \"String\", \"value\": \"你好\", \"parameter\": \"你好\", \"operator\":\"==\", \"result\": true},\n" +
            "                        {\"showName\": \"fi004\", \"fieldName\": \"fi004\",\"id\": \"10004\", \"fieldType\": \"int\", \"value\": 100, \"parameter\": 300, \"operator\":\"<\", \"result\": true}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]    \n" +
            "        } ,\n" +
            "        {\n" +
            "            \"name\": \"rule002\", \n" +
            "            \"id\": \"10002\", \n" +
            "            \"threshold\": 100,\n" +
            "            \"score\": 100,\n" +
            "            \"level\": 3,\n" +
            "            \"conditionRelationShip\":\"(10003||10004)\",     \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con003\", \"id\": \"10003\",  \"fieldRelationShip\":\"10005\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi005\", \"fieldName\": \"fi005\", \"id\": \"10005\", \"fieldType\": \"Date\", \"value\": \"2017-01-01\", \"parameter\": \"2018-01-01\", \"operator\":\">\", \"result\": true}\n" +
            "                    ]\n" +
            "                } , \n" +
            "                {\n" +
            "                    \"name\": \"con004\", \"id\": \"10004\",  \"fieldRelationShip\":\"10006\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi006\", \"fieldName\": \"fi006\", \"id\": \"10006\", \"fieldType\": \"Datetime\", \"value\": \"2017-01-01 00:00:00\", \"parameter\": \"2018-01-01 00:00:00\", \"operator\":\"<\", \"result\": true}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ] \n" +
            "        }\n" +
            "    \n" +
            "\n" +
            "    ],\n" +
            "\n" +
            "    \"match_type\": 0,\n" +
            "    \"threshold_min\": 300, \n" +
            "    \"threshold_max\": 600,\n" +
            "    \"status\": 0,\n" +
            "    \"parameters\": \n" +
            "      {\n" +
            "        \"cid\": \"13306328903\",     \n" +
            "        \"idNumber\": \"370404196212262212\",  \n" +
            "        \"realName\": \"赵玉柏\"      \n" +
            "      } \n" +
            "}\n";


    /**
     * V2版本-错误配置：
     * @Error1: 条件运算时，非法条件ID
     * @Error2: 条件运算时，非法运算符
     * @Error3: 字段运算时，缺少运算符
     * @return
     */
    private static String ErrorRulesConfig_v2 = "{\n" +
            "    \"id\": \"10001\",\n" +
            "    \"name\": \"rules001\",\n" +
            "    \"ruleList\": [\n" +
            "        {\n" +
            "            \"name\": \"rule001\",\n" +
            "            \"id\": \"10001\", \n" +
            "            \"threshold\": 100, \n" +
            "            \"score\": 100,\n" +
            "            \"level\": 1,\n" +
            "            \"conditionRelationShip\":\"(10001||10002)\", \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con001\", \"id\": \"10001\",\"fieldRelationShip\":\"10001||10002\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi001\", \"fieldName\": \"fi001\", \"id\": \"10001\",  \"fieldType\": \"int\", \"value\": 100, \"parameter\": 300, \"operator\":\"+\", \"result\": true},\n" +
            "                        {\"showName\": \"fi002\", \"fieldName\": \"fi002\", \"id\": \"10002\",  \"fieldType\": \"int\", \"value\": 100, \"parameter\": 300, \"operator\":\"<\", \"result\": true}\n" +
            "                    ] \n" +
            "                } ,\n" +
            "\n" +
            "                {\n" +
            "                    \"name\": \"con002\", \"id\": \"10002\", \"fieldRelationShip\":\"10003||10004\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi003\", \"fieldName\": \"fi003\",\"id\": \"10003\", \"fieldType\": \"String\", \"value\": \"你好\", \"parameter\": \"你好\", \"operator\":\"==\", \"result\": true},\n" +
            "                        {\"showName\": \"fi004\", \"fieldName\": \"fi004\",\"id\": \"10004\", \"fieldType\": \"int\", \"value\": 100, \"parameter\": 300, \"operator\":\"<\", \"result\": true}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]    \n" +
            "        } ,\n" +
            "        {\n" +
            "            \"name\": \"rule002\", \n" +
            "            \"id\": \"10002\", \n" +
            "            \"threshold\": 100,\n" +
            "            \"score\": 100,\n" +
            "            \"level\": 3,\n" +
            "            \"conditionRelationShip\":\"(10003||10004)\",     \n" +
            "            \"conditionsList\": [\n" +
            "                {\n" +
            "                    \"name\": \"con003\", \"id\": \"10003\",  \"fieldRelationShip\":\"10005\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi005\", \"fieldName\": \"fi005\", \"id\": \"10005\", \"fieldType\": \"Date\", \"value\": \"2017-01-01\", \"parameter\": \"2018-01-01\", \"operator\":\">\", \"result\": true}\n" +
            "                    ]\n" +
            "                } , \n" +
            "                {\n" +
            "                    \"name\": \"con004\", \"id\": \"10004\",  \"fieldRelationShip\":\"10006\", \"result\": true, \"fieldList\": [\n" +
            "                        {\"showName\": \"fi006\", \"fieldName\": \"fi006\", \"id\": \"10006\", \"fieldType\": \"Datetime\", \"value\": \"2017-01-01 00:00:00\", \"parameter\": \"2018-01-01 00:00:00\", \"operator\":\"<\", \"result\": true}\n" +
            "                    ]\n" +
            "                }\n" +
            "            ] \n" +
            "        }\n" +
            "    \n" +
            "\n" +
            "    ],\n" +
            "\n" +
            "    \"match_type\": 0,\n" +
            "    \"threshold_min\": 300, \n" +
            "    \"threshold_max\": 600,\n" +
            "    \"status\": 0,\n" +
            "    \"parameters\": \n" +
            "      {\n" +
            "        \"cid\": \"13306328903\",     \n" +
            "        \"idNumber\": \"370404196212262212\",  \n" +
            "        \"realName\": \"赵玉柏\"      \n" +
            "      } \n" +
            "}\n";

    private static String prodRuleConfig = "{\"id\":10075,\"matchType\":1,\"name\":\"手机行为及运营商数据查验\",\"parameters\":{\"cid\":\"13306328903\",\"idNumber\":\"370404196212262212\",\"liveLongitude\":\"北京市天安门\",\"realName\":\"赵玉柏\",\"workLongitude\":\"北京市天安门\"},\"ruleList\":[{\"conditionRelationShip\":\"217\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10006\",\"fieldName\":\"IdPhoneNameValidate\",\"fieldType\":\"string\",\"id\":10322,\"operator\":\"!=\",\"parameter\":\"0\",\"value\":\"0\",\"valueDesc\":\"三维验证一致\"}],\"fieldRelationShip\":\"10322\",\"id\":217,\"name\":\"手机三要素验证不一致\"}],\"id\":10289,\"level\":3,\"name\":\"手机三要素验证不一致\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"218\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10323,\"operator\":\"==\",\"parameter\":\"03\",\"value\":\"3\",\"valueDesc\":\"(24,+)\"},{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10324,\"operator\":\"==\",\"parameter\":\"04\",\"value\":\"3\",\"valueDesc\":\"(24,+)\"}],\"fieldRelationShip\":\"10323&&10324\",\"id\":218,\"name\":\"手机号在网时长小于6个月\"}],\"id\":10290,\"level\":3,\"name\":\"手机在网时长小于6个月\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"219\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10325,\"operator\":\"==\",\"parameter\":\"3\",\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10326,\"operator\":\"==\",\"parameter\":\"2\",\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10327,\"operator\":\"==\",\"parameter\":\"4\",\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10325||10326||10327\",\"id\":219,\"name\":\"手机号状态为不在网、不可用、销号\"}],\"id\":10291,\"level\":3,\"name\":\"手机状态为不在网、不可用、销号\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"220\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10328,\"operator\":\"==\",\"parameter\":\"1\",\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10328\",\"id\":220,\"name\":\"手机号状态为停机\"}],\"id\":10292,\"level\":2,\"name\":\"手机号状态为停机\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"221\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10013\",\"fieldName\":\"consumerGradePlus\",\"fieldType\":\"string\",\"id\":10329,\"operator\":\"==\",\"parameter\":\"010\",\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10329\",\"id\":221,\"name\":\"手机号码消费档次月均为0元\"}],\"id\":10293,\"level\":3,\"name\":\"手机号码消费档次月均为0元\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"222\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10454\",\"fieldName\":\"ownPhoneNumber\",\"fieldType\":\"int\",\"id\":10330,\"operator\":\">\",\"parameter\":\"1\",\"value\":\"0\",\"valueDesc\":\"不大于2个\"}],\"fieldRelationShip\":\"10330\",\"id\":222,\"name\":\"手机号自然人接入号码个数大于2\"}],\"id\":10294,\"level\":2,\"name\":\"手机号自然人接入号码个数\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"223\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10331,\"operator\":\">\",\"parameter\":\"0\",\"value\":\"1\",\"valueDesc\":\"(0,3]\"},{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10332,\"operator\":\"<=\",\"parameter\":\"3\",\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10331&&10332\",\"id\":223,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\"}],\"id\":10295,\"level\":2,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"224\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10333,\"operator\":\">\",\"parameter\":\"3\",\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10333\",\"id\":224,\"name\":\"手机号近3个月停机次数大于3次\"}],\"id\":10296,\"level\":3,\"name\":\"手机号近3个月停机次数大于3次\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"225\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10456\",\"fieldName\":\"phoneDataLevel\",\"fieldType\":\"string\",\"id\":10334,\"operator\":\"==\",\"parameter\":\"01\",\"value\":\"05\",\"valueDesc\":\"[2000,+)\"}],\"fieldRelationShip\":\"10334\",\"id\":225,\"name\":\"手机号近3个月平均流量小于100MB\"}],\"id\":10297,\"level\":2,\"name\":\"手机号近3个月平均流量小于100MB\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"226\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10016\",\"fieldName\":\"residenceValidate\",\"fieldType\":\"string\",\"id\":10335,\"operator\":\"!=\",\"parameter\":\"1\",\"value\":\"05\",\"valueDesc\":\"不在同一城市\"}],\"fieldRelationShip\":\"10335\",\"id\":226,\"name\":\"家庭地址比对不一致\"}],\"id\":10298,\"level\":2,\"name\":\"家庭地址比对不一致\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"227\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10015\",\"fieldName\":\"workPlaceValidate\",\"fieldType\":\"string\",\"id\":10336,\"operator\":\"!=\",\"parameter\":\"1\",\"value\":\"05\",\"valueDesc\":\"不在同一城市\"}],\"fieldRelationShip\":\"10336\",\"id\":227,\"name\":\"单位地址比对不一致\"}],\"id\":10299,\"level\":2,\"name\":\"单位地址比对不一致\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"228\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10455\",\"fieldName\":\"phoneDepartStatus\",\"fieldType\":\"int\",\"id\":10337,\"operator\":\"==\",\"parameter\":\"1\",\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10337\",\"id\":228,\"name\":\"手机号为出境状态\"}],\"id\":10300,\"level\":2,\"name\":\"手机号为出境状态\",\"score\":0,\"threshold\":50}],\"score\":0,\"sourceData\":{\"ownPhoneNumber\":{\"field\":\"ownPhoneNumber\",\"valueDesc\":\"不大于2个\",\"inter\":\"A5\",\"value\":\"0\"},\"city\":{\"field\":\"city\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"枣庄\"},\"isp\":{\"field\":\"isp\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"电信\"},\"onlineStatus\":{\"field\":\"onlineStatus\",\"valueDesc\":\"正常在用\",\"inter\":\"A4\",\"value\":\"0\"},\"phoneDepartStatus\":{\"field\":\"phoneDepartStatus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"C2\",\"value\":\"\"},\"IdPhoneNameValidate\":{\"field\":\"IdPhoneNameValidate\",\"valueDesc\":\"三维验证一致\",\"inter\":\"B7\",\"value\":\"0\"},\"residenceValidate\":{\"field\":\"residenceValidate\",\"valueDesc\":\"不在同一城市\",\"inter\":\"B19\",\"value\":\"05\"},\"outOfServiceTimes\":{\"field\":\"outOfServiceTimes\",\"valueDesc\":\"(0,3]\",\"inter\":\"B13\",\"value\":\"1\"},\"phoneDataLevel\":{\"field\":\"phoneDataLevel\",\"valueDesc\":\"[2000,+)\",\"inter\":\"B21\",\"value\":\"05\"},\"province\":{\"field\":\"province\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"山东\"},\"onlineTime\":{\"field\":\"onlineTime\",\"valueDesc\":\"(24,+)\",\"inter\":\"A3\",\"value\":\"3\"},\"consumerGradePlus\":{\"field\":\"consumerGradePlus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"D3\",\"value\":\"\"},\"workPlaceValidate\":{\"field\":\"workPlaceValidate\",\"valueDesc\":\"不在同一城市\",\"inter\":\"B18\",\"value\":\"05\"}},\"status\":0,\"thresholdMax\":150,\"thresholdMin\":100}\n";


    private static String prodBatchRuleConfig = "{\"id\":#*KeyId*#,\"matchType\":1,\"name\":\"手机行为及运营商数据查验\",\"parameters\":{\"cid\":\"13306328903\",\"idNumber\":\"370404196212262212\",\"liveLongitude\":\"北京市天安门\",\"realName\":\"赵玉柏\",\"workLongitude\":\"北京市天安门\"},\"ruleList\":[{\"conditionRelationShip\":\"217\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10006\",\"fieldName\":\"IdPhoneNameValidate\",\"fieldType\":\"string\",\"id\":10322,\"operator\":\"!=\",\"parameter\":\"0\",\"value\":\"0\",\"valueDesc\":\"三维验证一致\"}],\"fieldRelationShip\":\"10322\",\"id\":217,\"name\":\"手机三要素验证不一致\"}],\"id\":10289,\"level\":3,\"name\":\"手机三要素验证不一致\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"218\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10323,\"operator\":\"==\",\"parameter\":\"03\",\"value\":\"3\",\"valueDesc\":\"(24,+)\"},{\"fieldId\":\"10004\",\"fieldName\":\"onlineTime\",\"fieldType\":\"string\",\"id\":10324,\"operator\":\"==\",\"parameter\":\"04\",\"value\":\"3\",\"valueDesc\":\"(24,+)\"}],\"fieldRelationShip\":\"10323&&10324\",\"id\":218,\"name\":\"手机号在网时长小于6个月\"}],\"id\":10290,\"level\":3,\"name\":\"手机在网时长小于6个月\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"219\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10325,\"operator\":\"==\",\"parameter\":\"3\",\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10326,\"operator\":\"==\",\"parameter\":\"2\",\"value\":\"0\",\"valueDesc\":\"正常在用\"},{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10327,\"operator\":\"==\",\"parameter\":\"4\",\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10325||10326||10327\",\"id\":219,\"name\":\"手机号状态为不在网、不可用、销号\"}],\"id\":10291,\"level\":3,\"name\":\"手机状态为不在网、不可用、销号\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"220\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10005\",\"fieldName\":\"onlineStatus\",\"fieldType\":\"string\",\"id\":10328,\"operator\":\"==\",\"parameter\":\"1\",\"value\":\"0\",\"valueDesc\":\"正常在用\"}],\"fieldRelationShip\":\"10328\",\"id\":220,\"name\":\"手机号状态为停机\"}],\"id\":10292,\"level\":2,\"name\":\"手机号状态为停机\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"221\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10013\",\"fieldName\":\"consumerGradePlus\",\"fieldType\":\"string\",\"id\":10329,\"operator\":\"==\",\"parameter\":\"010\",\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10329\",\"id\":221,\"name\":\"手机号码消费档次月均为0元\"}],\"id\":10293,\"level\":3,\"name\":\"手机号码消费档次月均为0元\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"222\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10454\",\"fieldName\":\"ownPhoneNumber\",\"fieldType\":\"int\",\"id\":10330,\"operator\":\">\",\"parameter\":\"1\",\"value\":\"0\",\"valueDesc\":\"不大于2个\"}],\"fieldRelationShip\":\"10330\",\"id\":222,\"name\":\"手机号自然人接入号码个数大于2\"}],\"id\":10294,\"level\":2,\"name\":\"手机号自然人接入号码个数\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"223\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10331,\"operator\":\">\",\"parameter\":\"0\",\"value\":\"1\",\"valueDesc\":\"(0,3]\"},{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10332,\"operator\":\"<=\",\"parameter\":\"3\",\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10331&&10332\",\"id\":223,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\"}],\"id\":10295,\"level\":2,\"name\":\"手机号近3个月停机次数大于0次小于等于3次\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"224\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10007\",\"fieldName\":\"outOfServiceTimes\",\"fieldType\":\"int\",\"id\":10333,\"operator\":\">\",\"parameter\":\"3\",\"value\":\"1\",\"valueDesc\":\"(0,3]\"}],\"fieldRelationShip\":\"10333\",\"id\":224,\"name\":\"手机号近3个月停机次数大于3次\"}],\"id\":10296,\"level\":3,\"name\":\"手机号近3个月停机次数大于3次\",\"score\":0,\"threshold\":100},{\"conditionRelationShip\":\"225\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10456\",\"fieldName\":\"phoneDataLevel\",\"fieldType\":\"string\",\"id\":10334,\"operator\":\"==\",\"parameter\":\"01\",\"value\":\"05\",\"valueDesc\":\"[2000,+)\"}],\"fieldRelationShip\":\"10334\",\"id\":225,\"name\":\"手机号近3个月平均流量小于100MB\"}],\"id\":10297,\"level\":2,\"name\":\"手机号近3个月平均流量小于100MB\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"226\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10016\",\"fieldName\":\"residenceValidate\",\"fieldType\":\"string\",\"id\":10335,\"operator\":\"!=\",\"parameter\":\"1\",\"value\":\"05\",\"valueDesc\":\"不在同一城市\"}],\"fieldRelationShip\":\"10335\",\"id\":226,\"name\":\"家庭地址比对不一致\"}],\"id\":10298,\"level\":2,\"name\":\"家庭地址比对不一致\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"227\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10015\",\"fieldName\":\"workPlaceValidate\",\"fieldType\":\"string\",\"id\":10336,\"operator\":\"!=\",\"parameter\":\"1\",\"value\":\"05\",\"valueDesc\":\"不在同一城市\"}],\"fieldRelationShip\":\"10336\",\"id\":227,\"name\":\"单位地址比对不一致\"}],\"id\":10299,\"level\":2,\"name\":\"单位地址比对不一致\",\"score\":0,\"threshold\":50},{\"conditionRelationShip\":\"228\",\"conditionsList\":[{\"fieldList\":[{\"fieldId\":\"10455\",\"fieldName\":\"phoneDepartStatus\",\"fieldType\":\"int\",\"id\":10337,\"operator\":\"==\",\"parameter\":\"1\",\"value\":\"\",\"valueDesc\":\"暂不支持此运营商\"}],\"fieldRelationShip\":\"10337\",\"id\":228,\"name\":\"手机号为出境状态\"}],\"id\":10300,\"level\":2,\"name\":\"手机号为出境状态\",\"score\":0,\"threshold\":50}],\"score\":0,\"sourceData\":{\"ownPhoneNumber\":{\"field\":\"ownPhoneNumber\",\"valueDesc\":\"不大于2个\",\"inter\":\"A5\",\"value\":\"0\"},\"city\":{\"field\":\"city\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"枣庄\"},\"isp\":{\"field\":\"isp\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"电信\"},\"onlineStatus\":{\"field\":\"onlineStatus\",\"valueDesc\":\"正常在用\",\"inter\":\"A4\",\"value\":\"0\"},\"phoneDepartStatus\":{\"field\":\"phoneDepartStatus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"C2\",\"value\":\"\"},\"IdPhoneNameValidate\":{\"field\":\"IdPhoneNameValidate\",\"valueDesc\":\"三维验证一致\",\"inter\":\"B7\",\"value\":\"0\"},\"residenceValidate\":{\"field\":\"residenceValidate\",\"valueDesc\":\"不在同一城市\",\"inter\":\"B19\",\"value\":\"05\"},\"outOfServiceTimes\":{\"field\":\"outOfServiceTimes\",\"valueDesc\":\"(0,3]\",\"inter\":\"B13\",\"value\":\"1\"},\"phoneDataLevel\":{\"field\":\"phoneDataLevel\",\"valueDesc\":\"[2000,+)\",\"inter\":\"B21\",\"value\":\"05\"},\"province\":{\"field\":\"province\",\"valueDesc\":\"\",\"inter\":\"\",\"value\":\"山东\"},\"onlineTime\":{\"field\":\"onlineTime\",\"valueDesc\":\"(24,+)\",\"inter\":\"A3\",\"value\":\"3\"},\"consumerGradePlus\":{\"field\":\"consumerGradePlus\",\"valueDesc\":\"暂不支持此运营商\",\"inter\":\"D3\",\"value\":\"\"},\"workPlaceValidate\":{\"field\":\"workPlaceValidate\",\"valueDesc\":\"不在同一城市\",\"inter\":\"B18\",\"value\":\"05\"}},\"status\":0,\"thresholdMax\":150,\"thresholdMin\":100}\n";


    public static String getRulesConfig() {
        return rulesConfig;
    }

    public static String getRulesConfig2() {
        return rulesConfig2;
    }

    public static String getErrorRulesConfig() {
        return errorRulesConfig;
    }

    public static String getErrorRulesConfigForEngine() {
        return errorRulesConfigForEngine;
    }

    public static String getErrorRulesConfigForEngine2() {
        return errorRulesConfigForEngine2;
    }



    public static String getRulesConfig_v2() {
        return rulesConfig_v2;
    }

    public static String getErrorRulesConfig_v2() {
        return ErrorRulesConfig_v2;
    }

    public static String getProdRuleConfig() {
        return prodRuleConfig;
    }

    public static String getprodBatchRuleConfig() {
        return prodBatchRuleConfig;
    }
}
