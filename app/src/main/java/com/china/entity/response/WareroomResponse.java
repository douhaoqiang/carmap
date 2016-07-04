package com.china.entity.response;

import com.china.entity.Wareroom;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12.
 */
public class WareroomResponse extends Response {
    public Body body;


    public class Body{
        public ArrayList<Wareroom> warehouses;
    }
}
