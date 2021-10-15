package com.wyksofts.dvt.model.todayweather;

import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("name")
    public String name;
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
}
