package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
    private Cursor data;
    private SimpleDateFormat format;
    private SimpleDateFormat df;
    private TextView noDataTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_line_graph);

        format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        df = new SimpleDateFormat("MM-dd HH:mm");

        String selectedSymbol = getIntent().getExtras().getString(SELECTED_SYMBOL);
        lineChartView = (LineChartView) findViewById(R.id.linechart);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);

        data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{
                        QuoteColumns.BIDPRICE + ", " +
                        QuoteColumns.CREATED },
                QuoteColumns.SYMBOL + " = ?",
                new String[]{selectedSymbol}, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lineChartView.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (data == null || data.getCount() < 2) {
            noDataTextView.setVisibility(View.VISIBLE);
        } else {
            noDataTextView.setVisibility(View.GONE);

            int count = data.getCount();

            if (!data.moveToFirst()) {
                data.close();
                return;
            }

            float points[] = new float[count];
            String labels[] = new String[count];

            float min = 99999999;
            float max = 0;
            int step = 1;

            if (count > 200) {
                step = 20;
                points = new float[count/step];
                labels = new String[count/step];
            }


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
                if (index == points.length -1)
                    break;
                int nextIndex = index + step;
                int newIndex = index;
                while (newIndex < nextIndex && newIndex < points.length - 1) {
                    data.moveToNext();
                    newIndex++;
                }
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
    }
}
