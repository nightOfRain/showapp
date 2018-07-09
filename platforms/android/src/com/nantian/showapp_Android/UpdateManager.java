package com.nantian.showapp_Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
 






import java.io.BufferedReader;
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  
  
  




import java.net.URLConnection;
import com.nantian.plugins.NTDataDictionaryPlugin;
import com.nantian.showapp_Android.R;
import android.app.AlertDialog;  
import android.app.Dialog;  
import android.app.AlertDialog.Builder;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.Intent;  
import android.content.DialogInterface.OnClickListener;  
import android.net.Uri;  
import android.os.Handler;  
import android.os.Message;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.widget.ProgressBar;  
  
public class UpdateManager {  
  
    private Context mContext;  
      
    //提示语  
    private String updateMsg = "有最新的软件包哦，亲快下载吧~";  

    //返回的安装包url  需要和后台交互
    private String apkUrl ;  
    private String newCode ; 
    private String currCode ; 
      
    private Dialog noticeDialog;  
      
    private Dialog downloadDialog;  
     /* 下载包安装路径 */  
    private static String savePath = "/sdcard/appcheck/download"; 
      
    private static  String saveFileName = "";  
  //  private static final String saveFileName = savePath + "YiChang_Android_1.5.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */  
    private ProgressBar mProgress;  
  
      
    private static final int DOWN_UPDATE = 1;  
      
    private static final int DOWN_OVER = 2;  
      
    private int progress;  
      
    private Thread downLoadThread;  
      
    private boolean interceptFlag = false;  
      
    private Handler mHandler = new Handler(){  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case DOWN_UPDATE:  
                mProgress.setProgress(progress);  
                break;  
            case DOWN_OVER:  
                mProgress.setVisibility(View.GONE);
                installApk();  
                break;  
            default:  
                break;  
            }  
        };  
    };  
      
    public UpdateManager(Context context) {  
    	 
        this.mContext = context;  
    }  
      
    //外部接口让主Activity调用  
    public void checkUpdateInfo(String apkurl, String newCode, String versionCode, String pathstr){  
    	Log.i("CHeckUpdate", "asdfas");
    	this.apkUrl = apkurl;
    	this.newCode = newCode;
    	this.currCode = versionCode;
    	this.savePath = pathstr;
        showNoticeDialog();  
        
        return ;
    }  
      
      
    private void showNoticeDialog(){  
        AlertDialog.Builder builder = new Builder(mContext);  
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(" "+Float.parseFloat(currCode));
        sb.append(", 发现新版本:");
        sb.append(" "+Float.parseFloat(newCode));
        sb.append(", 是否更新?");
        builder.setTitle("软件版本更新");  
        builder.setMessage(sb.toString());  
        builder.setPositiveButton("更新", new OnClickListener() {           
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                showDownloadDialog();            
            }  
        });  
        builder.setNegativeButton("暂不更新", new OnClickListener() {            
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();   
                interceptFlag=true;
            	
            }  
        });  
        noticeDialog = builder.create();  
        noticeDialog.show();  
        
        
    }  
      
    public void showDownloadDialog(){  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("软件版本更新");  
         
        final LayoutInflater inflater = LayoutInflater.from(mContext);  
        View v = inflater.inflate(R.layout.progress, null);  
        mProgress = (ProgressBar)v.findViewById(R.id.progress);  
        
        builder.setView(v);  
        builder.setNegativeButton("取消", new OnClickListener() {   
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                interceptFlag = true;  
            }  
        });  
        downloadDialog = builder.create();  
        downloadDialog.show();  
         
        downloadApk();  
    }  
      
    private Runnable mdownApkRunnable = new Runnable() {      
        @Override  
        public void run() {  
            try {  
                URL url = new URL(apkUrl);                
                URLConnection conn = url.openConnection();  
                int length = conn.getContentLength();  
                
                Log.i("length", "="+length);
                InputStream is = conn.getInputStream();  
                  

                 
                saveFileName = savePath + "/appCheck_AndroidRelease.apk";
                String apkFile = saveFileName;  
                File ApkFile = new File(apkFile);  
               FileOutputStream fos = new FileOutputStream(ApkFile);  
               
                int count = 0;  
                byte buf[] = new byte[1024];  
        
                File file = new File(savePath);  
                if(!file.exists()){  
                    file.mkdirs();  
                } 
                do{                  
                    int numread = is.read(buf);  
                   // Log.i("numread", "long="+numread);
                    count += numread;  
                    progress =(int)(((float)count / length) * 100);  
                    //更新进度  
                    mHandler.sendEmptyMessage(DOWN_UPDATE);  
                    if(numread <= 0){      
                        //下载完成通知安装  
                        mHandler.sendEmptyMessage(DOWN_OVER);  
                        break;  
                    }  
                    fos.write(buf,0,numread);  
                   // Thread.sleep(50);
                }while(!interceptFlag);//点击取消就停止下载.  
                  
                fos.close();  
                is.close();  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch(IOException e){  
                e.printStackTrace();  
            }  
              
        }  
    };  
      
     /**
     * 下载apk
     * @param url
     */  
      
    private void downloadApk(){  
        downLoadThread = new Thread(mdownApkRunnable);  
        downLoadThread.start();  
    }  
     /**
     * 安装apk
     * @param url
     */  
    private void installApk(){  
        File apkfile = new File(saveFileName);  
        Log.i("FILE", saveFileName);
        if (!apkfile.exists()) {  
            return;  
        }      
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");   
        mContext.startActivity(i);      
    }  
    
}
