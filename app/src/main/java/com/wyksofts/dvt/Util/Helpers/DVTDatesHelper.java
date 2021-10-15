package com.wyksofts.dvt.Util.Helpers;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DVTDatesHelper {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Calendar cal = Calendar.getInstance();

        Date date=cal.getTime();
        String[] days = new String[6];
        days[0]=sdf.format(date);

        for(int i = 1; i< 6; i++){
            cal.add(Calendar.DAY_OF_MONTH,+1);
            date=cal.getTime();
            days[i]=sdf.format(date);
        }

        for(int i = (days.length-1); i >= 0; i--){ }

        return days;
    }
}
