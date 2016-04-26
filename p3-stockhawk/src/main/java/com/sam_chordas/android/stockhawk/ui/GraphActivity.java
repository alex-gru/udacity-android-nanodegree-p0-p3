package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alexgru on 25-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class GraphActivity extends AppCompatActivity {

    public static final String SELECTED_SYMBOL = "SELECTED_SYMBOL";
    public static Context mContext;
    private LineChartView lineChartView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_line_graph);

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");

        String selectedSymbol = getIntent().getExtras().getString(SELECTED_SYMBOL);
        lineChartView = (LineChartView) findViewById(R.id.linechart);

        Cursor data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{
                        QuoteColumns.BIDPRICE + ", " +
                        QuoteColumns.CREATED },
                QuoteColumns.SYMBOL + " = ?",
                new String[]{selectedSymbol}, null);

        if (data == null || data.getCount() == 0) {
            return;
        }

        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        float points[] = new float[data.getCount()];
        String labels[] = new String[data.getCount()];
        float min = 99999999;
        float max = 0;
        int count = data.getCount();
        for(int index = 0; !data.isAfterLast(); index++) {
            float bidPrice = data.getFloat(0);
            String created = data.getString(1);
            points[index] = bidPrice;
            labels[index] = "";

            if (bidPrice < min)
                min = bidPrice;
            if (bidPrice > max)
                max = bidPrice;

            if (index == 0 || index == count/2 || index == count - 1) {
                Date date = null;
                try {
                    date = format.parse(created);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                labels[index] = df.format(date);
            }
            data.moveToNext();
        }

        LineSet dataset = new LineSet(labels, points);
        int minInt = (int)Math.floor(min);
        int maxInt = (int)Math.ceil(max);
        lineChartView.setAxisBorderValues(minInt, maxInt, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dataset.setColor(getColor(R.color.material_cyan_700));
            lineChartView.setAxisColor((getColor(R.color.white)));
            lineChartView.setLabelsColor((getColor(R.color.white)));

        } else {
            dataset.setColor(ContextCompat.getColor(mContext, R.color.material_cyan_700));
            lineChartView.setAxisColor(ContextCompat.getColor(mContext, R.color.white));
            lineChartView.setLabelsColor(ContextCompat.getColor(mContext, R.color.white));
        }
        dataset.setDotsRadius(0);
        lineChartView.addData(dataset);
        lineChartView.setXAxis(false);
        lineChartView.setXLabels(AxisController.LabelPosition.OUTSIDE);
        lineChartView.setYLabels(AxisController.LabelPosition.INSIDE);
        lineChartView.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lineChartView.dismiss();
    }
}
