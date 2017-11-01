package com.asia.yongyou.yongyouagent.constant;

/**
 * Created by ichen on 2017/5/8.
 */
public class Constant {

    /*测试环境*/
    public static final String BASE_URL = "http://54.223.215.137:24811/MVNO-HBH";
    /*集成环境*/
//	public static final String BASE_URL = "http://54.222.159.189:34861/MVNO-HBH" ;
    /*生产环境*/
//	public static final String BASE_URL = "http://54.222.154.14:19210/MVNO-HBH" ;

    public static final String CHECK_USER_URL = "/business/checkOldCust";
    public static final String IDCARD_ORDER_URL = "/business/custReg";
    public static final String GET_AGENT_INFO = "/agent/agentInfo";
    public static final String UPDATE_VERSION = "/app/versionMassege";
    public static final String AGENT_LOGIN = "/agent/agentLogin";
    public static final String ARCHIVE_PHONE_CHECK = "/business/archiveCheckSim";

    //查询号码等级
    public static final String GET_NUM_LVL="/user/getNumLevalList";
    //查询号码列表
    public static final String FUZZY_QUERY_NUM="/user/fuzzyQueryNumByNumsublevel";
    //查询sim卡号
    public static final String FUZZY_QUERY_SIM="/user/fuzzyQuerySimNum";
    //开户校验手机号和sim卡号
    public static final String OPEN_PHONE_CHECK="/user/checkTelPhone";
    //开户查套餐
    public static final String QUERY_SET_MEAL="/business/querySetmeal";
    //通过档位查套餐
    public static final String QUERY_PACKAGE_BY_PRICE="/business/querySetmealByCommAdjustFee";
    //开户查增值产品
    public static final String QUERY_PRODUCT="/business/queryProduct";
    //开户
    public static final String OPEN_ACCOUNT="/user/openAccount";

    public static final String CHARSET_UTF8 = "utf-8";
    public static final String STATUS_OK = "1";
    public static final String FORCE_UPDATE = "0";

    public static final String HANDLE = "handle";
    public static final String USER = "user";


    // 40.151 0.181
    public static final String ip = "115.28.2.173";//218.56.11.181   211.138.20.171   58.18.172.100
    //	public static final String ip = "cardreadv-kaer.chnl.zj.chinamobile.com";//218.56.11.181   211.138.20.171   58.18.172.100
    public static final String acc = "admin";   //bishenle_test
    public static final String pwd = "www.kaer.cn";   //as!@ER34
    public static final int port = 7443;

    public static final String IDCARDINFO = "IDCardItem";

}
