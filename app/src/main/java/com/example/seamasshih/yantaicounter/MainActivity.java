package com.example.seamasshih.yantaicounter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Clock clock;
    DatePickerDialog datePickerDialog;
    DatePickerDialog comeDatePickerDialog;
    int year = -1,month = -1,date = -1,mode = -1;
    int comeYear = -1, comeMonth = -1, comeDate = -1;
    SharedPreferences sharedPreferences;


    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        clock = findViewById(R.id.clock);
        clock.setMainActivity(this);
        sharedPreferences = getSharedPreferences("backTaiwan", Context.MODE_PRIVATE);
        mode = sharedPreferences.getInt("mode",0);

        setComeDatePick();

        if (sharedPreferences.getInt("year",-1) != -1 &&
                sharedPreferences.getInt("month",-1) != -1 &&
                sharedPreferences.getInt("date",-1) != -1){
            year = sharedPreferences.getInt("year",-1);
            month = sharedPreferences.getInt("month",-1);
            date = sharedPreferences.getInt("date",-1);
            if (!clock.setDate(year,month,date,mode)) {
                Toast toast = Toast.makeText(getApplicationContext(),"返台時間過囉～", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show(); }
        }
        datePickerDialog = new DatePickerDialog(this,3);
        datePickerDialog.setTitle("請選擇返台日期");
        datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (year == -1 || month == -1 || date == -1)
                    datePickerDialog.getDatePicker().updateDate(
                            Integer.valueOf(clock.getDate().substring(0,4)),
                            Integer.valueOf(clock.getDate().substring(4,6))-1,
                            Integer.valueOf(clock.getDate().substring(6,8))
                    );
                else {
                    datePickerDialog.getDatePicker().updateDate(year, month-1, date);
                }
            }
        });
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                MainActivity.this.year = year;
                MainActivity.this.month = month;
                MainActivity.this.date = dayOfMonth;
                if (!clock.setDate(year,month,dayOfMonth,mode)) {
                    Toast toast = Toast.makeText(getApplicationContext(),"太早返台囉～", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("year",year);
                    editor.putInt("month",month);
                    editor.putInt("date",date);
                    editor.apply();
                }
            }
        });

        clock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                datePickerDialog.show();
                return true;
            }
        });
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = (mode+1)%5;
                if (!clock.setDate(year,month,date,mode)) {
                    Toast toast = Toast.makeText(getApplicationContext(),"返台時間過囉～", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show(); }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("mode",mode);
                editor.apply();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                             View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.N)
    void setComeDatePick(){
        if (sharedPreferences.getInt("comeYear",-1) != -1 &&
                sharedPreferences.getInt("comeMonth",-1) != -1 &&
                sharedPreferences.getInt("comeDate",-1) != -1){
            comeYear = sharedPreferences.getInt("comeYear",-1);
            comeMonth = sharedPreferences.getInt("comeMonth",-1);
            comeDate = sharedPreferences.getInt("comeDate",-1);
            if (!clock.setComeDate(comeYear,comeMonth,comeDate)) {
                Toast toast = Toast.makeText(getApplicationContext(),"太晚出差囉～", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show(); }
        }
        comeDatePickerDialog = new DatePickerDialog(this,3);
        comeDatePickerDialog.setTitle("請選擇出差日期");
        comeDatePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (comeYear == -1 || comeMonth == -1 || comeDate == -1) {
                    comeDatePickerDialog.getDatePicker().updateDate(
                            Integer.valueOf(clock.getDate().substring(0, 4)),
                            Integer.valueOf(clock.getDate().substring(4, 6)) - 1,
                            Integer.valueOf(clock.getDate().substring(6, 8))
                    );
                }
                else {
                    comeDatePickerDialog.getDatePicker().updateDate(comeYear, comeMonth-1, comeDate);
                }
            }
        });
        comeDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                MainActivity.this.comeYear = year;
                MainActivity.this.comeMonth = month;
                MainActivity.this.comeDate = dayOfMonth;
                if (!clock.setComeDate(year,month,dayOfMonth)) {
                    Toast toast = Toast.makeText(getApplicationContext(),"太晚出差囉～", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("comeYear",year);
                    editor.putInt("comeMonth",month);
                    editor.putInt("comeDate",dayOfMonth);
                    editor.apply();
                }
                if (!clock.setDate(MainActivity.this.year,MainActivity.this.month,MainActivity.this.date,mode)) {
                    Toast toast = Toast.makeText(getApplicationContext(),"太早返台囉～", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    boolean isDateEarly() {
        if (Integer.valueOf(clock.getDate().substring(0, 4)) > sharedPreferences.getInt("year", -1))
            return true;
        else if (Integer.valueOf(clock.getDate().substring(0, 4)) < sharedPreferences.getInt("year", -1))
            return false;
        else if (Integer.valueOf(clock.getDate().substring(4, 6)) > sharedPreferences.getInt("month", -1))
            return true;
        else if (Integer.valueOf(clock.getDate().substring(4, 6)) < sharedPreferences.getInt("month", -1))
            return false;
        else if (Integer.valueOf(clock.getDate().substring(6, 8)) > sharedPreferences.getInt("date", -1))
            return true;
        else if (Integer.valueOf(clock.getDate().substring(6, 8)) < sharedPreferences.getInt("date", -1))
            return false;
        else
            return false;
    }
}