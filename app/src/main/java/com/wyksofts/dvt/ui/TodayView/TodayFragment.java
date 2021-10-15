package com.wyksofts.dvt.ui.TodayView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wyksofts.dvt.Activities.SplashScreen;
import com.wyksofts.dvt.Adapter.forecastAdapter;
import com.wyksofts.dvt.R;
import com.wyksofts.dvt.Util.DVTConstants;
import com.wyksofts.dvt.Util.DVTConverter;
import com.wyksofts.dvt.Util.DVTWeatherIconManager;
import com.wyksofts.dvt.network.networking.NetworkListener;
import com.wyksofts.dvt.model.todayweather.WeatherResponse;
import com.wyksofts.dvt.network.networking.WeatherService;
import com.wyksofts.dvt.network.networking.NetworkManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TodayFragment extends Fragment {

    TextView tTemperature;
    TextView tMinTemperature;
    TextView tMaxTemperature;
    TextView currentTemperature;
    TextView tempCondation;
    Dialog loading;

    ImageView climate_bg;

    RecyclerView recyclerview;
    forecastAdapter adapter;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    protected LocationManager locationManager;
    protected double latitude;
    protected double longitude;
    String baseUrl = DVTConstants.BASE_URL;
    String API_URL = DVTConstants.API;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    NetworkManager networkManager;


    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentData();
            }else{
                startActivity(new Intent(getActivity(), SplashScreen.class));
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkManager = NetworkManager.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getContext().getSharedPreferences("Location", 0);
        editor = pref.edit();

        loading = new Dialog(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        tTemperature = view.findViewById(R.id.today_temp);
        tMaxTemperature = view.findViewById(R.id.max_temp);
        tMinTemperature = view.findViewById(R.id.min_temp);
        currentTemperature = view.findViewById(R.id.current_temp);
        climate_bg = view.findViewById(R.id.climate_bg);
        tempCondation = view.findViewById(R.id.today_temp_name);

        recyclerview = view.findViewById(R.id.other_days_recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(llm);

        getCurrentLocation();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(),
                "android.permission.ACCESS_FINE_LOCATION") == 0) {
        } else {

        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(getActivity(), SplashScreen.class));
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                getCurrentData();
                            }
                        }
                    });
        }
    }

    void getCurrentData() {
        showLoading();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(latitude, longitude, API_URL);
        call.enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    loading.dismiss();

                    tTemperature.setText(new DVTConverter().getCelcius(weatherResponse.main.temp)+"°C");
                    tMinTemperature.setText(new DVTConverter().getCelcius(weatherResponse.main.temp_min)+"°C");
                    tMaxTemperature.setText(new DVTConverter().getCelcius(weatherResponse.main.temp_max)+"°C");
                    currentTemperature.setText(new DVTConverter().getCelcius(weatherResponse.main.temp));
                    tempCondation.setText(weatherResponse.name+"\t\t"+weatherResponse.sys.country);
                    //tempCondation.setText(String.valueOf(weatherResponse.weather.get(0).main));
                    setBackgroundDrawable(String.valueOf(weatherResponse.weather.get(0).main));

                    getForecast(latitude,longitude);
                }
            }
            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                tempCondation.setText(R.string.unable_to_fetch);
                loading.dismiss();
            }
        });

    }

    private void setBackgroundDrawable(String imageChoice){
        climate_bg.setBackgroundResource(new DVTWeatherIconManager().getIcon(imageChoice));
    }

    void getForecast(double latitude, double longitude){
        showLoading();

        networkManager.setLocation(latitude,longitude);

        networkManager.GETWeather(new NetworkListener<ArrayList>() {
            @Override
            public void onResult(ArrayList object)
            {
                // Create an adapter using our result set
                forecastAdapter adapter = new forecastAdapter(getActivity(), object);
                loading.dismiss();
                recyclerview.setAdapter(adapter);
                Toast.makeText(getContext(), "forecast successfully fetched:", Toast.LENGTH_SHORT).show();
            }
        }, new NetworkListener()
        {
            @Override
            public void onResult(Object object)
            {
                loading.dismiss();
                Toast.makeText(getContext(), "failed to fetch!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //show loading diag
    public void showLoading(){
        loading.setContentView(R.layout.loading_diag);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.show();
    }

}