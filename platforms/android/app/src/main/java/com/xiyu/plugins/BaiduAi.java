package com.xiyu.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.baidu.ocr.demo.FileUtil;

import com.baidu.ocr.demo.RecognizeService;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;
import com.xiyu.showapp.MainActivity;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


public class BaiduAi extends CordovaPlugin {
    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    private static final int REQUEST_CODE_ACCURATE = 108;
    private static final int REQUEST_CODE_GENERAL_ENHANCED = 109;
    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110;
    private static final int REQUEST_CODE_BANKCARD = 111;
    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;
    private static final int REQUEST_CODE_DRIVING_LICENSE = 121;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
    private static final int REQUEST_CODE_RECEIPT = 124;

    private static final int REQUEST_CODE_PASSPORT = 125;
    private static final int REQUEST_CODE_NUMBERS = 126;
    private static final int REQUEST_CODE_QRCODE = 127;
    private static final int REQUEST_CODE_BUSINESSCARD = 128;
    private static final int REQUEST_CODE_HANDWRITING = 129;
    private static final int REQUEST_CODE_LOTTERY = 130;
    private static final int REQUEST_CODE_VATINVOICE = 131;
    private CallbackContext callbackContext;
    public Activity activity;
    public String token;
    @Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) 
            throws JSONException {
        activity = this.cordova.getActivity();
        this.callbackContext = callbackContext;
        Log.i("BaiduAi", args.getString(0));
        Log.i("BaiduAi", args.getString(1));

        String queryType = args.getString(0);
       // initAccessTokenWithAkSk();
        token = MainActivity.token;

        switch(queryType){
            case "bankcard": //银行卡
                //code
                getBankCard(args.getString(1));
                break;
            case "idcard": //身份证
                //code
                getIDCard(args.getString(1));
                break;
                default:
                    break;
        }


        Log.i("8888","token="+token);



        Log.i("getstockinfo", "Ok");
        return true;
    }

    private void getBankCard(String filePath){
        // 银行卡识别url
        String bankcardIdentificate = "https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard";
        // 本地图片路径
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            byte[] imgData = com.xiyu.plugins.FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String accessToken = token;
            String result = com.xiyu.plugins.HttpUtil.post(bankcardIdentificate, accessToken, params);
            System.out.println(result);
            this.callbackContext.success(result);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getIDCard(String filePath){
        // 身份证识别url
        String idcardIdentificate = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";

        try {
            byte[] imgData = com.xiyu.plugins.FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            // 识别身份证正面id_card_side=front;识别身份证背面id_card_side=back;
            String params = "id_card_side=front&" + URLEncoder.encode("image", "UTF-8") + "="
                    + URLEncoder.encode(imgStr, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String accessToken = token;
            String result = HttpUtil.post(idcardIdentificate, accessToken, params);
            System.out.println(result);
            this.callbackContext.success(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}