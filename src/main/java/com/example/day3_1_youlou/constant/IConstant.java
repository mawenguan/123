package com.example.day3_1_youlou.constant;

/**
 * Created by mwg on 2017/12/2.
 */

public interface IConstant {
    //此接口封装查询所要得到的4种数据类型
    String MIMETYPE_EMAIL="vnd.android.cursor.item/email_v2";
    String MIMETYPE_NAME= "vnd.android.cursor.item/name";
    String MIMETYPE_ADDRESS= "vnd.android.cursor.item/postal-address_v2";
    String MIMETYPE_PHONE= "vnd.android.cursor.item/phone_v2";
    String RECEIVED_SMS="android.provider.Telephony.SMS_RECEIVED";
    String SEND_SMS="com.example.day3_1_youlou.SEND_SMS";
}
