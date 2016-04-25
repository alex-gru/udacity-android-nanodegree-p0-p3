package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockIntentService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by Alex Gru on 24-Apr-16.
 */
public class StockWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, StockWidgetIntentService.class);
        intent.setAction(MyStocksActivity.ACTION_DATA_UPDATED);
        context.startService(intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        Intent intent = new Intent(context, StockWidgetIntentService.class);
        intent.setAction(MyStocksActivity.ACTION_DATA_UPDATED);
        context.startService(intent);    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (MyStocksActivity.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            intent = new Intent(context, StockWidgetIntentService.class);
            intent.setAction(MyStocksActivity.ACTION_DATA_UPDATED);
            context.startService(intent);        }
    }
}
