package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by alexgru on 25-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class GraphActivity extends AppCompatActivity {

    public static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_line_graph);
    }
}
