package com.wyksofts.dvt.Util;


import com.wyksofts.dvt.R;

public class DVTWeatherIconManager {

    public static int getIcon(String weatherIcon) {
        int icon;
        switch (weatherIcon) {
            case "Clear":
            case "Sands":
                icon = R.drawable.sea_sunnypng;
                break;


            case "Rain":
            case "Snow":
            case "Drizzle":
            case "Thunderstorm":
                icon = R.drawable.sea_rainy;
                break;

            case "Clouds":
                icon = R.drawable.sea_cloudy;
                break;

            case "01d":
            case "01n":
                icon = R.drawable.ic_sunny;
                break;

            case "09n":
            case "09d":
            case "10d":
            case "10n":
            case "11d":
            case "11n":
                icon = R.drawable.ic_rainy;
                break;

            case "02d":
            case "02n":
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                icon = R.drawable.ic_cloudy;
                break;

            default:
                icon = R.drawable.cloudy_card;
        }
        return icon;
    }

}
