package com.sam_chordas.android.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by alexgru on 25-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class StockWidgetIntentService extends IntentService {

    public StockWidgetIntentService(){
        super(StockWidgetIntentService.class.getName());
    }

    public StockWidgetIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (MyStocksActivity.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            // Retrieve all of the Today widget ids: these are the widgets we need to update
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                    StockWidgetProvider.class));

            Log.d(MyStocksActivity.TAG, "From StockWidgetIntentService!");

            // Get DB stock data, which "IS_CURRENT" (pseudo-boolean DB field)
            Cursor data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{QuoteColumns.SYMBOL + ", " +
                            QuoteColumns.BIDPRICE + ", " +
                            QuoteColumns.CHANGE + ", " +
                            QuoteColumns.PERCENT_CHANGE + ", " +
                            QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"}, null);
            Log.d(MyStocksActivity.TAG, "count from content resolver: " + data.getCount());

            if (data == null || data.getCount() < 2) {
              return;
            }

            if (!data.moveToFirst()) {
                data.close();
                return;
            }
            int sdk = Build.VERSION.SDK_INT;

            // Perform this loop procedure for each widget
            for (int appWidgetId : appWidgetIds) {
                int layoutId = R.layout.widget;
                RemoteViews views = new RemoteViews(getPackageName(), layoutId);

                views.setTextViewText(R.id.stock_symbol,data.getString(data.getColumnIndex(QuoteColumns.SYMBOL)));
                views.setTextViewText(R.id.bid_price,data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE)));
                views.setTextViewText(R.id.change, data.getString(data.getColumnIndex(QuoteColumns.CHANGE)));

                Intent launchIntent = new Intent(this, MyStocksActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//
//        for (int appWidgetId : appWidgetIds) {
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
//
//            Intent intent = new Intent(context, MyStocksActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
//    }
}
