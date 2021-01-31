package com.example.panda;

        import android.app.PendingIntent;
        import android.appwidget.AppWidgetManager;
        import android.appwidget.AppWidgetProvider;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.RemoteViews;

        import java.util.ArrayList;
        import java.util.List;
        import androidx.annotation.RequiresApi;

/**
 * Implementation of App Widget functionality.
 */
public class ListViewWidget extends AppWidgetProvider {

    public static final String ACTION_TOAST = "actionToast";

    /**
     * 위젯의 크기 및 옵션이 변경될 때마다 호출되는 함수
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        여기부분 다 사용할 일 없어져서 주석처리함!
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_listview);
        views.setTextViewText(R.id.widget_test_textview, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * 위젯이 바탕화면에 설치될 때마다 호출되는 함수
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

            // RemoteViewsService 실행 등록시키는 함수
            Intent serviceIntent = new Intent(context, MyRemoteViewsService.class);
            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.widget_listview);

            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            widget.setRemoteAdapter(R.id.widget_listview, serviceIntent);

            //Listview 클릭 이벤트를 위한 코드. -> 원리는 pendingIntent 부여 -> 하나씩 부여하기에는 부담이 되어서 각 항목의 클릭이 아닌 위젯 자체에 대한 클릭
            Intent toastIntent = new Intent(context, ListViewWidget.class);
            toastIntent.setAction(ListViewWidget.ACTION_TOAST);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            widget.setPendingIntentTemplate(R.id.widget_listview, toastPendingIntent);              //listView의 collection에 하나하나 pendingIntent를 부여하는 것은 메모리 부담이 많이 됨. 그래서 콜렉션에 하나의 펜딩인텐트만 부여한다.

            //보내기
            appWidgetManager.updateAppWidget(appWidgetIds, widget);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(ACTION_TOAST)) {

            Intent i = new Intent(context, MemoActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        super.onReceive(context, intent);
    }
}