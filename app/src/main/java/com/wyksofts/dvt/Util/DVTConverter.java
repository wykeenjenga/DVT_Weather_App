package com.wyksofts.dvt.Util;

import java.text.DecimalFormat;

public class DVTConverter {
    public String getCelcius(float temp){
        float value = (float) (temp-273.15);
        DecimalFormat df = new DecimalFormat("#.00");
        String result = df.format(value);
        return result;
    }


    public double getLocale(double locale){
        DecimalFormat df = new DecimalFormat("#.00000");
        String rs = df.format(locale);
        return Double.parseDouble(rs);
    }
}
