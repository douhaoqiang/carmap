package com.china.util;

import android.view.View;

import com.androidquery.AQuery;
import com.china.R;

/**
 * Created by renzhan on 15/7/29.
 */
public class HeaderUtils {
    AQuery aq;

    public HeaderUtils(AQuery aq, String title) {
        this.aq = aq;
        setMiddleText(title);
        aq.id(R.id.header_left_layout).invisible();
        aq.id(R.id.header_right_layout).invisible();
    }

    public HeaderUtils(AQuery aq, int id) {
        this.aq = aq;
        setMiddleText(id);
        aq.id(R.id.header_left_layout).invisible();
        aq.id(R.id.header_right_layout).invisible();
//        FontSizeUtils.setFontSize(aq, R.key.header_left_text, 46
//                , R.key.header_right_text, 46
//                , R.key.header_title, 55);
    }

    public HeaderUtils setMiddleText(String title) {
        aq.id(R.id.header_title).visible().text(title);
        return this;
    }

    public HeaderUtils setMiddleText(int id) {
        aq.id(R.id.header_title).visible().text(id);
        return this;
    }

    public HeaderUtils setLeftImage(int id, View.OnClickListener click) {
        setLeftClick(click);
        aq.id(R.id.header_left_layout).visible();
        aq.id(R.id.header_left_button).visible().image(id);
        aq.id(R.id.header_left_text).invisible();
        return this;
    }

    public HeaderUtils setLeftText(int id, View.OnClickListener click) {
        setLeftClick(click);
        aq.id(R.id.header_left_layout).visible();
        aq.id(R.id.header_left_button).invisible();
        aq.id(R.id.header_left_text).visible().text(id);
        return this;
    }

    public HeaderUtils setLeftText(String text, View.OnClickListener click) {
        setLeftClick(click);
        aq.id(R.id.header_left_layout).visible();
        aq.id(R.id.header_left_button).invisible();
        aq.id(R.id.header_left_text).visible().text(text);
        return this;
    }

    public HeaderUtils setLeftClick(View.OnClickListener click) {
        if (click != null)
            aq.id(R.id.header_left_layout).clicked(click);
        return this;
    }

    public HeaderUtils setRightImage(int id, View.OnClickListener click) {
        setRightClick(click);
        aq.id(R.id.header_right_layout).visible();
        aq.id(R.id.header_right_button).visible().image(id);
        aq.id(R.id.header_right_text).invisible();
        return this;
    }

    public HeaderUtils setRightImage(int id) {
        aq.id(R.id.header_right_layout).visible();
        aq.id(R.id.header_right_button).visible().image(id);
        aq.id(R.id.header_right_text).invisible();
        return this;
    }

    public HeaderUtils setRightText(int id, View.OnClickListener click) {
        setRightClick(click);
        aq.id(R.id.header_right_layout).visible();
        aq.id(R.id.header_right_button).invisible();
        aq.id(R.id.header_right_text).visible().text(id);
        return this;
    }

    public HeaderUtils setRightText(String text, View.OnClickListener click) {
        setRightClick(click);
        aq.id(R.id.header_right_layout).visible();
        aq.id(R.id.header_right_button).invisible();
        aq.id(R.id.header_right_text).visible().text(text);
        return this;
    }

    public HeaderUtils setRightClick(View.OnClickListener click) {
        if (click != null)
            aq.id(R.id.header_right_layout).clicked(click);
        return this;
    }

    public HeaderUtils gone(){
        aq.id(R.id.header_root_view).gone();
        return this;
    }

    public void setBackgroundRes(int resId){
        aq.id(R.id.header_root_view).background(resId);
    }

    public void setBackgroundColor(int Color){
        aq.id(R.id.header_root_view).backgroundColor(Color);
    }


}