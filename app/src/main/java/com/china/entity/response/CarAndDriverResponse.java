package com.china.entity.response;

import com.china.entity.Driver;
import com.china.entity.Vehicle;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12.
 */
public class CarAndDriverResponse extends Response{

    public Body body;

    public class Body{
        public ArrayList<Vehicle> vehicles;
        public ArrayList<Driver> drivers;
    }

}
