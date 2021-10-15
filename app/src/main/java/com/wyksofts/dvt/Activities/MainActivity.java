package com.wyksofts.dvt.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.wyksofts.dvt.R;
import com.wyksofts.dvt.ui.TodayView.TodayFragment;

public class MainActivity extends AppCompatActivity {

    Dialog close_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showTodayfrag();
        close_home=new Dialog(getApplicationContext());
    }

    private void showTodayfrag(){
        getSupportFragmentManager()
                .beginTransaction().add(R.id.root_layout_nav, new TodayFragment()).commit();
    }


    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager().popBackStack();
        }else {
            close_home.setContentView(R.layout.close_home);
            close_home.findViewById(R.id.yes_exit).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finishAffinity();
                }
            });
            close_home.findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    close_home.dismiss();
                }
            });
            close_home.setCancelable(false);
            close_home.getWindow().setBackgroundDrawable((Drawable) new ColorDrawable(0));
            close_home.show();
        }
    }
}