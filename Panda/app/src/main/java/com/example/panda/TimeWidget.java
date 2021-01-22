package com.example.panda;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Calendar;

public class TimeWidget extends AppWidgetProvider {
        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_view_widget);
            Calendar cal = Calendar.getInstance();
            remoteViews.setTextViewText(R.id.widget_test_textview, cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));

            Intent intent = new Intent(context, MemoActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_test_textview, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }

        @Override
        public void onEnabled(Context context) {
            Intent intent = new Intent(context, TimeWidget.class);
            intent.putExtra("mode","time");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, 0);

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);

        }

        //마지막 제거되는 순간
        @Override
        public void onDisabled(Context context) {
            //새로고침 기능 넣을거, 알람기능 등록!
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, TimeWidget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,0);
            alarmManager.cancel(pendingIntent);//알람 해제
            pendingIntent.cancel(); //인텐트 해제

        }

        /**
         * AppWidgetProvider가 브로드캐스트 하위 클래스이기 때문에 이와같은 onReceive함수가 있다.
         *      * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if(intent.getStringExtra("mode") != null){
                Calendar current = Calendar.getInstance(); //캘린더 싱글톤
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.list_view_widget);
                remoteViews.setTextViewText(R.id.widget_test_textview, current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(new ComponentName(context, TimeWidget.class),remoteViews);

            }else {
                super.onReceive(context, intent);
            }

        }
    }

