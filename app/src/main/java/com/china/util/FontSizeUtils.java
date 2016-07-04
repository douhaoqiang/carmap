package com.china.util;

import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.china.GlobalConstants;

/**
 * Created by renzhan on 15/5/25.
 */
public class FontSizeUtils {
    public static float getPx(int ui_px){
        return ui_px* GlobalConstants.ScreenWidth/ GlobalConstants.DesignScreenWidth;

    }

    public static void setFontSize(AQuery aq, int ... parms){
        if(aq == null)
            Log.e("FontSizeUtils", "setFontSize aq=null");
        if(parms != null){
            if(parms.length % 2 > 0)
                Log.e("FontSizeUtils", "setFontSize parms.length=" + parms.length + " aq=" + aq.getView());
            for(int i=0; i+1<parms.length; i+=2){
                TextView tv = aq.id(parms[i]).getTextView();
                if(tv != null && parms[i+1]>0)
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPx(parms[i+1]));
                else{
                    Log.e("FontSizeUtils", "setFontSize parms[" + i + "] " + parms[i] + "," + parms[i+1]);
                }
            }
        }
    }

    public static void setFontSize(TextView textview, int sp){
        if(textview != null)
            textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, getPx(sp));
        else
            Log.e("FontSizeUtils", "setFontSize textview=null");
    }

    public static float getTextWidth(String text, int ui_px) { //第一个参数是要计算的字符串，第二个参数是字提大小
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(getPx(ui_px));
        return FontPaint.measureText(text);
    }
}
