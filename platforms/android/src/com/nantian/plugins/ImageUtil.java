package com.nantian.plugins;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.graphics.Bitmap;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Typeface;  
import android.graphics.Bitmap.Config;  
import android.graphics.Paint.Align;  
import android.util.DisplayMetrics;
import android.util.Log; 

/**
 * 图片水印
*/
public class ImageUtil {
	 /** 
     * 进行添加水印图片和文字 
     *  
     * @param src 
     * @param waterMak 
     * @return 
     */  
	public static int MATIO;
	
    public static Bitmap createBitmap(Bitmap src,  String title) {  
        if (src == null) {  
            return src;  
        }  
        // 获取原始图片与水印图片的宽与高  
        int w = src.getWidth();  
        int h = src.getHeight();  
    //    int ww = waterMak.getWidth();  
      //  int wh = waterMak.getHeight();  
        
        Log.i("99990", title);
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);  
        Canvas mCanvas = new Canvas(newBitmap);  
        // 往位图中开始画入src原始图片  
        mCanvas.drawBitmap(src, 0, 0, null);  
        // 在src的右下角添加水印  
       // Paint paint = new Paint();  
        //paint.setAlpha(100);  
   //     mCanvas.drawBitmap(waterMak, w - ww - 5, h - wh - 5, paint);  
     //   NTDataDictionaryPlugin.dic.get("address_gps");
        // 开始加入文字  
        if (null != title) {  
            Paint textPaint = new Paint();  
            textPaint.setColor(Color.RED);  
          //  textPaint.setARGB(50, 4, 159, 241);
            textPaint.setTextSize(50);  
            String familyName = "宋体";  
            Typeface typeface = Typeface.create(familyName,  
                    Typeface.BOLD_ITALIC);  
            textPaint.setTypeface(typeface);  
            textPaint.setTextAlign(Align.CENTER);  
            SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss");   
            
            Date curDate =  new Date(System.currentTimeMillis()); 
            String  timeStr   =   formatter.format(curDate); 
            mCanvas.drawText(NTDataDictionaryPlugin.dic.get("address_gps")+timeStr, w / 2, h-50, textPaint);  
              
        }  
        
        mCanvas.save(Canvas.ALL_SAVE_FLAG);  
        mCanvas.restore();  
        return newBitmap; 
    }
    
  
}