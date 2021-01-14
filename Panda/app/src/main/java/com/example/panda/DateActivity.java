package com.example.panda;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateActivity extends AppCompatActivity {
    private DatePicker dp;
    private TimePicker tp;

    private String hour;
    private String minute;
    private String t; //시간

    private String year;
    private String month;
    private String day;
    private String d; //날짜

    private String dt;

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        TextView tv = findViewById(R.id.today_date);
        Calendar cal = Calendar.getInstance();
        tv.setText(cal.get(Calendar.YEAR) +"-"+ (cal.get(Calendar.MONTH)+1) +"-"+ cal.get(Calendar.DATE));
     ///////////////////////////////////////////////////////////////////////////////////////////////
        dp=(DatePicker)findViewById(R.id.dp);
        d = dp.getYear() + "-" + dp.getMonth()+1 + "-" + dp.getDayOfMonth() + " "; //초기값
        dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //monthOfYear는 0값이 1월을 뜻하므로 1을 더해줌 나머지는 같다.
                        d = year + "-" + monthOfYear+1 + "-" + dayOfMonth + " ";
                        dt=d+t;
                    }
                });
    ////////////////////////////////////////////////////////////////////////////////////////////////
        tp=(TimePicker)findViewById(R.id.tp);
        tp.setIs24HourView(true); //24시 표기법
        t= tp.getHour() + ":" + setMinute(tp.getMinute()) + ":00";
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                t= hour + ":" + setMinute(min) + ":00"; //시간 설정
                dt=d+t;
            }
        });
        dt=d+t;
    ////////////////////////////////////////////////////////////////////////////////////////////////
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
        Log.v("HelloAlarmActivity", mCalender.getTime().toString());
        Button button = (Button) findViewById(R.id.dateSetting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });
    }
    private String setMinute(int min) {
        if (min >= 10)
            minute = min + "";
        else
            minute = "0" + min;
        return minute;
    }

    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(DateActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DateActivity.this, 0, receiverIntent, 0);

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(dt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetime);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("", String.valueOf(e));
        }

    }
}