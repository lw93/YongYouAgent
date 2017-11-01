package com.asia.yongyou.yongyouagent.ws;

import android.content.Context;

import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.entity.IDCardInfo;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;


/**
 * 用户请求
 *
 * @author : chengcs
 * @create_time : 2015年3月25日 下午2:29:10
 * @desc : TODO
 * @update_person:
 * @update_time :
 * @update_desc :
 */
public class WSUser {

    public static RequestHandle updateVersion(Context context, BaseResponseHandler responseHandler) {

            return WSClient.getJson(context, Constant.UPDATE_VERSION, null, responseHandler);
    }


    /**
     * 代理商登录
     *
     * @param context
     * @param agentNum
     * @param agentPwd
     * @param resoponseHandler
     * @return
     */
    public static RequestHandle agentLogin(Context context, String agentNum, String agentPwd,
                                           BaseResponseHandler resoponseHandler) {

        RequestParams params = new RequestParams();
        params.put("agentNum",agentNum);
        params.put("agentPassword",agentPwd);
        return WSClient.getJson(context,Constant.AGENT_LOGIN,params,resoponseHandler);
    }

    /**
     * 返档号码验证请求
     *
     * @param context
     * @param archiveTel
     * @param agentNum
     * @param simCardNum
     * @param resoponseHandler
     * @return
     */
    public static RequestHandle archiveCheck(Context context,String archiveTel,String agentNum,
                                             String simCardNum,BaseResponseHandler resoponseHandler){
        RequestParams params=new RequestParams();
        params.put("telPhone",archiveTel);
        params.put("agentNum",agentNum);
        params.put("simCardNum",simCardNum);

        return WSClient.getJson(context, Constant.ARCHIVE_PHONE_CHECK, params, resoponseHandler);
    }

    /**
     * 检查是否是老用户
     *
     * @param context
     * @param certNum
     * @param agentNum
     * @param resoponseHandler
     * @return
     */
    public static RequestHandle checkUser(Context context, String agentNum, String certNum,
                                          BaseResponseHandler resoponseHandler) {
        RequestParams params = new RequestParams();
        params.put("agentNum", agentNum);
        params.put("certNum", certNum);
        return WSClient.getJson(context, Constant.CHECK_USER_URL, params, resoponseHandler);
    }

    /**
     * 反档激活
     * 身份证信息订单
     *
     * @param context
     * @param requestParams
     * @param resoponseHandler
     * @return
     */
    public static RequestHandle submitIDCardOrder(Context context, RequestParams requestParams, BaseResponseHandler resoponseHandler) {

        return WSClient.getJson(context, Constant.IDCARD_ORDER_URL, requestParams, resoponseHandler);
    }

    public static RequestHandle getAgentInfo(Context context,String agentNum,BaseResponseHandler responseHandler){
            RequestParams params = new RequestParams();
            params.put("agentNum",agentNum);
        return WSClient.getJson(context,Constant.GET_AGENT_INFO,params,responseHandler);
    }

    //开户查询号码等级
    public static RequestHandle getNumLvl(Context context,RequestParams params,BaseResponseHandler responseHandler){

        return WSClient.getJson(context,Constant.GET_NUM_LVL,params,responseHandler);
    }

    //开户模糊查询手机号
    public static  RequestHandle numFuzzyQuery(Context context,String numberLevel,String telPhoneNum
            ,String agentNum,int pageNo,BaseResponseHandler responseHandler){

        RequestParams params = new RequestParams();
        params.put("numberLevel",numberLevel);
        params.put("telPhoneNum",telPhoneNum);
        params.put("agentNum",agentNum);
        params.put("pageNo",pageNo);

        return WSClient.getJson(context,Constant.FUZZY_QUERY_NUM,params,responseHandler);
    }

    //开户模糊查询sim卡号
    public static RequestHandle simFuzzyQuery(Context context,String simNum,String agentNum,BaseResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("simNum",simNum);
        params.put("agentNum",agentNum);
        return WSClient.getJson(context,Constant.FUZZY_QUERY_SIM,params,responseHandler);
    }

    //开户校验手机号和sim卡号
    public static RequestHandle checkTelPhone(Context context,String telPhone,String simNum,String agentNum,BaseResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("telPhone",telPhone);
        params.put("simNum",simNum);
        params.put("agentNum",agentNum);

        return WSClient.getJson(context,Constant.OPEN_PHONE_CHECK,params,responseHandler);
    }

    //开户选择套餐
    public static RequestHandle choosePackages(Context context,String telPhone,String agentNum,BaseResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("telPhone",telPhone);
        params.put("agentNum",agentNum);

        return WSClient.getJson(context,Constant.QUERY_SET_MEAL,params,responseHandler);
    }

    //开户精确查找套餐
    public static RequestHandle queryPackageByPrice(Context context,String telPhone,String agentNum,String productType,String commAdjustFee,
                                                    BaseResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("telPhone",telPhone);
        params.put("agentNum",agentNum);
        params.put("productType",productType);
        params.put("commAdjustFee",commAdjustFee);

        return WSClient.getJson(context,Constant.QUERY_PACKAGE_BY_PRICE,params,responseHandler);

    }

    //开户选择增值产品
    public static RequestHandle chooseProduct(Context context,String telPhone,String agentNum,BaseResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("telPhone",telPhone);
        params.put("agentNum",agentNum);

        return WSClient.getJson(context,Constant.QUERY_PRODUCT,params,responseHandler);
    }

    //开户
    public static RequestHandle openAccount(Context context, IDCardInfo idCardInfo, String agentNum,
                                            String contactTel, String telPhone,String simNum,String isOldCust,String frontImg,String backImg,
                                            String handImg,String custFace,String productIdList,BaseResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("agentNum",agentNum);
        params.put("certNum",idCardInfo.getCertNumber());
        params.put("certName",idCardInfo.getPartyName());
        params.put("contactTel",contactTel);
        params.put("certSex",idCardInfo.getGender());
        params.put("certNation",idCardInfo.getNation());
        params.put("certBirth",idCardInfo.getBornDay());
        params.put("certAddress",idCardInfo.getCertAddress());
        params.put("validDate",idCardInfo.getEffDate());
        params.put("invalidDate",idCardInfo.getExpDate());
        params.put("isOldCust",isOldCust);
        params.put("telPhone",telPhone);
        params.put("simNum",simNum);
        params.put("frontImg",frontImg);
        params.put("backImg",backImg);
        params.put("handImg",handImg);
        params.put("custFace",custFace);
        params.put("productIdList",productIdList);

        return WSClient.getJson(context,Constant.OPEN_ACCOUNT,params,responseHandler);
    }



}
