package com.wyksofts.dvt.network.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wyksofts.dvt.Util.DVTConstants;
import com.wyksofts.dvt.ui.TodayView.DateTimeEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NetworkManager {

    private static NetworkManager instance;
    double latitude;
    double longitude;

    private RequestQueue requestQueue;
    private static Context context;


    private NetworkManager(Context context){
        NetworkManager.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkManager getInstance(Context context){
        if (instance == null){
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }

    public void setLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String weatherService() {
        String url = DVTConstants.BASE_URL_FORECAST +
                "lat=" +latitude+
                "&lon=" +longitude+
                "&appid="+ DVTConstants.API;
        return url;
    }

    public void GETWeather(final NetworkListener<ArrayList> okListener,
                           final NetworkListener errorListener){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, weatherService(),
                null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try{
                    // Parse our json response and put it into a data structure of our choosing - at the moment just an arrayList
                    ArrayList result = parseWeatherObject(response);

                    okListener.onResult(result);
                }catch (JSONException e){
                    errorListener.onResult(null);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                errorListener.onResult(null);
            }
        });
        addToRequestQueue(jsObjRequest);
    }




    private ArrayList parseWeatherObject(JSONObject json)
            throws JSONException{

        ArrayList arrayList = new ArrayList();
        JSONArray list=json.getJSONArray("list");

        for(int i=0;i<list.length();i+=8){
            DateTimeEntry dtEntry = new DateTimeEntry();

            JSONObject dtItem = list.getJSONObject(i);

            JSONObject dailyForecast = list.getJSONObject(i);
            JSONObject tempObject = dailyForecast.getJSONObject("main");
            JSONArray weatherArray = dtItem.getJSONArray("weather");
            JSONObject ob = (JSONObject) weatherArray.get(0);


            dtEntry.date = dtItem.getString("dt_txt");

            dtEntry.condation = ob.getString("main");
            dtEntry.temperature = tempObject.getString("temp");

            dtEntry.icon = ob.getString("icon");

            arrayList.add(dtEntry);
        }
        return arrayList;
    }
}
